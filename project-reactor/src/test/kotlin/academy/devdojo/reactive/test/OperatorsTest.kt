package academy.devdojo.reactive.test

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.test.StepVerifier
import java.nio.file.Files
import java.nio.file.Path

class OperatorsTest {
    val log = LoggerFactory.getLogger(MonoTest::class.java)

    @Test
    fun subscribeOnSimple() {
        var flux = Flux.range(1, 5)
            .map { i:Int? ->
                    log.info("Map 1 - Number {} "+Thread.currentThread().name, i)
                    i
            }
            .publishOn(Schedulers.boundedElastic())
            .map { i:Int? ->
                    log.info("Map 2 - Number {} "+Thread.currentThread().name, i)
                    i
            }

        StepVerifier.create(flux)
            .expectNext(1,2,3,4,5)
            .expectComplete()
            .verify()
    }

    @Test
    fun multipleSubscribeOnSimple() {
        var flux = Flux.range(1, 5)
            .subscribeOn(Schedulers.single())
            .map { i:Int? ->
                log.info("Map 1 - Number {} "+Thread.currentThread().name, i)
                i
            }
            .subscribeOn(Schedulers.boundedElastic())
            .map { i:Int? ->
                log.info("Map 2 - Number {} "+Thread.currentThread().name, i)
                i
            }

        StepVerifier.create(flux)
            .expectNext(1,2,3,4,5)
            .expectComplete()
            .verify()
    }

    @Test
    fun multiplePublishOnSimple() {
        var flux = Flux.range(1, 5)
            .publishOn(Schedulers.single())
            .map { i:Int? ->
                log.info("Map 1 - Number {} "+Thread.currentThread().name, i)
                i
            }
            .publishOn(Schedulers.boundedElastic())
            .map { i:Int? ->
                log.info("Map 2 - Number {} "+Thread.currentThread().name, i)
                i
            }

        StepVerifier.create(flux)
            .expectNext(1,2,3,4,5)
            .expectComplete()
            .verify()
    }

    @Test
    fun publishAnSubscribeOnSimple() {
        var flux = Flux.range(1, 5)
            .publishOn(Schedulers.single())
            .map { i:Int? ->
                log.info("Map 1 - Number {} "+Thread.currentThread().name, i)
                i
            }
            .subscribeOn(Schedulers.boundedElastic())
            .map { i:Int? ->
                log.info("Map 2 - Number {} "+Thread.currentThread().name, i)
                i
            }

        StepVerifier.create(flux)
            .expectNext(1,2,3,4,5)
            .expectComplete()
            .verify()
    }

    @Test
    fun subscribeOnIO(){
        var listmono = Mono.fromCallable {
            Files.readAllLines(Path.of("text-file")) }
            .log()
            .subscribeOn(Schedulers.boundedElastic())

        listmono.subscribe{s -> log.info("{}",s)};

        StepVerifier.create(listmono)
            .expectSubscription()
            .thenConsumeWhile{ list ->
                Assertions.assertFalse(list.isEmpty())
                log.info(list.size.toString())
                true
        }
    }
}