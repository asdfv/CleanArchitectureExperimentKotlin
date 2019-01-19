package by.grodno.vasili.presentation.feature.addnote;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import by.grodno.vasili.data.repository.NoteDataRepository;
import by.grodno.vasili.domain.executor.PostExecutionThread;
import by.grodno.vasili.domain.executor.SubscriberThread;
import by.grodno.vasili.domain.interactor.SaveNoteUseCase;
import by.grodno.vasili.domain.model.Note;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Mockito.when;

/**
 * Test useCase correct work while saving {@link Note}
 */
@RunWith(MockitoJUnitRunner.class)
public class AddNoteViewModelTest {
    private static final String TEST_ID = "testId";
    private SaveNoteUseCase useCase;
    private Note note;
    @Mock
    private SubscriberThread subscriberThread;
    @Mock
    private PostExecutionThread postExecutionThread;
    @Mock
    private NoteDataRepository repository;

    @Before
    public void setUp() {
        Scheduler scheduler = Schedulers.trampoline();
        Single<String> single = Single.fromObservable(Observable.just(TEST_ID));
        note = new Note("asd", "sdf");
        when(repository.insert(note)).thenReturn(single);
        when(subscriberThread.getScheduler()).thenReturn(scheduler);
        when(postExecutionThread.getScheduler()).thenReturn(scheduler);
        useCase = new SaveNoteUseCase(subscriberThread, postExecutionThread, repository);
    }

    @Test
    public void saveNoteAsync() {
        DisposableSingleObserver<String> observer = new DisposableSingleObserver<String>() {
            @Override
            public void onSuccess(String id) {
                Assert.assertEquals("Retrieving not correct id" , id, TEST_ID);
            }

            @Override
            public void onError(Throwable e) {
                Assert.fail("Error while observing: " + e.getLocalizedMessage());
            }
        };
        SaveNoteUseCase.Params params = SaveNoteUseCase.Params.create(note);
        useCase.execute(observer, params);
    }
}
