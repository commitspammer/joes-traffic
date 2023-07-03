package com.livetraffic.api.service;

import java.util.function.Consumer;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

@Service
public class SensorService {

	@Autowired
	private WebClient orionClient;

	private AtomicReference<String> globalSubscriptionId = new AtomicReference<>("");
	private SensorSink sensorSink = new SensorSink();
	private Flux<String> sensorStream = Flux.create(sensorSink).share();

	public Flux<String> getAll() {
		return orionClient.get()
			.uri("/entities?type=TrafficSensor")
			.retrieve()
			.bodyToFlux(String.class);
	}

	public Mono<Void> create(String sensor) {
		return orionClient.post()
			.uri("/entities")
			.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.body(Mono.just(sensor), String.class)
			.retrieve()
			.toBodilessEntity()
			.then(Mono.empty());
	}

	public Mono<Void> update(String id, String attrs) {
		return orionClient.post()
			.uri("/entities/" + id + "/attrs")
			.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.body(Mono.just(attrs), String.class)
			.retrieve()
			.toBodilessEntity()
			.then(Mono.empty());
	}

	public Flux<String> subscribeAll() {
		String id = globalSubscriptionId.get();
		if (id == "" || id == null) {
			orionClient.post()
				.uri("/subscriptions")
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.body(Mono.just(this.subscriptionStr()), String.class)
				.retrieve()
				.toBodilessEntity()
				.subscribe();
			globalSubscriptionId.set("asdf");
		}
		return sensorStream;
	}

	public Mono<Void> publishOne(String sensor) {
		sensorSink.produce(sensor);
		System.out.println(sensor);
		return Mono.empty();
	}

	private class SensorSink implements Consumer<FluxSink<String>> {
		private FluxSink<String> sink;
		@Override
		public void accept(FluxSink<String> sink) {
			this.sink = sink;
		}
		public void produce(String sensor) {
			this.sink.next(sensor);
		}
	}

	@PostConstruct
	private void initSampleSensors() {
		create(genSensor("ayrton_senna_1","busy",-5.856663,-35.205747)).subscribe();
		create(genSensor("roberto_freire_1","moderate",-5.863720,-35.188711)).subscribe();
		create(genSensor("salgado_filho_1","moderate",-5.831813,-35.212114)).subscribe();
		create(genSensor("salgado_filho_2","busy",-5.8446177955954415,-35.20879802146579)).subscribe();
		create(genSensor("mauro_negocio_1","free",-5.795700921513827,-35.22335530301774)).subscribe();
		create(genSensor("jose_bezarra_1","free",-5.804186678661092,-35.20936003253733)).subscribe();
		create(genSensor("jaguarari_1","moderate",-5.834330924322511,-35.22414804351795)).subscribe();
	}

	private String genSensor(String id, String flow, Double x, Double y) {
		return "{\"id\":\""+id+"\",\"type\":\"TrafficSensor\",\"flow\":{\"type\":\"Text\",\"value\":\""+flow+"\"},\"location\":{\"type\":\"geo:json\",\"value\":{\"type\":\"Point\",\"coordinates\":["+x+","+y+"]}}}";
	}

	private String subscriptionStr() {
		return "{\"description\":\"Global TrafficSensor subscription\",\"subject\":{\"entities\":[{\"idPattern\":\".*\",\"type\":\"TrafficSensor\"}],\"condition\":{\"attrs\":[\"flow\",\"location\"]}},\"notification\":{\"http\":{\"url\":\"http://api:8080/sensors/events\"},\"attrs\":[]},\"throttling\":1}";
	}

}
