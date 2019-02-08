package com.terraware;

import java.util.concurrent.locks.LockSupport;

public class FwDirectWaker implements FwWakerIf {

    public void wakeThread(Thread pThread) {
        LockSupport.unpark(pThread);
    }

    @Override
    public void wakeThread(Wakeable pWakeable) {
        pWakeable.wake();
    }
}
