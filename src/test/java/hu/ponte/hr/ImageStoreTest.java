package hu.ponte.hr;

import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest()
public class ImageStoreTest {

    @Value("${storage.upload.path}")
    private String STORAGE_PATH;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void uploadTest() throws Exception {
        FileInputStream in = new FileInputStream("./src/test/resources/images/cat.jpg");
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file",
                "image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                in
        );
        mockMvc.perform(multipart("/api/file/post")
                        .file(mockMultipartFile))
                .andExpect(status().isOk());

        Path filePath = Paths.get(STORAGE_PATH, "1_" + mockMultipartFile.getOriginalFilename());
        assertTrue(Files.exists(filePath));

        Files.delete(filePath);
    }
}
