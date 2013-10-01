package net.daum.trip.web.controller;

import net.daum.trip.ServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sql.DataSource;
import java.util.Map;

@Controller
@EnableConfigurationProperties(ServiceProperties.class)
public class HelloController {
    @Autowired
    private ServiceProperties properties;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public HelloController(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @RequestMapping("/")
    public
    @ResponseBody
    Map<String, Object> index() {
        return jdbcTemplate.queryForMap("SELECT * FROM MESSAGES WHERE ID=?", 0);
    }
}
