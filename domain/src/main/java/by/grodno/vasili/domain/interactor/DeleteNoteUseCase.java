package by.grodno.vasili.domain.interactor;

import by.grodno.vasili.domain.executor.PostExecutionThread;
import by.grodno.vasili.domain.executor.SubscriberThread;
import by.grodno.vasili.domain.repository.NoteRepository;
import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;

/**
 * Use case saving Note to repository
 */
public class DeleteNoteUseCase extends UseCase<DisposableCompletableObserver, DeleteNoteUseCase.Params> {
    private final SubscriberThread subscriberThread;
    private final PostExecutionThread postExecutionThread;
    private final NoteRepository repository;
    private final CompositeDisposable disposables;

    public DeleteNoteUseCase(SubscriberThread subscriberThread, PostExecutionThread postExecutionThread, NoteRepository repository) {
        this.subscriberThread = subscriberThread;
        this.postExecutionThread = postExecutionThread;
        this.repository = repository;
        this.disposables = new CompositeDisposable();
    }

    @Override
    CompositeDisposable getDisposables() {
        return disposables;
    }

    @Override
    public void execute(DisposableCompletableObserver observer, Params params) {
        final Completable observable = repository.delete(params.id)
                .subscribeOn(subscriberThread.getScheduler())
                .observeOn(postExecutionThread.getScheduler());
        disposables.add(observable.subscribeWith(observer));
    }

    public static final class Params {
        private final String id;

        private Params(String id) {
            this.id = id;
        }

        public static Params create(String id) {
            return new Params(id);
        }
    }
}
