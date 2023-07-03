package com.livetraffic.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.livetraffic.api.service.SensorService;

@RestController
@RequestMapping("/sensors")
public class SensorController {

	@Autowired
	private SensorService service;

	@GetMapping
	public Flux<String> getAll() {
		return service.getAll();
	}

	@GetMapping(path = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> subscribeAll() {
		return service.subscribeAll();
	}

	@PostMapping("/events")
	public Mono<Void> publishUpdate(HttpEntity<String> httpEntity) {
		return service.publishOne(httpEntity.getBody());
	}

	@PostMapping
	public Mono<Void> register(HttpEntity<String> httpEntity) {
		return service.create(httpEntity.getBody());
	}

	@PatchMapping("/{id}")
	public Mono<Void> update(HttpEntity<String> httpEntity, @PathVariable String id) {
		return service.update(id, httpEntity.getBody());
	}

}
