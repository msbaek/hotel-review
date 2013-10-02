package net.daum.trip.web.controller;

import net.daum.trip.ServiceProperties;
import net.daum.trip.domain.City;
import net.daum.trip.service.CityRepository;
import net.daum.trip.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Autowired
    private CityRepository cityRepoistory;

    @RequestMapping("/")
    @ResponseBody
    public Page<City> helloWorld() {
        Pageable pageable = new Pageable() {
            @Override
            public int getPageNumber() {
                return 1;
            }

            @Override
            public int getPageSize() {
                return 10;
            }

            @Override
            public int getOffset() {
                return 0;
            }

            @Override
            public Sort getSort() {
                return new Sort(Sort.Direction.ASC, "name");
            }
        };
        return cityRepoistory.findAll(pageable);
    }
}
