package net.daum.trip.web.controller;

import net.daum.trip.ServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.Map;

@Controller
@EnableConfigurationProperties(ServiceProperties.class)
public class HelloController {
    @Autowired
    private ServiceProperties properties;

    @RequestMapping("/")
    public
    @ResponseBody
    Map<String, String> index() {
        return Collections.singletonMap("message", properties.getMessage() + properties.getValue());
    }
}
