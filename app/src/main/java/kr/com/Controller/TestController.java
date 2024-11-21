package kr.com.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedInputStream;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

  @GetMapping("/test")
  public String test() {
                String test = "test";
      return test;
  }

}
