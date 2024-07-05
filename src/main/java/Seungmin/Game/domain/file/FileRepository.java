package Seungmin.Game.domain.file;


import Seungmin.Game.domain.file.fileDto.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {

    List<File> findByPostId(Long postId);

    void deleteAllByPostId(Long postId);
}
