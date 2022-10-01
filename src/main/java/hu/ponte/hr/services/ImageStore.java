package hu.ponte.hr.services;

import hu.ponte.hr.controller.ImageMeta;
import hu.ponte.hr.persistence.entity.Image;
import hu.ponte.hr.persistence.repository.ImageStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
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
            return "ok";
        } catch (IOException e) {
            e.printStackTrace();
            //It could be custom exception throwing
            return "error";
        }
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
