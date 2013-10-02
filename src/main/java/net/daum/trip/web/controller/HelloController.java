package net.daum.trip.web.controller;

import net.daum.trip.ServiceProperties;
import net.daum.trip.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableConfigurationProperties(ServiceProperties.class)
public class HelloController {
    @Autowired
    private ServiceProperties properties;

    @Autowired
    private CityService cityService;

    @RequestMapping("/")
    @ResponseBody
    public String helloWorld() {
        return this.cityService.getCity("Bath", "UK").getName();
    }
}
