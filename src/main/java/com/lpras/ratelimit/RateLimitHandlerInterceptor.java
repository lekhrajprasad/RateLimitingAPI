package com.lpras.ratelimit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;

@Component
public class RateLimitHandlerInterceptor implements HandlerInterceptor {
	static final Logger LOG = LoggerFactory.getLogger(RateLimitHandlerInterceptor.class);
	private static final String HEADER_API_KEY = "X-api-key";
	private static final String HEADER_LIMIT_REMAINING = "X-Rate-Limit-Remaining";
	private static final String HEADER_RETRY_AFTER = "X-Rate-Limit-Retry-After-Seconds";

	@Autowired
	private PricingPlanService pricingPlanService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String apiKey = request.getHeader(HEADER_API_KEY);
		LOG.info("API key: {}", apiKey);
		if (apiKey == null || apiKey.isEmpty()) {
			response.sendError(HttpStatus.BAD_REQUEST.value(), "Missing Header: " + HEADER_API_KEY);
			return false;
		}

		Bucket tokenBucket = pricingPlanService.resolveBucket(apiKey);

		ConsumptionProbe probe = tokenBucket.tryConsumeAndReturnRemaining(1);
		LOG.info("Consumption Prob: {}",probe);
		if (probe.isConsumed()) {
			response.addHeader(HEADER_LIMIT_REMAINING, String.valueOf(probe.getRemainingTokens()));
			return true;

		} else {

			long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;

			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.addHeader(HEADER_RETRY_AFTER, String.valueOf(waitForRefill));
			response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(), "You have exhausted your API Request Quota"); // 429

			return false;
		}
	}
}