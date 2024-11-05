package com.example.api.service;

import com.example.api.domain.Coupon;
import com.example.api.producer.CouponCreateProducer;
import com.example.api.repository.CouponCountRepository;
import com.example.api.repository.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class ApplyService {

	private final CouponRepository couponRepository;

	private final CouponCountRepository couponCountRepository;

	private final CouponCreateProducer couponCreateProducer;

	public ApplyService(CouponRepository couponRepository, CouponCountRepository couponCountRepository, CouponCreateProducer couponCreateProducer) {
		this.couponRepository = couponRepository;
		this.couponCountRepository = couponCountRepository;
		this.couponCreateProducer = couponCreateProducer;
	}

	/**
	 * 쿠폰(Coupon) 발급
	 */
//	public void apply(Long userId) {
//		// 쿠폰의 개수를 조회
//		long count = couponRepository.count();  // race condition 발생
//
//		// 발급 가능한 개수가 초과한 경우 발급하지 않음
//		if (count > 100) {
//			return;
//		}
//
//		// 쿠폰 생성
//		couponRepository.save(new Coupon(userId));
//	}

	/**
	 * redis incr 활용
	 */
	public void apply(Long userId) {
		// synchronized는 서버가 여러 대가 된다면 race condition이 다시 발생
		// redis incr key:value를 1씩 증가
		// redis는 싱글 스레드로 동작하여 race condiation 문제 해결 가능
		Long count = couponCountRepository.increment();

		if (count > 100) {
			return;
		}

		couponRepository.save(new Coupon(userId));
	}

	/**
	 * Kafka 활용
	 */
//	public void apply(Long userId) {
//		Long count = couponCountRepository.increment();
//
//		if (count > 100) {
//			return;
//		}

//		couponCreateProducer.create(userId);
//	}

}
