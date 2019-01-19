package by.grodno.vasili.presentation.feature.addnote;

import android.arch.lifecycle.ViewModel;

import com.annimon.stream.function.Consumer;

import javax.inject.Inject;

import by.grodno.vasili.domain.interactor.SaveNoteUseCase;
import by.grodno.vasili.domain.model.Note;
import io.reactivex.observers.DisposableSingleObserver;

/**
 * View model for activity with add note functionality
 */
public class AddNoteViewModel extends ViewModel {
    private final SaveNoteUseCase useCase;

    @Inject
    AddNoteViewModel(SaveNoteUseCase useCase) {
        this.useCase = useCase;
    }

    /**
     * Save one note to repository
     *
     * @param note      to save
     * @param onSuccess success callback
     * @param onError   error callback
     */
    void saveNoteAsync(Note note, Consumer<String> onSuccess, Consumer<Throwable> onError) {
        DisposableSingleObserver<String> observer = new DisposableSingleObserver<String>() {
            @Override
            public void onSuccess(String savedId) {
                onSuccess.accept(savedId);
            }

            @Override
            public void onError(Throwable e) {
                onError.accept(e);
            }
        };
        useCase.execute(observer, SaveNoteUseCase.Params.create(note));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        useCase.dispose();
    }
}
