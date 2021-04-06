package com.z.nativejpablocking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.nativex.hint.TypeHint;
import springfox.documentation.spi.schema.ModelBuilderPlugin;

@TypeHint(types = ModelBuilderPlugin.class)
@SpringBootApplication
public class NativeJpaBlockingApplication {

	public static void main(String[] args) {
		SpringApplication.run(NativeJpaBlockingApplication.class, args);
	}

}
