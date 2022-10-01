package hu.ponte.hr.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

/**
 * @author zoltan
 */
@Configuration
public class AppConfig
{
	@Value("${storage.upload.path}")
	private String UPLOAD_PATH;

	@Bean
	public LocaleResolver localeResolver() {
		return new FixedLocaleResolver(Locale.ENGLISH);
	}

	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(100000000);
		return multipartResolver;
	}

	@Bean
	public void storageDirectoryResolver() throws IOException {
		if (!Files.exists(Path.of(UPLOAD_PATH))) {
			Files.createDirectories(Path.of(UPLOAD_PATH));
		}
	}


}
