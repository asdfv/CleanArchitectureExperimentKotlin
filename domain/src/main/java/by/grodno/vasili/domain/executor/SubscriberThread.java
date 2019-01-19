package by.grodno.vasili.domain.executor;

import io.reactivex.Scheduler;

/**
 * Thread abstraction. In this thread observable source emits elements
 */
public interface SubscriberThread {
    Scheduler getScheduler();
}
