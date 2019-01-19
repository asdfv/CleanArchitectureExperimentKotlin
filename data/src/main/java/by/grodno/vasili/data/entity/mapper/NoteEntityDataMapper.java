package by.grodno.vasili.data.entity.mapper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.annimon.stream.Stream;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.List;
import java.util.Map;

import by.grodno.vasili.data.entity.NoteEntity;
import by.grodno.vasili.domain.mapper.Mapper;
import by.grodno.vasili.domain.model.Note;
import timber.log.Timber;

import static java.util.Collections.emptyMap;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

/**
 * Mapper class used to transform Firebase {@link DataSnapshot} to {@link NoteEntity}
 * from data layer and {@link Note} in the domain layer.
 */
public class NoteEntityDataMapper extends Mapper<NoteEntity, Note> {

    @Override
    public Note map(NoteEntity entity) {
        if (entity == null) {
            Timber.d("Null NoteEntity while convert to Note");
            return null;
        }
        return new Note(entity.id, entity.title, entity.description, entity.getCreatedTimestamp());
    }

    @Override
    public NoteEntity reverseMap(Note note) {
        if (note == null) {
            Timber.d("Null Note while convert to NoteEntity");
            return null;
        }
        return new NoteEntity(note.id, note.title, note.description);
    }

    /**
     * Convert a {@link DataSnapshot} into an {@link NoteEntity}.
     */
    @Nullable
    public NoteEntity convert(DataSnapshot snapshot) {
        try {
            NoteEntity noteEntity = snapshot.getValue(NoteEntity.class);
            if (noteEntity == null) {
                throw new RuntimeException("Error converting Snapshot to NoteEntity");
            }
            noteEntity.id = snapshot.getKey();
            return noteEntity;
        } catch (DatabaseException e) {
            Timber.e(e, "Error converting snapshot to NoteEntity");
        }
        return null;
    }

    /**
     * Convert a {@link DataSnapshot} into a List of {@link NoteEntity}.
     */
    @NonNull
    public List<NoteEntity> convertToCollection(DataSnapshot snapshot) {
        return Stream.of(snapshot)
                .filter(DataSnapshot::exists)
                .map(this::getNoteEntitiesMap)
                .flatMap(map -> Stream.of(map.entrySet()))
                .filter(entry -> entry.getValue() != null)
                .map(entry -> {
                    NoteEntity entity = entry.getValue();
                    entity.id = entry.getKey();
                    return entity;
                })
                .toList();
    }

    private Map<String, NoteEntity> getNoteEntitiesMap(DataSnapshot snapshot) {
        try {
            GenericTypeIndicator<Map<String, NoteEntity>> type = new GenericTypeIndicator<Map<String, NoteEntity>>() {
            };
            return defaultIfNull(snapshot.getValue(type), emptyMap());
        } catch (DatabaseException e) {
            Timber.e(e, "Error converting snapshot to NoteEntity map");
        }
        return emptyMap();
    }
}
