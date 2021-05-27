package tech.itpark.dto.wiki;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class WikiSaveRequestDto {
    private long id;
    private String authorId;
    private String person;
    private String content;
}
