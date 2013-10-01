# Trip

spring-boot를 이용한 REST App 개발

## 1. 환경 설정
* build.gradle 파일 작성
* directory 생성
* Controller, Application 작성
* Run the application
* ```$ ./gradle build && java -jar build/libs/trip-0.1.0.jar```

## 2. 모니터링 기능 추가
* build.gradle에 ```compile("org.springframework.boot:spring-boot-starter-actuator:0.5.0.M4")``` 의존성 추가
* 아래 url과 같은 기능이 추가됨

```
	/env
	/health
	/beans
	/info
	/metrics
	/trace
	/dump
	/shutdown
```

# 문서 요약
## 1. Spring Boot
[원문](http://projects.spring.io/spring-boot/docs/spring-boot/README.html)

### 1.1 SpringApplication

SpringApplication은 main() 메소드를 통해 spring application을 편리하게 기동할 수 있게 한다. 대부분의 경우 SpringApplication.run 메소드로 위임만 하면 된다.

```
public static void main(String[] args) {
    SpringApplication.run(MySpringConfiguration.class, args);
}
```

### 1.2 Customizing SpringApplication

아래와 같이 설정하여 배너를 끌 수도 있다.

```
public static void main(String[] args) {
    SpringApplication app = new SpringApplication(MySpringConfiguration.class);
    app.setShowBanner(false);
    app.run(args);
}
```

### 1.3 Accessing command line properties

디폴트로 SpringApplication은 커맨드 라인 옵션 아규먼트(e. --server.port=9000)를 PropertySource로 변환하여 spring Environment에 최고 우선순위를 부여하여 추가한다.
Environment(System Property와 OS 환경 변수도 포함)의 Property들은 ```@Value``` 태그를 통해 spring 컴포넌트에 주입된다.

```
import org.springframework.stereotype.*
import org.springframework.beans.factory.annotation.*

@Component
public class MyBean {
    @Value("${name}")
    private String name; 
    // Running 'java -jar myapp.jar --name=Spring' will set this to "Spring"
}
```

### 1.4 CommandLineRunner beans

아래와 같은 행위를 수행하길 원한다면 CommandLineRunner 인터페이스를 구현하면 된다.

	- raw command line argument에 접근
	- SpringApplication이 실행된 후 어떤 코드를 수행
	
```
import org.springframework.boot.*
import org.springframework.stereotype.*

@Component
public class MyBean implements CommandLineRunner {

    public void run(String... args) {
        // Do something...
    }

}
```

둘 이상의 CommandLineRunner 구현체가 특정 순서로 호출되도록 하기 위해서는 ```org.springframework.core.Ordered interface```를 구현하거나 ```org.springframework.core.annotation.Order``` 어노테이션을 사용하면 된다.

### 1.5 Application Exit

spring의 lifecycle callback(```DisposableBean```이나 ```@PreDestroy```)을 이용해서 SpringApplication에 shutdown hook을 등록할 수 있다.
또 어플리케이션 종료시 특정 종료 코드를 반환하기 위해 ```org.springframework.boot.ExitCodeGenerator``` 인터페이스를 구현할 수도 있다.

### 1.6 Externalized Configuration

SpringApplication은 클래스 패스의 루트에서 ```application.properties```를 읽어서 spring Environment에 추가한다.

```application.properties```를 찾는 순서는 아래와 같다.

1. classpath root
2. current directory
3. classpath /config package
4. /config subdir of the current directory

profile에 종속적인 설정 파일도 사용 가능: ```application-{profile}.properties```

application.properties의 이름 변경 가능: ```$ java -jar myproject.jar --spring.config.name=myproject```

### 1.7 Setting the Default Spring Profile

profile을 사용하여 `@Profile`로 표시된 `@Component`만 로딩되도록 할 수 있다.

`spring.profiles.active` Environment를 사용. 이 환경 변수는 

1. application.properties에 설정 - `spring.profiles.active=dev,hsqldb` - 할 수도 있고
2. command line에 설정 - `--spring.profiles.active=dev,hsqldb` - 할 수도 있다.

### 1.8 Application Context Initializers

spring에서 ApplicationContextInitializer 인터페이스를 구현함으로써 ApplicationContext가 사용되기 전에 커스터마이즈할 수 있다. SpringApplication와 함께 ApplicationContextInitializer를 사용하려면 addInitializers 메소드를 사용하면 된다.

Environment property(`context.initializer.classes`)에 컴마로 분리된 클래스 이름 목록을 제공하거나 SpringFactoriesLoader 메커니즘을 사용하여 initializer들을 지정할 수 있다.

### 1.9 Embedded Servlet Container Support

ApplicationContext의 새로운 타입인 EmbeddedWebApplicationContext은 등록된 EmbeddedServletContainerFactory를 검색하여 컨테이너를 구동하는 WebApplicationContext이다. spring boot에는 TomcatEmbeddedServletContainerFactory와 JettyEmbeddedServletContainerFactory가 있다.
이를 통해 모든 표준적인 spring 개념들을 사용할 수 있다.

```
@Configuration
public class MyConfiguration {

    @Value("${tomcatport:8080}")
    private int port; 

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        return new TomcatEmbeddedServletContainerFactory(this.port);
    }

}
```

### 1.10 Customizing Servlet Containers

AbstractEmbeddedServletContainerFactory는 톰캣, 제티용 Srvlet Container 팩토리의 공통 부모 클래스이다. 이를 통해 web.xml에서 설정하던 사항들을 programmatic하게 설정할 수 있다.

```
@Bean
public EmbeddedServletContainerFactory servletContainer() {
    TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
    factory.setPort(9000);
    factory.setSessionTimeout(10, TimeUnit.MINUTES);
    factory.addErrorPages(new ErrorPage(HttpStatus.404, "/notfound.html");
    return factory;
}
```

### 1.11 Properteis 대신 YAML 사용

[SnakeYAML](http://code.google.com/p/snakeyaml/)이 클래스에 패스에 있으면 YAML 사용 가능

### 1.12 External EmbeddedServletContainerFactory configuration

EmbeddedServletContainerFactory를 설정할 수 있는 @ConfigurationProperties 클래스 ServerProperties를 제공

ServerProperties를 통해 아래와 같은 항목을 설정 가능

* server.port: defaults to 8080
* server.address: defaults to local address
* server.context_path: defaults to '/'

만일 톰캣을 사용한다면 아래와 같은 추가 설정이 가능

* The Tomcat access log pattern: server.tomcat.accessLogPattern
* The remote IP and protocol headers: server.tomcat.protocolHeader, server.tomcat.remoteIpHeader
* The Tomcat base directory: server.tomcat.basedir

### 1.13 Customizing Logging

commons-logging을 사용한다. 그리고 구현체는 변경 가능하고, java util logging, log4j, LogBack에 대한 디폴트 설정을 제공한다.

클래스 패스에 적절한 라이브러리를 포함함으로써 로깅이 활성화되고 클래스 패스 루트나 `logging.config` Environment에 정의된 패스에 설정 파일을 제공하여 커스터마이징할 수 있다.

로깅 시스템에 따라 다음과 같은 설정 파일이 로딩된다.

| Logging System | Customization |
| ------------- |-------------|
| Logback | right-aligned |
| Log4j | centered      |
| JDK | are neat      |

## 2. AutoConfigure
Spring Boot AutoConfiguration은 선언된 의존성에 기반하여 자동으로 spring 어플리케이션을 구성한다. 예를 들어 HSQLDB가 클래스 패스에 존재하고 수작업으로 아무런 DB Connection Bean을 설정하지 않았다면 AutoConfigure는 자동으로 in-memory database를 설정한다.

### 2.1 Enabling auto-configuration

메인 `@Configration` 클래스에 `@EnableAutoConfiguration` 어노테이션을 지정함으로써 auto-configuration을 활성화한다.

```
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.*;

@Configuration
@EnableAutoConfiguration
public class MyConfiguration {
}
```

### 2.2 Understanding auto-configured beans

일반적으로 auto-configuration 클래스는 `@ConditionalOnClass`과 `@ConditionalOnMissingBean` 어노테이션을 사용한다. 이를 통해 적절한 클래스가 발견되었을 때만 혹은 `@Configuration`을 선언하지 않았을 때만 auto-configuration이 적용되도록 할 수 있다.

### 2.3 Disabling specific auto-configuration

exclude 속성을 통해 특정 auto-configure 클래스가 적용되지 않도록 할 수 있다.

```
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.autoconfigure.jdbc.*;
import org.springframework.context.annotation.*;

@Configuration
@EnableAutoConfiguration(exclude={EmbeddedDatabaseConfiguration.class})
public class MyConfiguration {
}
```

### 2.4 Condition annotations

| Conditions | Anotations |
| - | - |
| Class conditions | @ConditionalOnClass, @ConditionalOnMissingClass |
| Bean conditions | @ConditionalOnBean, @ConditionalOnMissingBean |
| Resource conditions | @ConditionalOnResource |
| Web Application Conditions |  @ConditionalOnWebApplication, @ConditionalOnNotWebApplication |
| SpEL expression conditions | @ConditionalOnExpression |

## 3. Actuator

| Feature | Implementation | Notes |
| - | - | - |
| Server | Tomcat or Jetty | Whatever is on the classpath |
| REST | Spring MVC |  |
| Security | Spring Security | If on the classpath |
| Logging | Logback, Log4j or JDK | Whatever is on the classpath. Sensible defaults. |
| Database | HSQLDB or H2 | Per classpath, or define a DataSource to override |
| Externalized configuration | Properties or YAML | Support for Spring profiles. Bind automatically to @Bean. |
| Audit | Spring Security and Spring ApplicationEvent | Flexible abstraction with sensible defaults for security events |
| Validation | JSR-303 | If on the classpath |
| Management endpoints | Spring MVC | Health, basic metrics, request tracing, shutdown, thread dumps |
| Error pages | Spring MVC | Sensible defaults based on exception and status code |
| JSON | Jackson 2 |  |
| ORM | Spring Data JPA | If on the classpath |
| Batch | Spring Batch | If enabled and on the classpath |
| Integration Patterns | Spring Integration | If on the classpath |

### 3.1 A Basic Proejct

아래에 2개의 기본 의존성이 존재

```
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
```

이 의존성은 auto-configure, tomcat container를 프로젝트에 추가한다.

```
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
```

이 의존성은 default management endpoints 등과 같은 보다 옵션화된 기능을 프로젝트에 추가한다.

### 3.2 Externalizing configuration

`compile("org.yaml:snakeyaml:1.13")`을 build.gradle 파일에 추가해서 yaml을 사용하도록 설정

`src/main/resources/application.yml` 파일에 아래와 같이 설정함으로써 외부 설정을 사용할 수 있다.

```
server:
  port: 9000
management:
  port: 9001
logging:
  file: target/log.out
```

물론 snakeyaml을 클래스 패스에 추가하지 않고 properties를 사용할 수도 있다.

```
// ServiceProperties.java
@ConfigurationProperties(name="service")
public class ServiceProperties {
    private String message;
    private int value = 0;
    ... getters and setters
}

// SampleController.java
@Controller
@EnableAutoConfiguration
@EnableConfigurationProperties(ServiceProperties.class)
public class SampleController {

  @Autowired
  private ServiceProperties properties;

  @RequestMapping("/")
  @ResponseBody
  public Map<String, String> helloWorld() {
    return Collections.singletonMap("message", properties.getMessage());
  }

  ...
}
```

`@EnableConfigurationProperties`에서 `@EnableConfigurationProperties(ServiceProperties.class)` 처럼 빈을 지정할 수 있다.

### 3.3 Adding security

`compile("org.springframework.boot:spring-boot-starter-security:0.5.0.M4")` 의존성 추가

```
$ curl localhost:8080/
{"status": 403, "error": "Forbidden", "message": "Access Denied"}
$ curl user:password@localhost:8080/
{"message": "Hello World"}
```

위와 같이 id/pwd가 필요하게 된다.

기본적으로 auto configuration은 in-memory user DB에 하나의 엔트리를 갖는다. 확장하길 원한다면 `AuthenticationManager`에 대한 `@Bean`정의를 제공해야 한다.

### 3.4 Adding a database

아래와 같이 2개의 라이브러리를 추가

```
compile("org.springframework:spring-jdbc:3.2.4.RELEASE")
compile("org.hsqldb:hsqldb:2.3.0")
```

그리고 Controller에 아래와 같이 구현해보자.

```
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
```

그리고 실행하면 Internal Server Error가 발생한다. MESSAGES 테이블이 존재하지 않기 때문이다.

어플리케이션 구동시 데이터를 로딩하기 위해 클래스 패스 루트에 schema.sql 파일을 추가한다.