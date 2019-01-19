package by.grodno.vasili.presentation.feature.notedetails;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.annimon.stream.function.Consumer;

import javax.inject.Inject;

import by.grodno.vasili.domain.interactor.GetNoteUseCase;
import by.grodno.vasili.domain.interactor.SaveNoteUseCase;
import by.grodno.vasili.domain.model.Note;
import by.grodno.vasili.presentation.model.NoteItem;
import by.grodno.vasili.presentation.model.NoteItemMapper;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.observers.DisposableSingleObserver;
import timber.log.Timber;

/**
 * View model for activity with note details
 */
public class DetailsViewModel extends ViewModel {
    private final GetNoteUseCase getNoteUseCase;
    private final SaveNoteUseCase saveNoteUseCase;
    private final NoteItemMapper mapper;
    MutableLiveData<NoteItem> noteLiveData;

    @Inject
    DetailsViewModel(GetNoteUseCase getNoteUseCase, SaveNoteUseCase saveNoteUseCase, NoteItemMapper mapper) {
        this.getNoteUseCase = getNoteUseCase;
        this.saveNoteUseCase = saveNoteUseCase;
        this.mapper = mapper;
        this.noteLiveData = new MutableLiveData<>();
    }

    /**
     * Get one note from repository by id
     */
    void getNoteAsync(String id) {
        DisposableMaybeObserver<Note> observer = new DisposableMaybeObserver<Note>() {
            @Override
            public void onSuccess(Note note) {
                noteLiveData.setValue(mapper.map(note));
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e, "Error while retrieving Note");
            }

            @Override
            public void onComplete() {
                Timber.i("Not found Note with id: %s", id);
            }
        };
        getNoteUseCase.execute(observer, GetNoteUseCase.Params.create(id));
    }

    /**
     * Update note
     *
     * @param item      note
     * @param onSuccess success callback
     * @param onError   error callback
     */
    void save(NoteItem item, Consumer<String> onSuccess, Consumer<Throwable> onError) {
        DisposableSingleObserver<String> observer = new DisposableSingleObserver<String>() {
            @Override
            public void onSuccess(String id) {
                onSuccess.accept(id);
            }

            @Override
            public void onError(Throwable e) {
                onError.accept(e);
            }
        };
        saveNoteUseCase.execute(observer, SaveNoteUseCase.Params.create(mapper.reverseMap(item)));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        getNoteUseCase.dispose();
        saveNoteUseCase.dispose();
    }
}
