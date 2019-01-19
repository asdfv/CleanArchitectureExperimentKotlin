package by.grodno.vasili.presentation.feature.addnote;

import android.arch.lifecycle.ViewModel;

import by.grodno.vasili.data.repository.NoteDataRepository;
import by.grodno.vasili.domain.interactor.SaveNoteUseCase;
import by.grodno.vasili.presentation.dagger.ViewModelKey;
import by.grodno.vasili.presentation.thread.IOThread;
import by.grodno.vasili.presentation.thread.UIThread;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

@Module
public abstract class AddNoteActivityModule {

    @Provides
    @AddNoteActivityScope
    static SaveNoteUseCase provideSaveNoteUseCase(UIThread uiThread, IOThread ioThread, NoteDataRepository repository) {
        return new SaveNoteUseCase(ioThread, uiThread, repository);
    }

    @SuppressWarnings("unused")
    @Binds
    @IntoMap
    @ViewModelKey(AddNoteViewModel.class)
    abstract ViewModel bindAddNoteViewModel(AddNoteViewModel addNoteViewModel);
}
