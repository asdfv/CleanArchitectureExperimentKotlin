package by.grodno.vasili.presentation.model;

/**
 * Model for present Note
 */
public class NoteItem {
    public String id;
    public String title;
    public String description;
    public long created;

    public NoteItem(String id, String title, String description, long created) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.created = created;
    }
}
