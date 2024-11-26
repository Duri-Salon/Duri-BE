package kr.com.duri.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ReviewController {
    @ResponseBody
    @GetMapping("review")
    public String review() {
        return "review";
    }
}
