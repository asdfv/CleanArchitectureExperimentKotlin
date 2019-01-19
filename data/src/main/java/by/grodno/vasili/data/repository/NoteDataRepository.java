package by.grodno.vasili.data.repository;

import java.util.List;

import by.grodno.vasili.data.datasource.NoteEntityDatasource;
import by.grodno.vasili.data.entity.NoteEntity;
import by.grodno.vasili.data.entity.mapper.NoteEntityDataMapper;
import by.grodno.vasili.domain.model.Note;
import by.grodno.vasili.domain.repository.NoteRepository;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * {@link NoteRepository} implementation for retrieving {@link by.grodno.vasili.data.entity.NoteEntity}
 * from datasource and convert to {@link Note} for domain layer
 */
public class NoteDataRepository implements NoteRepository {
    private final NoteEntityDatasource datasource;
    private final NoteEntityDataMapper mapper;

    public NoteDataRepository(NoteEntityDatasource datasource, NoteEntityDataMapper mapper) {
        this.datasource = datasource;
        this.mapper = mapper;
    }

    @Override
    public Maybe<Note> getOne(String id) {
        return datasource.one(id).map(mapper::map);
    }

    @Override
    public Single<List<Note>> getAll() {
        Single<List<NoteEntity>> noteEntities = datasource.all();
        return noteEntities.map(mapper::mapList);
    }

    @Override
    public Completable delete(String id) {
        return datasource.delete(id);
    }

    @Override
    public Single<String> insert(Note note) {
        return datasource.insert(mapper.reverseMap(note));
    }

    @Override
    public Single<String> update(Note note) {
        return datasource.update(mapper.reverseMap(note));
    }
}
