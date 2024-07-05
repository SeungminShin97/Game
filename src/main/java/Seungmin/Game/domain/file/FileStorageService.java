package Seungmin.Game.domain.file;

import Seungmin.Game.common.exceptions.CustomException;
import Seungmin.Game.common.exceptions.CustomExceptionCode;
import lombok.Getter;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Getter
@Service
public class FileStorageService {

    private final Path filePath;

    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
        this.filePath = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    public Path createPath(String path) {
        return filePath.resolve(String.valueOf(path)).toAbsolutePath().normalize();
    }


    public Path createFolderReturnPath(String folderName) throws IOException {
        Path folderPath = filePath.resolve(String.valueOf(folderName)).normalize();
        Files.createDirectories(folderPath);
        return folderPath;
    }

    public void cleanFolder(final Path path) {
        File file = new File(String.valueOf(path));
        try {
            if(file.exists())
                FileUtils.cleanDirectory(file);
        } catch (IOException e) {
            throw new CustomException(CustomExceptionCode.FileDeleteFailedException, "폴더 초기화 중 오류 : " + path.toString());
        }
    }

    public void deleteFolder(final Path path) {
        File file = new File(String.valueOf(path));
        try {
            if(file.exists()) {
                FileUtils.cleanDirectory(file);
                FileUtils.deleteDirectory(file);
            }
        } catch (IOException e) {
            throw new CustomException(CustomExceptionCode.FileDeleteFailedException, "폴더 삭제 중 오류 : " + path.toString());
        }
    }
}
