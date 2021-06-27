package com.lpras.ratelimit;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/area", consumes = MediaType.APPLICATION_JSON_VALUE)
class AreaCalculationController {

	@PostMapping(value = "/triangle", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AreaV1> triangle(@RequestBody TriangleDimensionsV1 dimensions) {
		return ResponseEntity.ok(new AreaV1("triangle", 0.5d * dimensions.getHeight() * dimensions.getBase()));
	}

	@PostMapping(value = "/rectangle", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AreaV1> rectangle(@RequestHeader(value = "X-api-key") String apiKey,
			@RequestBody RectangleDimensionsV1 dimensions) {
		return ResponseEntity.ok(new AreaV1("rectangle",dimensions.getLength() * dimensions.getWidth()));
	}
}