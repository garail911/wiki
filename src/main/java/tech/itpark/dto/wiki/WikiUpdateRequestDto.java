package tech.itpark.dto.wiki;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class WikiUpdateRequestDto {
    private long id;
    private long authorId;
    private String person;
    private String content;
}
