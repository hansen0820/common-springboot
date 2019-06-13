package com.example.demo.controller;

import com.example.demo.bean.People;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class HelloController {

    @Value("${common.name}")
    private String name;

    @Value("${common.age}")
    private String age;

    @Value("${common.content}")
    private String content;

    @Autowired
    private People people;

    @RequestMapping("/hello")
    public String hello() {
        return "hello, springboot 11" + name + "," + age + "," + content + ",people.getAge=" + people.getAge();
    }
}
