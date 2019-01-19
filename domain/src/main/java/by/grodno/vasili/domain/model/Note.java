package by.grodno.vasili.domain.model;

/**
 * Represent Note model in domain layer
 */
public final class Note {
    public String id;
    public String title;
    public String description;
    public long created;

    /**
     * Constructor for convert {@link Note} received from data layer
     */
    public Note(String id, String title, String description, long created) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.created = created;
    }

    /**
     * Constructor for creating new {@link Note} from entered user data,
     * id and creation date will be populate on the data layer
     */
    public Note(String title, String description) {
        this.id = null;
        this.title = title;
        this.description = description;
        this.created = 0;
    }
}
