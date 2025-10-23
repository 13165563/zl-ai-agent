package com.zluolan.zaiagent.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AI旅游规划大师 API")
                        .version("1.0")
                        .description("AI旅游规划大师接口文档 - 专业的旅游规划助手"));
    }
}
