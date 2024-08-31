package com.example.api.service;

import com.example.api.repository.CouponRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApplyServiceTest {

	@Autowired
	private ApplyService applyService;

	@Autowired
	private CouponRepository couponRepository;

	@Test
	public void 한번만응모() {
		applyService.apply(1L);

		long count = couponRepository.count();

		assertThat(count).isEqualTo(1);
	}

	@Test
	public void 여러명응모() throws InterruptedException {
		// 1. 1000개의 요청
		int threadCount = 1000;

		// 2. ExecutorService는 병렬 작업을 간단하게 할 수 있게 도와주는 Java API
		ExecutorService executorService = Executors.newFixedThreadPool(32);

		// 3. 다른 스레드에서 수행하는 작업을 기다리도록 도와주는 클래스
		CountDownLatch latch = new CountDownLatch(threadCount);

		// 4. for문을 이용해 1000개의 요청
		for (int i = 0; i < threadCount; i++) {
			long userId = i;
			executorService.submit(() -> {
				try {
					applyService.apply(userId);
				} finally {
					latch.countDown();;
				}
			});
		}

		latch.await();

		Thread.sleep(10000);

		// 5. 모든 요청이 완료되면 생성된 쿠폰의 개수를 확인한다.
		long count = couponRepository.count();

		// 6. 정상적으로 100개가 생성된 것을 예상한다.
		assertThat(count).isEqualTo(100);

		// 7. 100개가 생성될 것을 기대하였지만 더 많이 쿠폰이 발급되는 문제점이 있다.
		// Race Condition: 두 개 이상의 쓰레드가 공유 데이터에 엑세스하고 작업하려고 할 때 발생하는 문제
	}
}