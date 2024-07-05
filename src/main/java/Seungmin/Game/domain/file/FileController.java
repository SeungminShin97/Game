package Seungmin.Game.domain.file;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Controller
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("file/post/{postId}/{uuid}/{fileName}")
    @ResponseBody
    public ResponseEntity<byte[]> getPostFile(@PathVariable("postId") final Long postId,
                                                @PathVariable("uuid") final String uuid,
                                                @PathVariable("fileName") final String fileName) {
        Path path = fileService.getPostFilePath("/" + postId.toString() + "/" + uuid + "_" + fileName);
        File file = path.toFile();

        if(!file.exists())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        try {
            byte[] data = FileCopyUtils.copyToByteArray(file);
            HttpHeaders header = new HttpHeaders();
            header.add("Content-type", Files.probeContentType(file.toPath()));
            return new ResponseEntity<>(data, header, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
