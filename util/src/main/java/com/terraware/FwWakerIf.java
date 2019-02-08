package com.terraware;

public interface FwWakerIf {

    void wakeThread(Thread pThread);
    void wakeThread(Wakeable pWakeable);

    public static class Singleton
    {
        private static FwWakerProxy cSingleton = new FwWakerProxy(new FwDirectWaker());

        public static FwWakerIf get()
        {
            return cSingleton;
        }

        public static void setDefaultWaker(FwWakerIf pWaker)
        {
            if (pWaker == null)
            {
                throw new NullPointerException();
            }

            cSingleton.setWaker(pWaker);
        }

        private static class FwWakerProxy implements FwWakerIf
        {
            private volatile FwWakerIf mWaker;

            public FwWakerProxy(FwWakerIf pWaker)
            {
                mWaker = pWaker;
            }

            public void setWaker(FwWakerIf pWaker)
            {
                mWaker = pWaker;
            }

            @Override
            public void wakeThread(Thread pThread)
            {
                mWaker.wakeThread(pThread);
            }

            @Override
            public void wakeThread(Wakeable pWakeable)
            {
                mWaker.wakeThread(pWakeable);
            }
        }
    }

}
