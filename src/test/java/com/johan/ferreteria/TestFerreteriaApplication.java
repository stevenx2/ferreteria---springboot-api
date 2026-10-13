package com.johan.ferreteria;

import org.springframework.boot.SpringApplication;

public class TestFerreteriaApplication {

	public static void main(String[] args) {
		SpringApplication.from(FerreteriaApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
