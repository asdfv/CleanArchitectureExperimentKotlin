package by.grodno.vasili.presentation.dagger;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;

import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Singleton;

import by.grodno.vasili.data.datasource.FirebaseNoteEntityDatasource;
import by.grodno.vasili.data.datasource.NoteEntityDatasource;
import by.grodno.vasili.data.entity.mapper.NoteEntityDataMapper;
import by.grodno.vasili.data.repository.NoteDataRepository;
import by.grodno.vasili.domain.executor.PostExecutionThread;
import by.grodno.vasili.domain.executor.SubscriberThread;
import by.grodno.vasili.presentation.thread.IOThread;
import by.grodno.vasili.presentation.thread.UIThread;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
abstract class AppModule {
    @SuppressWarnings("unused")
    @Binds
    @Singleton
    abstract Context bindContext(Application application);

    @SuppressWarnings("unused")
    @Binds
    abstract ViewModelProvider.Factory provideViewModelFactory(ViewModelProviderFactory viewModelProviderFactory);

    @SuppressWarnings("unused")
    @Binds
    @Singleton
    abstract PostExecutionThread bindUiThread(UIThread uiThread);

    @SuppressWarnings("unused")
    @Binds
    @Singleton
    abstract SubscriberThread bindIoThread(IOThread ioThread);

    @Provides
    @Singleton
    static NoteDataRepository provideNoteDataRepository() {
        NoteEntityDataMapper mapper = new NoteEntityDataMapper();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        NoteEntityDatasource datasource = new FirebaseNoteEntityDatasource(mapper, database);
        return new NoteDataRepository(datasource, mapper);
    }
}
