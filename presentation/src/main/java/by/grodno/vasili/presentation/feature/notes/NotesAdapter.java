package by.grodno.vasili.presentation.feature.notes;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import by.grodno.vasili.presentation.R;
import by.grodno.vasili.presentation.databinding.NoteItemBinding;
import by.grodno.vasili.presentation.model.NoteItem;

/**
 * Adapter for notes recycler view
 */
@NotesActivityScope
class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {
    private List<NoteItem> notes;

    @Inject
    NotesAdapter() {
        this.notes = new ArrayList<>();
    }

    /**
     * Set new notes for render in recycler view
     */
    public void setNotes(List<NoteItem> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    /**
     * Get item in recycler view by position
     */
    NoteItem getItem(int position) {
        return notes.get(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NoteItem item = notes.get(position);
        holder.binding.setNote(item);
        holder.binding.setHandler((NotesActivity) holder.itemView.getContext());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    /**
     * ViewHolder class for Notes Recycler view
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        NoteItemBinding binding;

        ViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }
}
