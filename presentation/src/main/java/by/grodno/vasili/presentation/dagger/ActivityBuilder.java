package by.grodno.vasili.presentation.dagger;

import by.grodno.vasili.presentation.feature.addnote.AddNoteActivity;
import by.grodno.vasili.presentation.feature.addnote.AddNoteActivityModule;
import by.grodno.vasili.presentation.feature.addnote.AddNoteActivityScope;
import by.grodno.vasili.presentation.feature.notedetails.DetailsActivity;
import by.grodno.vasili.presentation.feature.notedetails.DetailsActivityModule;
import by.grodno.vasili.presentation.feature.notedetails.DetailsActivityScope;
import by.grodno.vasili.presentation.feature.notes.NotesActivity;
import by.grodno.vasili.presentation.feature.notes.NotesActivityModule;
import by.grodno.vasili.presentation.feature.notes.NotesActivityScope;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Create subcomponent and injector for activities
 */
@Module
interface ActivityBuilder {
    @SuppressWarnings("unused")
    @NotesActivityScope
    @ContributesAndroidInjector(modules = {NotesActivityModule.class})
    NotesActivity bindNotesActivity();

    @SuppressWarnings("unused")
    @AddNoteActivityScope
    @ContributesAndroidInjector(modules = {AddNoteActivityModule.class})
    AddNoteActivity bindAddNoteActivity();

    @SuppressWarnings("unused")
    @DetailsActivityScope
    @ContributesAndroidInjector(modules = {DetailsActivityModule.class})
    DetailsActivity bindDetailsActivity();
}
