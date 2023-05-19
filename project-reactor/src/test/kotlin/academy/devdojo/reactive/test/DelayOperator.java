package academy.devdojo.reactive.test;

import academy.devdojo.reactive.test.courseutil.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class DelayOperator {
    public static void main(String[] args) {
        Flux.range(1,100)
                .log()
                .delayElements(Duration.ofSeconds(1))
                .subscribe(Util.subscriber());

        Util.sleepSeconds(60);
    }
}
