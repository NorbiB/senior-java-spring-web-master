package hu.ponte.hr.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.ponte.hr.controller.ImageMeta;
import hu.ponte.hr.persistence.entity.Image;
import hu.ponte.hr.persistence.repository.ImageStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ImageStore {

    private final ImageStoreRepository imageStoreRepository;

    @Value("${storage.upload.path}")
    private String UPLOAD_PATH;

    @Autowired
    public ImageStore(ImageStoreRepository imageStoreRepository) {
        this.imageStoreRepository = imageStoreRepository;
    }

    public String store(MultipartFile file) {
        Image image = Image.builder()
                .name(file.getOriginalFilename())
                .mimeType(file.getContentType())
                .size(file.getSize())
                .path(UPLOAD_PATH)
                .build();
        image = imageStoreRepository.save(image);

        try {
            byte[] bytes = file.getBytes();
            Files.write(Paths.get(UPLOAD_PATH, image.getId() + "_" + file.getOriginalFilename()), bytes);
            createMetadata(image.getId(), file);
            return "ok";
        } catch (IOException e) {
            imageStoreRepository.deleteById(image.getId());
            e.printStackTrace();
            return "error";
        }
    }

    private void createMetadata(Long id, MultipartFile file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Path metaPath = Paths.get(UPLOAD_PATH, id + "_" + file.getOriginalFilename().split("\\.")[0] + ".json");
        Map<String, String> metadata = new HashMap<>();
        metadata.put("id", id.toString());
        metadata.put("name", file.getOriginalFilename());
        metadata.put("mimeType", file.getContentType());
        metadata.put("size", String.valueOf(file.getSize()));
        objectMapper.writeValue(new File(metaPath.toString()), metadata);
    }

    public List<ImageMeta> listImages() {
        return imageStoreRepository.findAll().stream().map(image -> ImageMeta.builder()
                .id(image.getId().toString())
                .name(image.getName())
                .size(image.getSize())
                .mimeType(image.getMimeType())
                .digitalSign("NotImplementedYet")
                .build()).collect(Collectors.toList());
    }

    public Optional<Image> getImage(Long id) {
        return imageStoreRepository.findById(id);
    }
}
