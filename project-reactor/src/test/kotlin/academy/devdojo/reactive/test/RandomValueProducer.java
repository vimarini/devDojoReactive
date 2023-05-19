package academy.devdojo.reactive.test;

import reactor.core.publisher.Flux;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class RandomValueProducer {
    public Flux<Integer> generateRandomValues(int count) {
        Random random = new Random();
        AtomicInteger counter = new AtomicInteger(count);

        return Flux.generate(sink -> {
            if (counter.get() > 0 ) {
                int randomValue = random.nextInt((30-20)+1)+20;
                sink.next(randomValue);
                counter.decrementAndGet();
            } else {
                sink.complete();
            }
        });
    }
}
