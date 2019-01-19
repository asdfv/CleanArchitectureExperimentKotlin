package by.grodno.vasili.presentation.feature.notes;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import by.grodno.vasili.domain.interactor.DeleteNoteUseCase;
import by.grodno.vasili.domain.interactor.GetNotesListUseCase;
import by.grodno.vasili.domain.model.Note;
import by.grodno.vasili.presentation.model.NoteItem;
import by.grodno.vasili.presentation.model.NoteItemMapper;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import timber.log.Timber;

/**
 * View model for activity witch present list of notes
 */
public class NotesViewModel extends ViewModel {
    private final GetNotesListUseCase getNotesListUseCase;
    private final DeleteNoteUseCase deleteNoteUseCase;
    private final NoteItemMapper mapper;
    private MutableLiveData<List<NoteItem>> notesLiveData;

    @Inject
    NotesViewModel(GetNotesListUseCase getNotesListUseCase, DeleteNoteUseCase deleteNoteUseCase, NoteItemMapper mapper) {
        this.getNotesListUseCase = getNotesListUseCase;
        this.deleteNoteUseCase = deleteNoteUseCase;
        this.mapper = mapper;
    }

    /**
     * Init {@link LiveData} if needed and run loading data for her from repository
     *
     * @return LiveData instance
     */
    LiveData<List<NoteItem>> getNotesLiveData() {
        if (notesLiveData == null) {
            notesLiveData = new MutableLiveData<>();
            reloadData();
        }
        return notesLiveData;
    }

    /**
     * Remove note from repository by and update {@link LiveData}
     *
     * @param id        identifier
     * @param position  in recycler view
     * @param onSuccess success callback
     */
    void removeNoteAsync(String id, int position, Runnable onSuccess) {
        DisposableCompletableObserver observer = new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                List<NoteItem> noteItems = notesLiveData.getValue();
                if (noteItems != null) {
                    noteItems.remove(position);
                    notesLiveData.setValue(noteItems);
                    onSuccess.run();
                }
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e, "Error deleting note with id = %s", id);
            }
        };
        deleteNoteUseCase.execute(observer, DeleteNoteUseCase.Params.create(id));
    }

    /**
     * Reload {@link LiveData} with callback
     */
    void reloadData(Runnable onComplete) {
        loadNotesAsync(onComplete);
    }

    /**
     * Reload {@link LiveData}
     */
    void reloadData() {
        loadNotesAsync(() -> {});
    }

    private void loadNotesAsync(Runnable onComplete) {
        DisposableSingleObserver<List<Note>> observer = new DisposableSingleObserver<List<Note>>() {
            @Override
            public void onSuccess(List<Note> receivedNotes) {
                notesLiveData.setValue(mapper.mapList(receivedNotes));
                onComplete.run();
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e, "Error executing use case for get all notes");
                onComplete.run();
            }
        };
        getNotesListUseCase.execute(observer, null);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        getNotesListUseCase.dispose();
        deleteNoteUseCase.dispose();
    }
}
