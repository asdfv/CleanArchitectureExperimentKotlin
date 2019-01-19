package by.grodno.vasili.data.datasource;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.apache.commons.lang3.ObjectUtils;

import java.util.List;

import by.grodno.vasili.data.entity.NoteEntity;
import by.grodno.vasili.data.entity.mapper.NoteEntityDataMapper;
import by.grodno.vasili.data.util.SingleValueOnSubscribe;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * {@link NoteEntityDatasource} implementation for work with Firebase realtime database
 */
public class FirebaseNoteEntityDatasource implements NoteEntityDatasource {
    private static final String NOTES_PATH = "notes";

    private final NoteEntityDataMapper mapper;
    private final DatabaseReference notesRef;

    public FirebaseNoteEntityDatasource(NoteEntityDataMapper mapper, FirebaseDatabase database) {
        this.notesRef = database.getReference(NOTES_PATH);
        this.mapper = mapper;
    }

    @Override
    public Maybe<NoteEntity> one(String id) {
        Query noteRef = notesRef.child(id);
        return Single.create(new SingleValueOnSubscribe(noteRef))
                .filter(DataSnapshot::exists)
                .map(mapper::convert)
                .filter(ObjectUtils::anyNotNull);
    }

    @Override
    public Single<List<NoteEntity>> all() {
        return Single.create(new SingleValueOnSubscribe(notesRef))
                .map(mapper::convertToCollection);
    }

    @Override
    public Completable delete(String id) {
        return Completable.create(emitter -> {
            DatabaseReference noteRef = notesRef.child(id);
            noteRef.removeValue(((error, ref) -> {
                if (emitter.isDisposed()) {
                    return;
                }
                if (error == null) {
                    emitter.onComplete();
                } else {
                    emitter.onError(error.toException());
                }
            }
            ));
        });
    }

    @Override
    public Single<String> insert(NoteEntity noteEntity) {
        return Single.create(emitter -> notesRef
                .push()
                .setValue(noteEntity, (error, ref) -> {
                    if (emitter.isDisposed()) {
                        return;
                    }
                    if (error == null) {
                        emitter.onSuccess(ref.getKey());
                    } else {
                        emitter.onError(error.toException());
                    }
                }));
    }

    @Override
    public Single<String> update(NoteEntity entity) {
        return Single.create(emitter -> notesRef.child(entity.id)
                .setValue(entity, (error, ref) -> {
                    if (emitter.isDisposed()) {
                        return;
                    }
                    if (error == null) {
                        emitter.onSuccess(ref.getKey());
                    } else {
                        emitter.onError(error.toException());
                    }
                }));
    }
}
