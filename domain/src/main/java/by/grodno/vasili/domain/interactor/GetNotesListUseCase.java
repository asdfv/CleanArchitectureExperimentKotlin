package by.grodno.vasili.domain.interactor;

import java.util.List;

import by.grodno.vasili.domain.executor.PostExecutionThread;
import by.grodno.vasili.domain.executor.SubscriberThread;
import by.grodno.vasili.domain.model.Note;
import by.grodno.vasili.domain.repository.NoteRepository;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;

/**
 * Use case retrieving notes list from repository
 */
public class GetNotesListUseCase extends UseCase<DisposableSingleObserver<List<Note>>, Void> {
    private final SubscriberThread subscriberThread;
    private final PostExecutionThread postExecutionThread;
    private final NoteRepository repository;
    private final CompositeDisposable disposables;

    public GetNotesListUseCase(SubscriberThread subscriberThread, PostExecutionThread postExecutionThread, NoteRepository repository) {
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
    public void execute(DisposableSingleObserver<List<Note>> observer, Void params) {
        final Single<List<Note>> observable = repository.getAll()
                .subscribeOn(subscriberThread.getScheduler())
                .observeOn(postExecutionThread.getScheduler());
        disposables.add(observable.subscribeWith(observer));
    }
}
