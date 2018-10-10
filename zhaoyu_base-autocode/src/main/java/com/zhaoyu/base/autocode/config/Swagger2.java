package com.zhaoyu.base.autocode.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

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
public class Swagger2 extends WebMvcConfigurationSupport{
	
//	@Api：修饰整个类，描述Controller的作用
//	@ApiOperation：描述一个类的一个方法，或者说一个接口
//	@ApiParam：单个参数描述
//	@ApiModel：用对象来接收参数
//	@ApiProperty：用对象接收参数时，描述对象的一个字段
//	@ApiResponse：HTTP响应其中1个描述
//	@ApiResponses：HTTP响应整体描述
//	@ApiIgnore：使用该注解忽略这个API
//	@ApiError ：发生错误返回的信息
//	@ApiParamImplicitL：一个请求参数
//	@ApiParamsImplicit 多个请求参数

	
	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.select()
		        //为当前包路径
		        .apis(RequestHandlerSelectors.basePackage("com.zhaoyu.base.autocode.controller"))
		        .paths(PathSelectors.any())
		        .build();
	}
	
	private ApiInfo apiInfo(){
		return new ApiInfoBuilder()
				.title("Spring Boot 测试使用 Swagger2 构建RESTful API")
				.contact(new Contact("cookie", "http://www.baidu.com", "2118119173@qq.com"))
				.version("1.0")
				.description("用户管理")
				.build();
	}
	
	/**
     * 防止@EnableMvc把默认的静态资源路径覆盖了，手动设置的方式
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
    	registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    	registry.addResourceHandler("swagger-ui.html")
        .addResourceLocations("classpath:/META-INF/resources/");
    	registry.addResourceHandler("/webjars/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars/");
//    	super.addResourceHandlers(registry);
    }


}
