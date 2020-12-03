package study.datajpa.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController//json 형태로 객체 반환
public class HelloController {
    @RequestMapping("/hello") //hello 경로에 다음 매소드를 처리시킨다
    public String hello() {
        return "hello";
    }
}
