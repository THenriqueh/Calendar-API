package syncope.calendar.calendar_api.domain.model;

import lombok.*;

@Getter
@Setter
@Builder
public class Category {

    private Long id;
    private Long userId;
    private String name;

    public void rename(String newName) {
        this.name = newName == null ? null : newName.trim();
    }
}
