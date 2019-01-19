package by.grodno.vasili.presentation.feature.notedetails;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.annimon.stream.function.Consumer;

import java.util.Locale;

import javax.inject.Inject;

import by.grodno.vasili.presentation.R;
import by.grodno.vasili.presentation.databinding.ActivityDetailsBinding;
import by.grodno.vasili.presentation.feature.base.BaseActivity;
import by.grodno.vasili.presentation.model.NoteItem;
import timber.log.Timber;

import static by.grodno.vasili.presentation.feature.base.Utils.MAX_DESCRIPTION_LENGTH;
import static by.grodno.vasili.presentation.feature.base.Utils.MAX_TITLE_LENGTH;
import static by.grodno.vasili.presentation.feature.base.Utils.validateDescription;
import static by.grodno.vasili.presentation.feature.base.Utils.validateTitle;
import static org.apache.commons.lang3.StringUtils.trim;

/**
 * Activity for presenting information about note and edit it
 */
public class DetailsActivity extends BaseActivity<ActivityDetailsBinding> {
    public static final String ID = "by.grodno.vasili.presentation.feature.notedetails.EXTRA.ID";
    public static final String NOTE_CHANGED = "by.grodno.vasili.presentation.feature.notedetails.EXTRA.NOTE_CHANGED";

    private DetailsViewModel model;

    @Inject
    ViewModelProvider.Factory provider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this, provider).get(DetailsViewModel.class);
        binding.setHandler(new Handler());
        loadNoteAsync();
        model.noteLiveData.observe(this, noteItem -> binding.setNote(noteItem));
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_details;
    }

    private void loadNoteAsync() {
        String id = getIntent().getStringExtra(ID);
        if (id == null) {
            Timber.w("Error taking extra with note ID from intent");
            showToast("Sorry, we can`t show you this note details");
        } else {
            model.getNoteAsync(id);
        }
    }

    /**
     * Handler class for Details view events
     */
    public class Handler {
        /**
         * Enable editing Note fields
         */
        public void onEditClick() {
            EditText titleView = binding.titleText;
            EditText descriptionView = binding.descriptionText;
            enableEditing(titleView, descriptionView);
            binding.editSwitch.setVisibility(View.GONE);
        }

        /**
         * Checking entered data for correct and presence of changes and save
         */
        public void saveIfChanged() {
            NoteItem item = binding.getNote();
            String changedTitle = trim(binding.titleText.getText().toString());
            String changedDescription = trim(binding.descriptionText.getText().toString());
            if (!validateTitle(changedTitle) || !validateDescription(changedDescription)) {
                showToast(String.format(Locale.US,
                        "Entered data can`t be empty. Maximum length for title: %d, for description: %d",
                        MAX_TITLE_LENGTH,
                        MAX_DESCRIPTION_LENGTH));
                return;
            }
            NoteItem changedItem = new NoteItem(item.id, changedTitle, changedDescription, item.created);
            if (hasChanges(item, changedItem)) {
                Consumer<String> onSuccess = id -> showToast(String.format("Note %s successfully updated", id));
                Consumer<Throwable> onError = error -> showToast("Error updating note: " + error.getLocalizedMessage());
                model.save(changedItem, onSuccess, onError);
                notifyAboutChanges();
            }
            finish();
        }

        private void notifyAboutChanges() {
            Intent intent = new Intent();
            intent.putExtra(NOTE_CHANGED, true);
            setResult(RESULT_OK, intent);
        }

        private boolean hasChanges(NoteItem original, NoteItem changed) {
            return !original.title.equals(changed.title) || !original.description.equals(changed.description);
        }

        private void enableEditing(EditText... views) {
            for (EditText view : views) {
                view.setInputType(InputType.TYPE_CLASS_TEXT);
                view.setEnabled(true);
                view.setBackgroundResource(R.drawable.edit_text_background);
            }
        }
    }
}
