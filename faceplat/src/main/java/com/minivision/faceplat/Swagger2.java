package com.minivision.faceplat;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.minivision.faceplat.rest.result.RestResult;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.ImplicitGrant;
import springfox.documentation.service.LoginEndpoint;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2 {

	@Bean
	public Docket createRestApi() {
		/*List<ResponseMessage> responses = Arrays.asList(
				new ResponseMessageBuilder().code(500).message("服务内部错误").build(),
				new ResponseMessageBuilder().code(403).message("禁止访问(Forbidden)").build(),
				new ResponseMessageBuilder().code(400).message("请求参数错误(Bad Request)").build(),
				new ResponseMessageBuilder().code(401).message("未授权(Unauthorized)").build(),
				new ResponseMessageBuilder().code(405).message("参数格式错误(invalid input)").build());*/
		
		return new Docket(DocumentationType.SWAGGER_2)
		    .genericModelSubstitutes(RestResult.class)
		    .apiInfo(apiInfo())
		    .useDefaultResponseMessages(false)
		    .forCodeGeneration(true)
			//.globalResponseMessage(RequestMethod.GET, responses)
			//.globalResponseMessage(RequestMethod.POST, responses)
			.select()
			.apis(RequestHandlerSelectors.basePackage("com.minivision.faceplat"))
			.paths(PathSelectors.any())
			.build()
			.securitySchemes(Arrays.asList(oauth()))
			.securityContexts(securityContext());
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("小视人脸服务平台").description("小视人脸服务 RESTful APIs")
				// .termsOfServiceUrl("http:///")
				.contact(new Contact("PanXinmiao", null, "panxinmiao@minivision.cn")).version("1.0").build();
	}
	
    private SecurityScheme oauth() {
      return new OAuthBuilder().name("oauth2").grantTypes(grantTypes()).scopes(scopes()).build();
    }
  
  
    private List<AuthorizationScope> scopes() {
      return Arrays.asList(new AuthorizationScope("face", "人脸检测相关服务"), new AuthorizationScope("faceset", "人脸库相关服务"));
    }
  
    private List<GrantType> grantTypes() {
      //List<GrantType> grants = Arrays.asList(new AuthorizationCodeGrant(new TokenRequestEndpoint("/oauth/authorize", "foo", "bar"), new TokenEndpoint("/oauth/access_token", "access_token")));
      //List<GrantType> grants = Arrays.asList(new ResourceOwnerPasswordCredentialsGrant("/oauth/token"));
      LoginEndpoint le = new LoginEndpoint("/oauth/authorize");
      List<GrantType> grants = Arrays.asList(new ImplicitGrant(le, "faceplat"));
      return grants;
    }
    
    
    private SecurityContext makeSecurityContext(String path, AuthorizationScope... scopes) {
        SecurityReference securityReference = SecurityReference
          .builder()
          .reference("oauth2")
          .scopes(scopes)
          .build();

        return SecurityContext
          .builder()
          .securityReferences(Arrays.asList(securityReference))
          .forPaths(PathSelectors.ant(path))
          .build();
    }
    
    
    private List<SecurityContext> securityContext() {
      SecurityContext faceContext = makeSecurityContext("/api/v1/*", new AuthorizationScope("face", "人脸检测相关服务"));
      SecurityContext faceSetContext = makeSecurityContext("/api/v1/faceset/*", new AuthorizationScope("faceset", "人脸库相关服务"));
      return Arrays.asList(faceContext, faceSetContext);
    }

}