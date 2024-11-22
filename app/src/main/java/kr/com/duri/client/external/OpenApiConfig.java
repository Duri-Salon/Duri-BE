package kr.com.duri.client.external;

import io.swaggy.swagger.customlib.config.SwaggyConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(SwaggyConfig.class) // SwaggyConfig를 추가
public class OpenApiConfig {
    // OpenAPI와 관련된 추가 설정을 여기에 작성
}