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
