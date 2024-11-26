package kr.com.duri.common;

import kr.com.duri.common.response.CommonResponseEntity;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/example")
public class CommonController {

    @GetMapping("/success")
    public CommonResponseEntity<String> getSuccessExample() {
        String data = "성공입니다!";
        return CommonResponseEntity.success(data);
    }

    @GetMapping("/error")
    public CommonResponseEntity<Object> getErrorExample() {
        return CommonResponseEntity.error(HttpStatus.INTERNAL_SERVER_ERROR, "실패입니다!");
    }
}
