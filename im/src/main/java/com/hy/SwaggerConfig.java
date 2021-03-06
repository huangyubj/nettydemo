package com.hy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.hy"))// 指定扫描包下面的注解
                .paths(PathSelectors.any())
                .build();
    }
    //http://localhost:8080/swagger-ui.html
    // 创建api的基本信息
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Swagger2 RESTful APIs")
                .description("Swagger2 RESTful APIs")
                .termsOfServiceUrl("http://www.baidu.com/")
                .contact(new Contact("hy","https://github.com/huangyubj/nettydemo","hy00870@163.com"))
                .version("1.0.0")
                .build();
    }
}