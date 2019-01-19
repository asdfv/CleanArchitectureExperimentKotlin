package by.grodno.vasili.domain.interactor;

import by.grodno.vasili.domain.executor.PostExecutionThread;
import by.grodno.vasili.domain.executor.SubscriberThread;
import by.grodno.vasili.domain.model.Note;
import by.grodno.vasili.domain.repository.NoteRepository;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;

/**
 * Use case saving Note to repository
 */
public class SaveNoteUseCase extends UseCase<DisposableSingleObserver<String>, SaveNoteUseCase.Params> {
    private final SubscriberThread subscriberThread;
    private final PostExecutionThread postExecutionThread;
    private final NoteRepository repository;
    private final CompositeDisposable disposables;

    public SaveNoteUseCase(SubscriberThread subscriberThread, PostExecutionThread postExecutionThread, NoteRepository repository) {
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
    public void execute(DisposableSingleObserver<String> observer, Params params) {
        Note note = params.note;
        Single<String> operation = note.id == null ? repository.insert(note) : repository.update(note);
        final Single<String> observable = operation
                .subscribeOn(subscriberThread.getScheduler())
                .observeOn(postExecutionThread.getScheduler());
        disposables.add(observable.subscribeWith(observer));
    }


    public static final class Params {
        private final Note note;

        private Params(Note note) {
            this.note = note;
        }

        public static Params create(Note note) {
            return new Params(note);
        }
    }
}
