package com.lpras.ratelimit;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;

public enum PricingPlan {

	FREE(5),
	BASIC(10),
	PROFESSIONAL(20),
	INTERNAL(50);

	private int bucketCapacity;

	/*
	 * @Value(value = "free") static int free;
	 */
	
	private PricingPlan(int bucketCapacity) {
		this.bucketCapacity = bucketCapacity;
	}

	Bandwidth getLimit() {
		Refill refill = Refill.intervally(bucketCapacity, Duration.ofMinutes(1));
		return Bandwidth.classic(bucketCapacity, refill);
	}

	public int bucketCapacity() {
		return bucketCapacity;
	}

	static PricingPlan resolvePlanFromApiKey(String apiKey) {
		if (apiKey == null || apiKey.isEmpty()) {
			return FREE;
		} else if (apiKey.startsWith("PX001-")) {
			return PROFESSIONAL;
		} else if (apiKey.startsWith("BX001-")) {
			return BASIC;
		}else if (apiKey.startsWith("IX001-")) {
			return INTERNAL;
		}
		return FREE;
	}
}