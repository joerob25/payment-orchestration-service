package io.vosy.payment_orchestration_service;

import org.springframework.boot.SpringApplication;

public class TestPaymentOrchestrationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(PaymentOrchestrationServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
