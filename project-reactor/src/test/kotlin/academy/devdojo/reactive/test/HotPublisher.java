package academy.devdojo.reactive.test;

import academy.devdojo.reactive.test.courseutil.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.stream.Stream;

public class HotPublisher {

    public static void main(String[] args) {
        HotPublisher hotPublisher = new HotPublisher();
        hotPublisher.hotPublisherResub();

    }

    private static Stream<String> getMovie(){
        System.out.println("Got the movie streaming req");
        return Stream.of(
                "Scene 1",
                "Scene 2",
                "Scene 3",
                "Scene 5",
                "Scene 6",
                "Scene 7"
        );
    }

    public void simpleHotPublisher(){
        Flux<String> movieStream = Flux.fromStream(() -> getMovie())
                .delayElements(Duration.ofSeconds(2))
                .publish()
                .refCount(2);//at least this of subscribers

        movieStream
                .subscribe(Util.subscriber("sam"));

        Util.sleepSeconds(5);

        movieStream
                .subscribe(Util.subscriber("mike"));

        Util.sleepSeconds(60);
    }

    public void hotPublisherResub(){
        Flux<String> movieStream = Flux.fromStream(() -> getMovie())
                .delayElements(Duration.ofSeconds(1))
                .publish()
                .refCount(1);//at least this of subscribers

        movieStream
                .subscribe(Util.subscriber("sam"));

        Util.sleepSeconds(10);

        movieStream
                .subscribe(Util.subscriber("mike"));

        Util.sleepSeconds(60);
    }

}
