package by.grodno.vasili.data.entity;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

import timber.log.Timber;

/**
 * The entity represent Note model in data layer
 */
public final class NoteEntity {
    private static final String TIMESTAMP_KEY = "timestamp";
    private static final long DEFAULT_LONG_TIMESTAMP = 0;

    @Exclude
    public String id;
    public String title;
    public String description;
    private HashMap<String, Object> timestampCreatedMap;

    @SuppressWarnings("unused")
    public NoteEntity() {
    }

    public NoteEntity(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.timestampCreatedMap = new HashMap<String, Object>() {{
            put(TIMESTAMP_KEY, ServerValue.TIMESTAMP);
        }};
    }

    @SuppressWarnings("unused")
    public HashMap<String, Object> getTimestampCreatedMap() {
        return timestampCreatedMap;
    }

    /**
     * Get timestamp from populated by Firebase timestampCreatedMap
     */
    @Exclude
    public long getCreatedTimestamp() {
        if (timestampCreatedMap != null) {
            Object timeStampObject = timestampCreatedMap.get(TIMESTAMP_KEY);
            if (timeStampObject != null) {
                try {
                    return (long) timeStampObject;
                } catch (ClassCastException e) {
                    Timber.w(e, "Incorrect format for created date");
                }
            }
        }
        Timber.w("Missing created date");
        return DEFAULT_LONG_TIMESTAMP;
    }

    @Override
    public String toString() {
        return "NoteEntity{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
