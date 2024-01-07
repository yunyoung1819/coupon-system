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

		int threadCount = 1000;

		ExecutorService executorService = Executors.newFixedThreadPool(32);

		CountDownLatch latch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			long userId = i;
			executorService.submit(() -> {
				try {
					applyService.apply(userId);
				} finally {
					latch.countDown();
				}
			});
		}
		latch.await();

		// 기대 결과는 100개의 쿠폰 발급이 되는 것이지만 Race condition이 발생힉기 때믄에 테스트 코드는 실패
		// Race Condition은 두개 이상의 쓰레드가 공유 데이터를 엑세스하고 동시에 작업하려고 할 때 발생하는 문제
		long count = couponRepository.count();

		assertThat(count).isEqualTo(100);
	}
}