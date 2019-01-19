package by.grodno.vasili.presentation.feature.addnote;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.annimon.stream.function.Consumer;

import java.util.Locale;

import javax.inject.Inject;

import by.grodno.vasili.domain.model.Note;
import by.grodno.vasili.presentation.R;
import by.grodno.vasili.presentation.databinding.ActivityAddnoteBinding;
import by.grodno.vasili.presentation.feature.base.BaseActivity;
import timber.log.Timber;

import static by.grodno.vasili.presentation.feature.base.Utils.MAX_DESCRIPTION_LENGTH;
import static by.grodno.vasili.presentation.feature.base.Utils.MAX_TITLE_LENGTH;
import static by.grodno.vasili.presentation.feature.base.Utils.validateDescription;
import static by.grodno.vasili.presentation.feature.base.Utils.validateTitle;
import static org.apache.commons.lang3.StringUtils.trim;

/**
 * Activity for add note functionality
 */
public class AddNoteActivity extends BaseActivity<ActivityAddnoteBinding> {
    public static final String NOTE_SAVED = "by.grodno.vasili.presentation.feature.addnote.EXTRA.NOTE_SAVED";
    private AddNoteViewModel model;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setHandler(new NoteHandler());
        model = ViewModelProviders.of(this, viewModelFactory).get(AddNoteViewModel.class);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_addnote;
    }

    /**
     * Handler class for Note view events
     */
    public class NoteHandler {
        /**
         * Create domain {@link Note} from entered by user fields and try to save it in ViewModel
         */
        public void save() {
            String title = trim(binding.titleInput.getText().toString());
            String description = trim(binding.descriptionInput.getText().toString());
            if (validateTitle(title) && validateDescription(description)) {
                saveNoteAsync(title, description);
            } else {
                showToast(String.format(Locale.US,
                        "Entered data can`t be empty. Maximum length for title: %d, for description: %d",
                        MAX_TITLE_LENGTH,
                        MAX_DESCRIPTION_LENGTH));
            }
        }
    }

    private void saveNoteAsync(String title, String description) {
        Consumer<String> onSuccess = id -> {
            showToast("Successfully saved " + id);
            Intent resultIntent = new Intent();
            resultIntent.putExtra(NOTE_SAVED, true);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        };
        Consumer<Throwable> onError = error -> {
            showToast("Saving error " + error.getLocalizedMessage());
            Timber.e(error, "Error executing use case");
            finish();
        };
        Note note = new Note(title, description);
        model.saveNoteAsync(note, onSuccess, onError);
    }
}
