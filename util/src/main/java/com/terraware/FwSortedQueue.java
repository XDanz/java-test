package com.terraware;

import java.util.PriorityQueue;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A queue of sequenced numbered items. A dequeue operation will wait if necessary for the item with the next sequence
 * number to become available. An enqueue operation will wait until space is available if capacity is unavailable.
 * @param <T> The type of the items to be stored in the queue.
 * @author erik.sprang, Cinnober Financial Technology
 */
public class FwSortedQueue<T>
{
    private static final long cMaxSpin = 16;
    private static final Object cLockItem = new Object();
    private final static FwWakerIf cWaker = FwWakerIf.Singleton.get();

    private final Element<T>[] mData;
    private final int mSize;
    private final int mMask;
    private final String mName;
    private long mNextSeqNo;
    private volatile Element<T> mBlockingElement;
    private final AtomicInteger mQueueLength = new AtomicInteger(0);
    private final AtomicLong mMaxSeqNo = new AtomicLong(-1);
    private final PriorityQueue<PiggyContainer<T>> mPiggyBacks = new PriorityQueue<PiggyContainer<T>>();
    private final AtomicInteger mNumPiggys = new AtomicInteger();
    private final AtomicReference<Thread> mGlobalLockOwner = new AtomicReference<Thread>();

    /**
     * Create a sorted queue
     *
     * @param pQueueName
     *            Name of the queue
     * @param pMaxCapacity
     */
    public FwSortedQueue(String pQueueName, int pMaxCapacity) {
        this(pQueueName, pMaxCapacity, 0);
    }

    /**
     * Create a sorted queue that will start with sequence numbers others than
     * the default.
     *
     * @param pQueueName
     *            Name of the queue
     * @param pMaxCapacity
     *            capacity
     * @param pStartSeqNo
     *            The sequence number of the first object that will be put in
     *            the queue
     */
    @SuppressWarnings("unchecked")
    public FwSortedQueue(String pQueueName, int pMaxCapacity, long pStartSeqNo)
    {
        mName = pQueueName;
        mSize = powRound(pMaxCapacity);
        mData = new Element[mSize];
        for (int i = 0; i < mSize; ++i)
        {
            mData[i] = new Element<T>();
        }
        mMask = mSize - 1;
        resetSorterQueue(pStartSeqNo);
    }

    //Round size up to nearest power-of-two, so that we can use bit masking instead of modulo
    private static int powRound(int pVal)
    {
        if (pVal <= 0)
        {
            throw new IllegalArgumentException("Capacity must be larger than zero.");
        }

        int tHighBit = Integer.highestOneBit(pVal);
        if (pVal > tHighBit)
        {
            tHighBit <<= 1;
            if (tHighBit < 0)
            {
                tHighBit = Integer.MAX_VALUE;
            }
        }
        return tHighBit;
    }

    /**
     * Get the name of the queue
     * @return The name of the queue
     */
    public String getName()
    {
        return mName;
    }

    /**
     * Reset the sorter queue to a specific sequence number, clearing all current data in the queue
     * @param pNextSeqNo The next sequence number expected to be dequeued
     */
    public void resetSorterQueue(long pNextSeqNo)
    {
        acquireGlobalLock();
        try
        {
            wakeBlockingElement();

            synchronized (this)
            {
                mNextSeqNo = pNextSeqNo;
                mMaxSeqNo.set(pNextSeqNo - 1);
                for (int i = 0; i < mSize; ++i)
                {
                    long tSeqNo = (pNextSeqNo + i);
                    int tIndex = ((int) tSeqNo) & mMask;
                    Element<T> tElem = mData[tIndex];
                    tElem.resetFor(tSeqNo - mSize);
                }

                mQueueLength.set(0);
            }
        }
        finally
        {
            releaseGlobalLock();
        }
    }

    /**
     * Wait for the dequeuer sequence number to reach pPreviousSequenceNumber, and then update it to pNewNextSeqNo.
     * @param pPreviousSequenceNumber
     * @param pNewNextSeqNo
     */
    public void setSorterQueueInitialSequenceNumber(long pPreviousSequenceNumber, long pNewNextSeqNo)
    {
        StringBuilder tB = new StringBuilder("Jump over sequences in ").append(mName);
        boolean tRetry = true;
        while (tRetry) {
            acquireGlobalLock();
            try
            {
                wakeBlockingElement();

                synchronized (this)
                {
                    if (mNextSeqNo > pPreviousSequenceNumber)
                    {
                        tRetry = false;
                        tB.append(". Discard sequences in interval [");
                        tB.append(mNextSeqNo).append(" -> ").append((pNewNextSeqNo -1)).append("]");
                        tB.append(" Next expected is ").append(pNewNextSeqNo);
                        StringBuilder tDiscard = new StringBuilder();
                        for (long i = mNextSeqNo; i < (pNewNextSeqNo); i++)
                        {
                            int tIndex = (int) (i & mMask);
                            Element<?> tElem = mData[tIndex];
                            if (tElem.resetFor(i)) {
                                tDiscard.append(i).append(" ");
                                mQueueLength.decrementAndGet();
                            }
                            pollPiggy(i);
                        }
                        if (tDiscard.length() > 0) {
                            tB.append("\n\tDiscarded data at seq# ").append(tDiscard);
                        }

                        mNextSeqNo = pNewNextSeqNo;
                    }
                }
            }
            finally
            {
                releaseGlobalLock();
            }
            if (tRetry) {
                // This method should not be time critical ...
                try {
                    Thread.sleep(10);
                }
                catch (InterruptedException e) {
                    // By design ...
                }
            }
        }
    }

    /**
     * Poll the queue of piggybacked items
     * @param pSeqNo Sequence number limit for the piggybacked item (only items with seq < this parameter is valid)
     * @return An item, or null if no piggy backed items matching the sequence number criteria is available
     */
    private T pollPiggy(long pSeqNo)
    {
        if (mNumPiggys.get() <= 0)
        {
            return null;
        }

        synchronized (mPiggyBacks)
        {
            PiggyContainer<T> tPiggy = mPiggyBacks.peek();
            if (tPiggy != null)
            {
                if (tPiggy.getSequenceNumber() < pSeqNo)
                {
                    mPiggyBacks.remove();
                    mNumPiggys.decrementAndGet();
                    T tItem = tPiggy.getItem();
                    return tItem;
                }
            }
        }
        return null;
    }

    /**
     * Test if there is a piggybacked item available
     * @return
     */
    private boolean hasPiggy()
    {
        return mNumPiggys.get() > 0;
    }

    /**
     * Dequeue a single item
     * @param pWaitTime Maximum time to wait in, milliseconds. -1 => poll and return immediately, 0 => wait indefinitely
     * @param pMaxSeqNo Maximum sequence number of item to return
     * @return An item, if one matching search criteria exists. Null, if no match found within time limit.
     * @throws InterruptedException If thread was interrupted
     */
    private T dequeueSingle(long pWaitTime, long pMaxSeqNo) throws InterruptedException
    {
        return dequeueSingle(pWaitTime, pMaxSeqNo, cMaxSpin);
    }

    /**
     * Dequeue a single item
     * @param pWaitTime Maximum time to wait in, milliseconds. -1 => poll and return immediately, 0 => wait indefinitely
     * @param pMaxSeqNo Maximum sequence number of item to return
     * @param pSpin Maximum number of iterations to aggressively spin and poll the queue
     * @return An item, if one matching search criteria exists. Null, if no match found within time limit.
     * @throws InterruptedException If thread was interrupted
     */
    private T dequeueSingle(long pWaitTime, long pMaxSeqNo, long pSpin) throws InterruptedException
    {
        while (true)
        {
            try
            {
                synchronized (this)
                {
                    return dequeueSingleInternal(pWaitTime, pMaxSeqNo, pSpin);
                }
            }
            catch (SafepointRequiredException e)
            {
                handleSafePoint(e);
            }
        }
    }

    /**
     * Check if global lock has been requested by some other thread, and throw an exception in such case
     * @throws SafepointRequiredException If some other thread has requested a global lock
     */
    private void checkSafePoint() throws SafepointRequiredException
    {
        Thread tLockHolder = mGlobalLockOwner.get();
        if (tLockHolder != null && tLockHolder != Thread.currentThread())
        {
            throw new SafepointRequiredException();
        }
    }

    /**
     * Handle a safe point by waiting until global lock as been released
     * @param pE The exception raised, signaling that a global lock is required by some other thread
     * @throws InterruptedException If thread was interrupted while waiting for global lock to be released
     */
    private void handleSafePoint(SafepointRequiredException pE) throws InterruptedException
    {
        Thread tLockHolder = mGlobalLockOwner.get();
        if (tLockHolder != null && tLockHolder != Thread.currentThread())
        {
            synchronized (this)
            {
                while ((tLockHolder = mGlobalLockOwner.get()) != null && tLockHolder != Thread.currentThread())
                {
                    wait();
                }
            }
        }
    }


    /**
     * Dequeue a single item
     * @param pWaitTime Maximum time to wait in, milliseconds. -1 => poll and return immediately, 0 => wait indefinitely
     * @param pMaxSeqNo Maximum sequence number of item to return
     * @param pSpin Maximum number of iterations to aggressively spin and poll the queue
     * @return An item, if one matching search criteria exists. Null, if no match found within time limit.
     * @throws InterruptedException If thread was interrupted
     * @throws SafepointRequiredException If some other thread has requested a global lock. Retry after lock release!
     */
    private T dequeueSingleInternal(long pWaitTime, long pMaxSeqNo, long pSpin)
            throws InterruptedException, SafepointRequiredException
    {
        long tEnterTime = System.nanoTime();
        long tSeqNo = mNextSeqNo;

        T tItem;
        T tPiggyItem;
        if ((tPiggyItem = pollPiggy(tSeqNo)) != null)
        {
            return tPiggyItem;
        }

        if (pMaxSeqNo != -1 && tSeqNo > pMaxSeqNo)
        {
            return null;
        }

        int tIndex = ((int) tSeqNo) & mMask;
        Element<T> tElem = mData[tIndex];

        while ((tItem = tElem.getItem(tSeqNo)) == null)
        {
            if (pWaitTime < 0)
            {
                return null;
            }

            long tSpin = 1;
            while (tSpin < pSpin && tElem.mSeqNo.get() != tSeqNo)
            {
                if ((tPiggyItem = pollPiggy(tSeqNo)) != null)
                {
                    return tPiggyItem;
                }

                checkSafePoint(); //Abort if global lock is needed

                if (pWaitTime > 0 && System.nanoTime() - tEnterTime > (pWaitTime * 1000000))
                {
                    //Timeout
                    return null;
                }

                if (Thread.interrupted())
                {
                    throw new InterruptedException();
                }

                ++tSpin;
            }

            if (tSpin >= pSpin)
            {
                try
                {
                    tItem = tElem.awaitItem(tSeqNo, pWaitTime > 0 ?
                            pWaitTime - ((System.nanoTime() - tEnterTime) / 1000000) :
                            pWaitTime);
                }
                catch (TimeoutException e)
                {
                    return null;
                }

                if (tItem == null)
                {
                    //Possible piggy available
                    continue;
                }

                break;
            }
        }

        ++mNextSeqNo;
        mQueueLength.decrementAndGet();

        cWaker.wakeThread(tElem);

        return tItem;
    }

    /**
     * Dequeue the next element. If there is no element wait until there is one,
     * possibly wait for ever.
     *
     * @return the next element to be dequeued
     */
    public T dequeue()
    {
        try
        {
            return dequeueSingle(0, -1);
        }
        catch (InterruptedException e)
        {
            return null;
        }
    }

    /**
     * Dequeue the next element. If there is no element wait until there is one,
     * possibly wait for ever.
     *
     * @return the next element to be dequeued
     */
    public T dequeueAggressive()
    {
        try
        {
            return dequeueSingle(0, -1, Long.MAX_VALUE);
        }
        catch (InterruptedException e)
        {
            return null;
        }
    }

    /**
     * Dequeue the next element. If there is no element wait until there is one,
     * possibly wait for ever.
     *
     * @return the next element to be dequeued
     */
    public T dequeueAggressive(long pTimeOut)
    {
        try
        {
            return dequeueSingle(pTimeOut, -1, Long.MAX_VALUE);
        }
        catch (InterruptedException e)
        {
            return null;
        }
    }

    /**
     * The next element is dequeued. If the timeout is larger than 0, dequeue
     * will return null if the queue still does not contain the next element to
     * be dequeued after having waited the specified amount of time.
     *
     * @param pTimeOutMilliSeconds
     * @return the next element to be dequeued, or null
     */
    public T dequeue(long pTimeOutMilliSeconds)
    {
        try
        {
            return dequeueSingle(pTimeOutMilliSeconds, -1);
        }
        catch (InterruptedException e)
        {
            return null;
        }
    }

    /**
     * Dequeue the next element, with sequence <= parameter. If there is no element wait until there is one,
     * possibly wait for ever. Return null immediately if max sequence has already passed.
     *
     * @param Maximum allowed sequence number
     * @return the next element to be dequeued, or null if maximum sequence number has already passed
     */
    public T dequeueUpTo(long pSequenceNumber)
    {
        try
        {
            return dequeueSingle(-1, pSequenceNumber);
        }
        catch (InterruptedException e)
        {
            return null;
        }
    }

    /**
     * The next element is dequeued, with sequence <= parameter.
     * If the timeout is larger than 0, dequeue will return null if
     * the queue still does not contain the next element to be dequeued
     * after having waited the specified amount of time. If maximum sequence
     * has already passed, return null.
     *
     * @param Maximum allowed sequence number
     * @param pTimeOutMilliSeconds
     * @return the next element to be dequeued, or null
     */
    public T dequeueUpTp(long pSequenceNumber, long pTimeOutMilliSeconds)
    {
        try
        {
            return dequeueSingle(pTimeOutMilliSeconds, pSequenceNumber);
        }
        catch (InterruptedException e)
        {
            return null;
        }
    }

    /**
     * The next elements are dequeued. If the time out is larger than 0, dequeue
     * will return null if the queue still does not contain the next element to
     * be dequeued after having waited the specified amount of time.
     *
     * @param pTimeOutMilliSeconds
     *            Time to wait for next element(s)
     * @param pContainer
     *            An array into which dequeued elements are to be placed
     * @return the number of items dequeued, -1 if interrupted
     */
    public synchronized int dequeueArray(long pTimeOutMilliSeconds, T[] pContainer)
    {
        return dequeueArray(pTimeOutMilliSeconds, pContainer, -1);
    }

    /**
     * The next elements with sequence <= parameter are dequeued. If the time out is larger than 0, dequeue
     * will return null if the queue still does not contain the next element to
     * be dequeued after having waited the specified amount of time.
     *
     * @param pTimeOutMilliSeconds
     *            Time to wait for next element(s)
     * @param pContainer
     *            An array into which dequeued elements are to be placed
     * @param Maximum allowed sequence number
     * @return the number of items dequeued, -1 if interrupted
     */
    public int dequeueArray(long pTimeOutMilliSeconds, T[] pContainer, long pMaxSeqNo)
    {
        try
        {
            T tItem;
            if ((tItem = dequeueSingle(-1, pMaxSeqNo)) == null)
            {
                tItem = dequeueSingle(pTimeOutMilliSeconds, pMaxSeqNo);
            }
            if (tItem != null)
            {
                int tNumItems = 0;
                do
                {
                    pContainer[tNumItems++] = tItem;
                }
                while (tNumItems < pContainer.length && ((tItem = dequeueSingle(-1, pMaxSeqNo)) != null));
                return tNumItems;
            }
            return 0;
        }
        catch (InterruptedException e)
        {
            return -1;
        }
    }

    /**
     * Add an item to the queue. Sequence numbers must be consecutive and unique, but may be added out-of-order.
     * @param pElement Item to be added.
     * @param pSeqNo Sequence number of the item.
     * @return The current length of the queue.
     */
    public int enqueue(T pElement, long pSeqNo)
    {
        while (true)
        {
            try
            {
                return enqueueInternal(pElement, pSeqNo);
            }
            catch (SafepointRequiredException e)
            {
                try
                {
                    handleSafePoint(e);
                }
                catch (InterruptedException e1)
                {
                    continue;
                }
            }
        }
    }

    /**
     * Add an item to the queue. Sequence numbers must be consecutive and unique, but may be added out-of-order.
     * @param pElement Item to be added.
     * @param pSeqNo Sequence number of the item.
     * @return The current length of the queue.
     * @throws SafepointRequiredException If some other thread has requested a global lock. Retry after lock release!
     */
    private int enqueueInternal(T pElement, long pSeqNo) throws SafepointRequiredException
    {
        if ((pSeqNo & Long.MIN_VALUE) != 0)
        {
            throw new IllegalArgumentException("Negative sequence numbers not allowed.");
        }

        int tIndex = ((int) pSeqNo) & mMask;
        Element<T> tElem = mData[tIndex];
        if (!tElem.tryLock(pSeqNo))
        {
            int tSpin = 0;
            while (!tElem.tryLock(pSeqNo))
            {
                if (tElem.isOldSeqNo(pSeqNo))
                {
                    return getLength();
                }

                if (++tSpin < cMaxSpin)
                {
                    continue;
                }

                if (tElem.awaitQueueability(pSeqNo))
                {
                    break;
                }
                else
                {
                    return getLength();
                }
            }
        }

        //Lock success!
        while (true)
        {
            try
            {
                tElem.setItem(pSeqNo, pElement);
                break;
            }
            catch (SafepointRequiredException e)
            {
                try
                {
                    handleSafePoint(e);
                    if (!tElem.isLocked(pSeqNo))
                    {
                        throw e;
                    }
                }
                catch (InterruptedException e1)
                {
                    throw e;
                }
            }
        }

        //TODO: This may not be completely water tight if a reset is performed...
        int tQueueLength = mQueueLength.incrementAndGet();
        long tCurMax = mMaxSeqNo.get();
        while (pSeqNo > tCurMax)
        {
            if (mMaxSeqNo.compareAndSet(tCurMax, pSeqNo))
            {
                break;
            }
            tCurMax = mMaxSeqNo.get();
        }

        if (mBlockingElement == tElem)
        {
            cWaker.wakeThread(tElem);
        }

        return tQueueLength;
    }

    /**
     * Add an unsequenced item to the queue. Item will be dequeued after the current tail of the queue, at the earliest.
     * @param pElement Item to be added
     */
    public void enqueuePiggybacked(T pElement)
    {
        enqueuePiggybacked(pElement, mMaxSeqNo.get());
    }

    /**
     * Add an unsequenced item to the queue. Item will be dequeued after the specified sequence number, at the earliest.
     * @param pElement Item to be added
     * @param pPiggyBackSequence Sequence number after which the item may be dequeued
     */
    public void enqueuePiggybacked(T pElement, long pPiggyBackSequence)
    {
        synchronized (mPiggyBacks)
        {
            mPiggyBacks.add(new PiggyContainer<T>(pElement, pPiggyBackSequence));
            mNumPiggys.incrementAndGet();
        }

        wakeBlockingElement();
    }

    /**
     * Wakeup any potential consumer thread ...
     */
    private final void wakeBlockingElement() {
        Element<T> tBlocker = mBlockingElement;
        if (tBlocker != null)
        {
            //Might wake too many, but that is unlikely. All threads able to handle spurious wake up...
            cWaker.wakeThread(tBlocker);
        }
    }

    /**
     * Get list of sequence numbers currently absent between first and last element in queue
     * @return A list of the missing sequence numbers.
     * Ex: Queue contains [1, 4, 5, 6, 8, 10, 11] => getSeqNoHoles() returns [2, 3, 7, 9].
     */
    public long[] getSeqNoHoles()
    {
        acquireGlobalLock();
        try
        {
            wakeBlockingElement();

            synchronized (this)
            {
                return findHoles(mNextSeqNo, mMaxSeqNo.get(), 0);
            }
        }
        finally
        {
            releaseGlobalLock();
        }
    }

    /**
     * Create and populate list of holes
     * @param pStartSeq First sequence number at which to start looking for holes
     * @param pEndSequence Last sequence number to look at
     * @param pNumHolesDetected Total number of holes detected so far
     * @return A list of detected holes
     */
    private long[] findHoles(long pStartSeq, long pEndSequence, int pNumHolesDetected)
    {
        for (long i = pStartSeq; i <= pEndSequence; ++i)
        {
            Element<T> tElem = mData[(int) (i & mMask)];
            if (tElem.getSeqNo() != i || tElem.getItem() == null)
            {
                long[] tHoles = findHoles(i + 1, pEndSequence, pNumHolesDetected + 1);
                tHoles[pNumHolesDetected] = i;
                return tHoles;
            }
        }

        long[] tHoles = new long[pNumHolesDetected];
        return tHoles;
    }

    /**
     * Remove any items past the first hole, if any.
     * @return Number of items removed
     */
    public int removeHoles()
    {
        acquireGlobalLock();
        try
        {
            wakeBlockingElement();

            synchronized (this)
            {
                boolean tHoleFound = false;
                int tRemovedItems = 0;
                long tEnd = mMaxSeqNo.get();

                for (long i = mNextSeqNo; i <= tEnd; ++i)
                {
                    Element<T> tElem = mData[(int) (i & mMask)];
                    if (!tHoleFound)
                    {
                        if (tElem.getSeqNo() != i || tElem.getItem() == null)
                        {
                            mMaxSeqNo.set(i - 1);
                            tHoleFound = true;
                        }
                    }
                    else
                    {
                        tElem.setItem(null);
                        tElem.setSeqNo(i);
                        ++tRemovedItems;
                    }
                }

                mQueueLength.set(Math.max(0, mQueueLength.get() - tRemovedItems));
                return tRemovedItems;
            }
        }
        finally
        {
            releaseGlobalLock();
        }
    }

    /**
     * Get the current length of the queue
     * @return The current length of the queue
     */
    public int getLength()
    {
        return Math.max(0, mQueueLength.get() + mNumPiggys.get());
    }

    /**
     * Get next sequence number expected to be retrieved from the queue
     * @return
     */
    public long getNextSeqNo()
    {
        return mNextSeqNo;
    }

    /**
     * Get the maximum sequence number currently in the queue
     * @return
     */
    public long getMaxSeqNo()
    {
        return mMaxSeqNo.get();
    }

    /**
     * Acquire a global lock, so other threads won't add and remove stuff while we're mucking around
     */
    private void acquireGlobalLock()
    {
        Thread tLockOwner;
        while ((tLockOwner = mGlobalLockOwner.get()) != null)
        {
            if (tLockOwner == Thread.currentThread())
            {
                throw new IllegalStateException("Global lock may not be taken recursively");
            }

            try
            {
                wait();
            }
            catch (InterruptedException e)
            {
                //Continue to wait
            }
        }

        mGlobalLockOwner.set(Thread.currentThread());

        for (int i = 0; i < mData.length; ++i)
        {
            Element<T> tElem = mData[i];
            tElem.lock();
        }
    }

    /**
     * Release the global lock again.
     */
    private synchronized void releaseGlobalLock()
    {
        for (int i = 0; i < mData.length; ++i)
        {
            Element<T> tElem = mData[i];
            tElem.release();
        }

        if (!mGlobalLockOwner.compareAndSet(Thread.currentThread(), null))
        {
            throw new IllegalStateException("Current thread is not the owner of the global lock.");
        }

        notifyAll();
    }

    /**
     * Class encapsulating an element in the queue; with sequence number, item and some helper methods
     *
     * @author erik.sprang, Cinnober Financial Technology
     */
    private class Element<U> implements Wakeable
    {
        private final AtomicReference<Object> mItem = new AtomicReference<Object>();
        private final AtomicLong mSeqNo = new AtomicLong();

        private long mSavedSeqNo; //Backup during global lock
        private U mSavedItem;


        /**
         * Try to lock the element, in order to enqueue an item to it
         * @param pSequence Sequence number of the item we wish to enqueue into this element
         * @return True if the lock as granted, false otherwise
         */
        public boolean tryLock(long pSequence)
        {
            long tCurrent = mSeqNo.get();
            if (tCurrent != pSequence - mSize || mItem.get() != null)
            {
                return false;
            }

            long tReserveSeq = pSequence | Long.MIN_VALUE;
            return mSeqNo.compareAndSet(tCurrent, tReserveSeq);
        }

        /**
         * Reset the element so that the specified sequence number can be enqueued, if currently containing lower
         * @param pSeqNo SeqNo for which to prepare new enqueue
         */
        public synchronized boolean resetFor(long pSeqNo)
        {
            boolean tHadData = (getItem() != null);
            setSeqNo(pSeqNo);
            setItem(null);
            return tHadData;
        }

        /**
         * Wait until an item is available in this element
         * @param pSeqNo
         * @param pWaitTime
         * @return
         * @throws SafepointRequiredException
         * @throws InterruptedException
         * @throws TimeoutException
         */
        @SuppressWarnings("unchecked")
        public synchronized U awaitItem(final long pSeqNo, final long pWaitTime)
                throws SafepointRequiredException, InterruptedException, TimeoutException
        {
            final long tEnterTime = pWaitTime > 0 ? System.nanoTime() : 0;
            final long tWaitNanos = pWaitTime > 0 ? pWaitTime * 1000000L : 0;
            mBlockingElement = (Element<T>) this;
            try
            {
                U tItem;
                while ((tItem = getItem(pSeqNo)) == null)
                {
                    if (hasPiggy())
                    {
                        return null;
                    }

                    if (pWaitTime <= 0)
                    {
                        wait();
                    }
                    else
                    {
                        final long tDuration = System.nanoTime() - tEnterTime;
                        if (tDuration >= tWaitNanos)
                        {
                            throw new TimeoutException();
                        }
                        final long tMillisW = (tWaitNanos - tDuration) / 1000000L;
                        if (tMillisW > 0) {
                            wait(tMillisW);
                        }
                    }

                    checkSafePoint();
                }
                return tItem;
            }
            finally
            {
                mBlockingElement = null;
            }
        }

        public boolean isLocked(long pSeqNo)
        {
            long tReserveSeq = pSeqNo | Long.MIN_VALUE;
            return mSeqNo.get() == tReserveSeq;
        }

        public synchronized boolean awaitQueueability(long pSeqNo)
        {
            while (!tryLock(pSeqNo))
            {
                if (isOldSeqNo(pSeqNo))
                {
                    return false;
                }

                try
                {
                    wait();
                }
                catch (InterruptedException e)
                {
                    return false;
                }
            }

            return true;
        }

        public boolean isOldSeqNo(long pSeqNo)
        {
            long tCurrent = mSeqNo.get();
            if (tCurrent == pSeqNo - mSize)
            {
                return false;
            }

            if (tCurrent < -mSize)
            {
                tCurrent &= Long.MAX_VALUE;
            }

            boolean tIsOld = (tCurrent - pSeqNo >= 0);
            if (tIsOld)
            {
//                Logger.Singleton.get().log(
//                        "*** WARNING *** " + mName + ": Sorted queue ignoring duplicate seqno " + pSeqNo
//                                + ". Expected " + mNextSeqNo + " or higher. Current=" + tCurrent + " mSeqNo=" +
//                                mSeqNo.get());
            }

            return tIsOld;

        }

        public void setItem(long pSeqNo, U pItem) throws SafepointRequiredException
        {
            if (!mItem.compareAndSet(null, pItem))
            {
                throw new SafepointRequiredException();
            }
            if (!mSeqNo.compareAndSet(pSeqNo | Long.MIN_VALUE, pSeqNo))
            {
                throw new SafepointRequiredException();
            }
        }

        @SuppressWarnings("unchecked")
        public U getItem(long pSeqNo) throws SafepointRequiredException
        {
            if (mSeqNo.get() == pSeqNo)
            {
                Object tItem = mItem.get();
                if (tItem == null)
                {
                    return null;
                }
                if (tItem == cLockItem || !mItem.compareAndSet(tItem, null))
                {
                    throw new SafepointRequiredException();
                }

                return (U) tItem;
            }
            return null;
        }

        @SuppressWarnings("unchecked")
        public synchronized void lock()
        {
            mSavedItem = (U) mItem.getAndSet(cLockItem);
            mSavedSeqNo = mSeqNo.getAndSet(-1);
            notifyAll(); //All blocking threads that are possibly holding the read lock need to wake up and release it
        }

        public synchronized void release()
        {
            mSeqNo.set(mSavedSeqNo);
            mItem.set(mSavedItem);
            mSavedItem = null;
            notifyAll();
        }

        @SuppressWarnings("unchecked")
        public U getItem()
        {
            if (mItem.get() == cLockItem)
            {
                return mSavedItem;
            }
            return (U) mItem.get();
        }

        public long getSeqNo()
        {
            long tSeqNo = mSeqNo.get();
            if (tSeqNo == -1L)
            {
                return mSavedSeqNo;
            }
            return tSeqNo;
        }

        public void setSeqNo(long pSeqNo)
        {
            long tSeqNo = mSeqNo.get();
            if (tSeqNo == -1L)
            {
                mSavedSeqNo = pSeqNo;
            }
            else
            {
                mSeqNo.set(pSeqNo);
            }
        }

        public void setItem(U pItem)
        {
            if (mItem.get() == cLockItem)
            {
                mSavedItem = pItem;
            }
            else
            {
                mItem.set(pItem);
            }
        }

        @Override
        public synchronized void wake()
        {
            notifyAll();
        }
    }

    private static class PiggyContainer<T> implements Comparable<PiggyContainer<T>> {
        private static final AtomicLong mOrderCounter = new AtomicLong(Long.MIN_VALUE);

        private final T mItem;
        private final long mSequenceNumber;
        private final long mOrder = mOrderCounter.getAndIncrement();

        public PiggyContainer(T pItem, long pSequenceNumber) {
            mItem = pItem;
            mSequenceNumber = pSequenceNumber;
        }

        @Override
        public int compareTo(PiggyContainer<T> pO) {
            long tDiff = mSequenceNumber - pO.mSequenceNumber;
            if (tDiff < 0)
            {
                return -1;
            }
            if (tDiff > 0)
            {
                return 1;
            }
            tDiff = mOrder - pO.mOrder;
            if (tDiff < 0)
            {
                return -1;
            }
            if (tDiff > 0)
            {
                return 1;
            }
            return 0;
        }

        public T getItem()
        {
            return mItem;
        }

        public long getSequenceNumber()
        {
            return mSequenceNumber;
        }

        public String toString()
        {
            return String.format("PiggyBack[%d, '%s']", mSequenceNumber, mItem.toString());
        }
    }

    private static class SafepointRequiredException extends Exception {
        private static final long serialVersionUID = 1843346640588903435L;
    }
}
