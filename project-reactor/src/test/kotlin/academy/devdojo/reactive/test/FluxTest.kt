package academy.devdojo.reactive.test

import org.junit.jupiter.api.Test
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import org.slf4j.LoggerFactory
import reactor.core.publisher.BaseSubscriber
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.lang.reflect.InvocationTargetException
import java.time.Duration

class FluxTest {
    val log = LoggerFactory.getLogger(MonoTest::class.java)

    @Test
    fun fluxSubscriber() {
        val fluxString = Flux.just("Vini","Neymar","Pucca","Garu")
            .log()

        StepVerifier.create(fluxString)
            .expectNext("Vini","Neymar","Pucca","Garu")
            .verifyComplete()
    }

    @Test
    fun fluxSubscriberNumbers() {
        val fluxNumber = Flux.range(1,10)
            .log()

        fluxNumber.subscribe{e -> log.info("Number: {}",e)}


        log.info("-------------------------------------")
        StepVerifier.create(fluxNumber)
            .expectNext(1,2,3,4,5,6,7,8,9,10)
            .verifyComplete()
    }

    @Test
    fun fluxSubscriberFromList() {
        val fluxNumber = Flux.fromIterable(listOf(1,2,3,4,5))
            .log()

        fluxNumber.subscribe{e -> log.info("Number: {}",e)}


        log.info("-------------------------------------")
        StepVerifier.create(fluxNumber)
            .expectNext(1,2,3,4,5)
            .verifyComplete()
    }

    @Test
    fun fluxSubscriberNumbersError() {
        val flux = Flux.range(1, 5)
            .log()
            .map { i ->
                if (i == 4) {
                    throw IndexOutOfBoundsException("index error")
                }
                i
            }
        flux.subscribe(
            { i -> log.info("Number {}", i) },
            { obj: Throwable -> obj.printStackTrace() },
            { log.info("DONE!") },
            { subscription -> subscription.request(3)}
        )

        log.info("-----------------------------------")
        StepVerifier.create(flux)
            .expectNext(1, 2, 3)
            .expectError(IndexOutOfBoundsException::class.java)
            .verify()
    }

    @Test
    fun fluxSubscriberNumbersUglyBackPressure() {
        val flux = Flux.range(1, 10)
            .log()

        flux.subscribe {
            object : Subscriber<Integer> {
                private var count = 0
                private lateinit var subscription: Subscription
                private val requestCount = 2

                override fun onSubscribe(p0: Subscription?) {
                    this.subscription = subscription
                    subscription.request(requestCount.toLong())
                }

                override fun onError(p0: Throwable?) {
                    TODO("Not yet implemented")
                }

                override fun onComplete() {
                    TODO("Not yet implemented")
                }

                override fun onNext(p0: Integer?) {
                    count++;
                    if (count >= 2) {
                        count = 0
                        subscription.request(2)
                    }
                }

            }
        }

        log.info("-----------------------------------")
        StepVerifier.create(flux)
            .expectNext(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            .verifyComplete()
    }

    @Test
    fun fluxSubscriberNumbersNotSoUglyBackPressure() {
        val flux = Flux.range(1, 10)
            .log()

        flux.subscribe {
            object : BaseSubscriber<Integer>() {
                private var count = 0
                private val requestCount = 2

                override fun hookOnSubscribe(subscription : Subscription) {
                   request(requestCount.toLong())
                }

                override fun hookOnNext(value: Integer) {
                    count++;
                    if (count >= requestCount) {
                        count = 0
                        request(requestCount.toLong())
                    }
                }


            }
        }

        log.info("-----------------------------------")
        StepVerifier.create(flux)
            .expectNext(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            .verifyComplete()
    }

    @Test
    fun fluxSubscriberPrettyBackPressure() {
        val fluxNumber = Flux.range(1,10)
            .log()
            .limitRate(3)


        fluxNumber.subscribe{e -> log.info("Number: {}",e)}


        log.info("-------------------------------------")
        StepVerifier.create(fluxNumber)
            .expectNext(1,2,3,4,5,6,7,8,9,10)
            .verifyComplete()
    }

    @Test
    fun fluxSubscriberIntervalOne() {
        val interval = Flux.interval(Duration.ofMillis(100))
            .take(10)
            .log()

        interval.subscribe{i -> log.info("Number: {}",i.toString())}

        Thread.sleep(3000)
    }

    @Test
    fun fluxSubscriberIntervalTwo() {
        StepVerifier.withVirtualTime{createInterval()}
            .expectSubscription()
            .expectNoEvent(Duration.ofHours(24))
            .thenAwait(Duration.ofDays(1))
            .expectNext(0L)
            .thenAwait(Duration.ofDays(1))
            .expectNext(1L)
            .thenCancel()
            .verify()
    }

    //ctrl + alt + m extract method no intelij
    private fun createInterval(): Flux<Long> {
        return Flux.interval(Duration.ofDays(1))
            .log()
    }
    //ctrl alt v -> cria variavel
    @Test
    fun conncetableFlux(){
        val connectableFlux = Flux.range(1, 10)
            .log()
            .delayElements(Duration.ofMillis(100))
            .publish()

//        connectableFlux.connect()

//        log.info("Thread sleeping for 300ms")
//        Thread.sleep(300)
//
//        connectableFlux.subscribe{e -> log.info(e.toString())}
//
//        log.info("Thread sleeping for 200ms")
//        Thread.sleep(200)
//
//        connectableFlux.subscribe{e -> log.info(e.toString())}

        StepVerifier
            .create(connectableFlux)
            .then(connectableFlux::connect)
            .expectNext(1,2,3,4,5,6,7,8,9,10)
            .expectComplete()
            .verify()
    }

    @Test
    fun conncetableFluxAutoConnect(){
        val connectableFlux = Flux.range(1, 10)
            .log()
            .delayElements(Duration.ofMillis(100))
            .publish()
            .autoConnect(2)

        StepVerifier
            .create(connectableFlux)
            .then(connectableFlux::subscribe)
            .expectNext(1,2,3,4,5,6,7,8,9,10)
            .expectComplete()
            .verify()
    }
}