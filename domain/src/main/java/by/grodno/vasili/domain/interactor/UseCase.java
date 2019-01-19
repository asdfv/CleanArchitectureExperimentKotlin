package by.grodno.vasili.domain.interactor;

import io.reactivex.disposables.CompositeDisposable;


/**
 * Abstract class for a Use Case.
 */
abstract class UseCase<T, Param> {
    /**
     * {@link CompositeDisposable} for cancel pending executions
     */
    abstract CompositeDisposable getDisposables();

    /**
     * Execute use case with params and observer
     *
     * @param observer for observing result of execution use case
     * @param params   for executing use case
     */
    abstract void execute(T observer, Param params);

    /**
     * Dispose execution
     */
    public void dispose() {
        CompositeDisposable disposables = getDisposables();
        if (disposables != null && !disposables.isDisposed()) {
            disposables.dispose();
        }
    }
}
