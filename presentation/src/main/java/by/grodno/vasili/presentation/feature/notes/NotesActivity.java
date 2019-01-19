package by.grodno.vasili.presentation.feature.notes;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import javax.inject.Inject;

import by.grodno.vasili.presentation.R;
import by.grodno.vasili.presentation.databinding.ActivityNotesBinding;
import by.grodno.vasili.presentation.feature.addnote.AddNoteActivity;
import by.grodno.vasili.presentation.feature.base.BaseActivity;
import by.grodno.vasili.presentation.feature.notedetails.DetailsActivity;

/**
 * Activity for present list of notes
 */
public class NotesActivity extends BaseActivity<ActivityNotesBinding> {
    private static final int ADD_NOTE_REQUEST_CODE = 1;
    private static final int DETAILS_NOTE_REQUEST_CODE = 2;
    private NotesViewModel model;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    NotesAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this, viewModelFactory).get(NotesViewModel.class);
        initViews();
        initSwipeToDelete();
        model.getNotesLiveData().observe(this, adapter::setNotes);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_notes;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case ADD_NOTE_REQUEST_CODE: {
                    refreshIfNeeded(data, AddNoteActivity.NOTE_SAVED);
                }
                case DETAILS_NOTE_REQUEST_CODE: {
                    refreshIfNeeded(data, DetailsActivity.NOTE_CHANGED);
                }
            }
        }
    }

    /**
     * Handler for click on item in Recycler view
     *
     * @param id clicked note id
     */
    public void onItemClick(String id) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(DetailsActivity.ID, id);
        startActivityForResult(intent, DETAILS_NOTE_REQUEST_CODE);
    }

    private void refreshIfNeeded(@NonNull Intent data, String extraName) {
        boolean needRefresh = data.getBooleanExtra(extraName, false);
        if (needRefresh) {
            model.reloadData();
        }
    }

    private void initSwipeToDelete() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                String id = adapter.getItem(position).id;
                Runnable onSuccess = () -> showToast("Successfully removed id = " + id);
                model.removeNoteAsync(id, position, onSuccess);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(binding.recyclerView);
    }

    private void initViews() {
        initFloatingButton();
        initRecyclerView();
        initSwipeContainer();
    }

    private void initFloatingButton() {
        binding.fab.setOnClickListener(view -> startActivityForResult(new Intent(this, AddNoteActivity.class), ADD_NOTE_REQUEST_CODE));
    }

    private void initSwipeContainer() {
        binding.swipeContainer.setOnRefreshListener(() -> model.reloadData(() -> binding.swipeContainer.setRefreshing(false)));
    }

    private void initRecyclerView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }
}
