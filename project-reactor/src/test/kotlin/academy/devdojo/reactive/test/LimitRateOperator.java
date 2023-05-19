package academy.devdojo.reactive.test;

import reactor.core.publisher.Flux;

public class LimitRateOperator {
    public static void main(String[] args) {
        Flux.range(1,1000)
                .log()
                .limitRate(100, 0)
                .subscribe(s -> System.out.println("Received: " + s));
    }
}
