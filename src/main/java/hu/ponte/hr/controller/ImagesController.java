package hu.ponte.hr.controller;


import hu.ponte.hr.persistence.entity.Image;
import hu.ponte.hr.services.ImageStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("api/images")
public class ImagesController {

    @Autowired
    private ImageStore imageStore;

    @GetMapping("meta")
    public List<ImageMeta> listImages() {
		return imageStore.listImages();
    }

    @GetMapping("preview/{id}")
    public void getImage(@PathVariable("id") String id, HttpServletResponse response) throws IOException {
        Optional<Image> image = imageStore.getImage(Long.parseLong(id));
        if (image.isEmpty()) {
            return;
        }
        File initialFile = new File(Paths.get(image.get().getPath(), id + "_" + image.get().getName()).toString());
        InputStream in = new FileInputStream(initialFile);
        response.setContentType(image.get().getMimeType());
        StreamUtils.copy(in, response.getOutputStream());
	}
}
