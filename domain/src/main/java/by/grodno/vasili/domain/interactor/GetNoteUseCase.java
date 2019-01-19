package by.grodno.vasili.domain.interactor;

import by.grodno.vasili.domain.executor.PostExecutionThread;
import by.grodno.vasili.domain.executor.SubscriberThread;
import by.grodno.vasili.domain.model.Note;
import by.grodno.vasili.domain.repository.NoteRepository;
import io.reactivex.Maybe;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableMaybeObserver;

/**
 * Use case for retrieving one {@link by.grodno.vasili.domain.model.Note} from repository
 */
public class GetNoteUseCase extends UseCase<DisposableMaybeObserver<Note>, GetNoteUseCase.Params> {
    private final SubscriberThread subscriberThread;
    private final PostExecutionThread postExecutionThread;
    private final NoteRepository repository;
    private final CompositeDisposable disposables;

    public GetNoteUseCase(SubscriberThread subscriberThread, PostExecutionThread postExecutionThread, NoteRepository repository) {
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
    public void execute(DisposableMaybeObserver<Note> observer, Params params) {
        final Maybe<Note> observable = repository.getOne(params.id)
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
