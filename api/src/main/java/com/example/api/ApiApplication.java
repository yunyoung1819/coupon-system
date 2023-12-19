package com.example.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 요구사항 정의
 * 선착순 100명에게 할인쿠폰을 제공하는 이벤트를 진행하고자 한다.
 *
 * 이 이벤트는 아래와 같은 조건을 만족하여야 한다.
 * 1. 선착순 100명에게만 지급되어야 한다.
 * 2. 101개 이상이 지급되면 안된다.
 * 3. 순간적으로 몰리는 트래픽을 버틸 수 있어야 한다.
 */
@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

}
