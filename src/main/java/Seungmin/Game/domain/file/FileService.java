package Seungmin.Game.domain.file;

import Seungmin.Game.common.exceptions.CustomException;
import Seungmin.Game.common.exceptions.CustomExceptionCode;
import Seungmin.Game.domain.file.fileDto.File;
import Seungmin.Game.domain.file.fileDto.FileResponse;
import Seungmin.Game.domain.post.postDto.Post;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FileService {

    private final FileRepository fileRepository;
    private final FileStorageService fileStorageService;

    @Transactional
    public void savePostFile(final Post post, final ArrayList<MultipartFile> files) throws FileSizeLimitExceededException {
        try {
            if(files.isEmpty()) return;

            Long postId = post.getId();

            Path postDirectory = fileStorageService.createFolderReturnPath("post/" + postId.toString());

            List<File> fileList = new ArrayList<>();
            for(MultipartFile multipartFile : files) {
                if(multipartFile.getSize() > 5 * 1024 * 1024) throw new FileSizeLimitExceededException("파일 최대 용량은 5MB입니다.", multipartFile.getSize(), 5 * 1024 * 1024);

                String uuid = UUID.randomUUID().toString();
                String fileName = uuid + "_" + multipartFile.getOriginalFilename();
                Path targetLocation = postDirectory.resolve(fileName);

                File file = File.builder()
                        .post(post)
                        .originalFileName(multipartFile.getOriginalFilename())
                        .uuid(uuid)
                        .path(targetLocation.toString())
                        .size(multipartFile.getSize())
                        .extension(multipartFile.getContentType()).build();
                fileList.add(file);

                Files.copy(multipartFile.getInputStream(), targetLocation);
            }
            fileRepository.saveAll(fileList);
        } catch (Exception e) {
            throw new RuntimeException("파일 저장 중 오류 발생 : " + e.getMessage(), e);
        }
    }

    @Transactional
    public void updatePostFile(final Post post, final ArrayList<MultipartFile> files) throws FileSizeLimitExceededException {
        if(files.isEmpty()) return;

        try {
            Long postId = post.getId();
            Path postPath = fileStorageService.createPath("post/" + postId.toString());
            fileStorageService.cleanFolder(postPath);
            fileRepository.deleteAllByPostId(postId);

            savePostFile(post, files);
        } catch (FileSizeLimitExceededException e) {
            throw new FileSizeLimitExceededException(e.getMessage(), e.getActualSize(), e.getPermittedSize());
        } catch (Exception e) {
            throw new CustomException(CustomExceptionCode.PostUpdateFailedException);
        }

    }

    public List<FileResponse> getPostFile(final long postId) {
        try {
            return fileRepository.findByPostId(postId).stream().map(t -> t.toDto(postId)).collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException(CustomExceptionCode.FileNotFoundException);
        }
    }

    public Path getPostFilePath(String path) {
        return fileStorageService.createPath("post" + path);
    }
}
