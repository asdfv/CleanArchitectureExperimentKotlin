package by.grodno.vasili.presentation.thread;

import javax.inject.Inject;

import by.grodno.vasili.domain.executor.SubscriberThread;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Thread for IO-bound work
 */
public class IOThread implements SubscriberThread {
    @Inject
    IOThread() {
    }

    @Override
    public Scheduler getScheduler() {
        return Schedulers.io();
    }
}
