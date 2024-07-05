package Seungmin.Game.domain.file.fileDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileResponse {
    private Long id;
    private Long postId;
    private String originalFileName;
    private String uuid;
    private String path;
    private Long size;
    private String extension;
}
