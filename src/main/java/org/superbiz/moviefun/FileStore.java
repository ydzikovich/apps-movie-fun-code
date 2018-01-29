package org.superbiz.moviefun;

import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

import static org.springframework.util.MimeTypeUtils.IMAGE_JPEG_VALUE;

public class FileStore implements BlobStore {

    @Override
    public void put(Blob blob) throws IOException {
        File targetFile = new File(blob.name);
        targetFile.delete();
        targetFile.getParentFile().mkdirs();
        targetFile.createNewFile();

        try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
            outputStream.write(StreamUtils.copyToByteArray(blob.inputStream));
        } catch (IOException e) {
            System.out.println("Couldn't write to the file");
        }
    }

    @Override
    public Optional<Blob> get(String fileName) throws IOException {
        File file = getCoverFile(fileName);
        if(file.exists()) {
            FileInputStream inputStream = new FileInputStream(file);
            String contentType = Files.probeContentType(file.toPath());
            contentType = contentType == null ? IMAGE_JPEG_VALUE : contentType;
            Blob blob = new Blob(fileName, inputStream, contentType);
            return Optional.of(blob);
        } else
            return Optional.empty();
    }

    @Override
    public void deleteAll() {
        // ...
    }

    private File getCoverFile(String coverFileName) {
        return new File(coverFileName);
    }
}