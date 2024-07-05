package Seungmin.Game.domain.file.fileDto;

import Seungmin.Game.domain.post.postDto.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column
    private String originalFileName;

    @Column
    private String uuid;

    @Column
    private String path;

    @Column
    private Long size;

    @Column
    private String extension;


    public FileResponse toDto(Long postId) {
        return FileResponse.builder()
                .id(id)
                .postId(postId)
                .originalFileName(originalFileName)
                .uuid(uuid)
                .path(path)
                .size(size)
                .extension(extension).build();
    }

}
