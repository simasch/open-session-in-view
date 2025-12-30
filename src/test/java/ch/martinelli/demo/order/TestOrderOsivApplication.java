package ch.martinelli.demo.order;

import org.springframework.boot.SpringApplication;

public class TestOrderOsivApplication {

    public static void main(String[] args) {
        SpringApplication.from(OrderOsivApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
