package by.grodno.vasili.domain.repository;

import java.util.List;

import by.grodno.vasili.domain.model.Note;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * Implement this interface for work with Note
 */
public interface NoteRepository {

    /**
     * Takes one {@link Note} from datasource by id
     * @param id {@link Note} identifier
     * @return Single observable {@link Note}
     */
    Maybe<Note> getOne(String id);

    /**
     * Takes all {@link Note}`s from datasource
     * @return list of {@link Note}
     */
    Single<List<Note>> getAll();

    /**
     * Delete one {@link Note} by id
     * @param id {@link Note} identifier
     * @return observable result
     */
    Completable delete(String id);

    /**
     * Insert one {@link Note} to datasource
     * @return observable saved {@link Note} id
     */
    Single<String> insert(Note note);

    /**
     * Update {@link Note} by id
     * @return observable saved {@link Note} id
     */
    Single<String> update(Note note);
}
