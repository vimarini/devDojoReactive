package academy.devdojo.reactive.test

import org.junit.jupiter.api.Test
import org.reactivestreams.Subscription
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class MonoTest {
    val log = LoggerFactory.getLogger(MonoTest::class.java)

    @Test
    fun monoSubscriber() {
        val name = "nico"
        val mono = Mono.just(name)
            .log()

        mono.subscribe()
        log.info("------------------")
        StepVerifier.create(mono)
            .expectNext("nico")
            .verifyComplete()

    }

    @Test
    fun monoSubscriberConsumer() {
        val name = "nico"
        val mono = Mono.just(name)
            .log()

        mono.subscribe { s -> log.info(s) }
        log.info("------------------")

        StepVerifier.create(mono)
            .expectNext("nico")
            .verifyComplete()

    }

    @Test
    fun monoSubscriberConsumerError() {
        val name = "nico"
        val mono = Mono.just(name)
            .map{s->throw RuntimeException("Error")}

        mono.subscribe({ s -> log.info(s)}
        ,{ s -> log.error("erro") })

        mono.subscribe({ s -> log.info(s)}
            ,Throwable::printStackTrace)

        log.info("------------------")

        StepVerifier.create(mono)
            .expectError(RuntimeException::class.java)
            .verify()

    }

    @Test
    fun monoSubscriberConsumerComplete() {
        val name = "nico"
        val mono = Mono.just(name)
            .log()
            .map { s -> s.uppercase()}

        mono.subscribe(
            { s -> log.info(s)},
            Throwable::printStackTrace,
            { log.info("Finished")},
            Subscription::cancel
        )

        log.info("------------------")

        StepVerifier.create(mono)
            .expectNext("nico".uppercase())
            .verifyComplete()

    }

    @Test
    fun monoDoOnMethods() {
        val name = "nico"
        val mono = Mono.just(name)
            .log()
            .map { s -> s.uppercase()}
            .doOnSubscribe {subs -> log.info("Subscribed")}
            .doOnRequest { longNumber -> log.info("Request Received, start doing something")}
            .doOnNext {s -> log.info("Value is here. Exec doOnNext {}",s)}
            .doOnSuccess { s -> log.info("Do on success")}

        mono.subscribe(
            { s -> log.info(s)},
            Throwable::printStackTrace,
            { log.info("Finished")}
        )

        log.info("------------------")

//        StepVerifier.create(mono)
//            .expectNext("nico".uppercase())
//            .verifyComplete()

    }
}