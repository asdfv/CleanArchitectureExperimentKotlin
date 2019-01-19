package by.grodno.vasili.presentation.thread;

import javax.inject.Inject;

import by.grodno.vasili.domain.executor.PostExecutionThread;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Main thread for executing process for UI
 */
public class UIThread implements PostExecutionThread {
    @Inject
    UIThread() {
    }

    @Override
    public Scheduler getScheduler() {
        return AndroidSchedulers.mainThread();
    }
}
