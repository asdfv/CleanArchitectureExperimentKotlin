package by.grodno.vasili.domain.executor;

import io.reactivex.Scheduler;

/**
 * Thread abstraction. In this thread subject is subscribed
 */
public interface PostExecutionThread {
    Scheduler getScheduler();
}
