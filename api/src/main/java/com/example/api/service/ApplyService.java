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

//	public void apply(Long userId) {
//		// 쿠폰의 개수를 조회
//		long count = couponRepository.count();
//
//		// 발급 가능한 개수가 초과한 경우 발급하지 않음
//		if (count > 100) {
//			return;
//		}
//
//		// 쿠폰 생성
//		couponRepository.save(new Coupon(userId));
//	}

	public void apply(Long userId) {
//		Long count = couponCountRepository.increment();
//
//		if (count > 100) {
//			return;
//		}

		couponCreateProducer.create(userId);
	}

}
