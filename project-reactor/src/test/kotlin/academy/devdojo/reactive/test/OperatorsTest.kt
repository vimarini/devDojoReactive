package academy.devdojo.reactive.test

import lombok.AllArgsConstructor
import lombok.EqualsAndHashCode
import lombok.Getter
import lombok.ToString
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.test.StepVerifier
import reactor.util.function.Tuple2
import java.nio.file.Files
import java.nio.file.Path
import java.time.Duration
import java.util.concurrent.atomic.AtomicLong

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

    @Test
    fun switchIfEmptyOperator() {
        var flux = emptyFlux()
            .switchIfEmpty(Flux.just("Not Empty Anymore"))
            .log()

        StepVerifier.create(flux)
            .expectSubscription()
            .expectNext("Not Empty Anymore")
            .expectComplete()
            .verify()
    }

    @Test
    fun deferOperator(){
        var just = Mono.just(System.currentTimeMillis())
        var defer = Mono.defer{ Mono.just(System.currentTimeMillis()) }

        defer.subscribe {l -> log.info("time {}", l)}
        Thread.sleep(100)
        defer.subscribe {l -> log.info("time {}", l)}
        Thread.sleep(100)
        defer.subscribe {l -> log.info("time {}", l)}
        Thread.sleep(100)
        defer.subscribe {l -> log.info("time {}", l)}
        Thread.sleep(100)
        defer.subscribe {l -> log.info("time {}", l)}

        var atomicLong = AtomicLong()
        defer.subscribe(atomicLong::set)

        Assertions.assertTrue(atomicLong.get() > 0)
    }

    @Test
    fun concateOperator() {
        var flux1 = Flux.just("a","b")
        var flux2 = Flux.just("c","d")

        var concat = Flux.concat(flux1,flux2)
            .log()

        StepVerifier.create(concat)
            .expectSubscription()
            .expectNext("a","b","c","d")
            .expectComplete()
            .verify()
    }

    @Test
    fun concateWithOperator() {
        var flux1 = Flux.just("a","b")
        var flux2 = Flux.just("c","d")

        var concat = flux1.concatWith(flux2)

        StepVerifier.create(concat)
            .expectSubscription()
            .expectNext("a","b","c","d")
            .expectComplete()
            .verify()
    }

    @Test
    fun combineLatestOperator() {
        var flux1 = Flux.just("a","b")
        var flux2 = Flux.just("c","d")

        var combine = Flux.combineLatest(flux1,flux2) {s1,s2 -> s1.toUpperCase() + s2.toUpperCase()}
            .log()

        StepVerifier.create(combine)
            .expectSubscription()
            .expectNext("BC","BD")
            .expectComplete()
            .verify()
    }

    @Test
    fun mergeOperator() {
        val flux1 = Flux.just<String>("a", "b").delayElements(Duration.ofMillis(200))
        val flux2 = Flux.just("c", "d")
        val mergeFlux = Flux.merge(flux1, flux2)
            .delayElements(Duration.ofMillis(200))
            .log()

//        mergeFlux.subscribe(log::info);

//        Thread.sleep(1000);
        StepVerifier
            .create(mergeFlux)
            .expectSubscription()
            .expectNext("c", "d", "a", "b")
            .expectComplete()
            .verify()
    }

    @Test
    @Throws(Exception::class)
    fun mergeWithOperator() {
        val flux1 = Flux.just<String>("a", "b").delayElements(Duration.ofMillis(200))
        val flux2 = Flux.just("c", "d")
        val mergeFlux = flux1.mergeWith(flux2)
            .delayElements(Duration.ofMillis(200))
            .log()
        StepVerifier
            .create(mergeFlux)
            .expectSubscription()
            .expectNext("c", "d", "a", "b")
            .expectComplete()
            .verify()
    }

    @Test
    fun mergeSequentialOperator() {
        val flux1: Flux<String> = Flux.just("a", "b").delayElements(Duration.ofMillis(200))
        val flux2: Flux<String> = Flux.just("c", "d")
        val mergeFlux: Flux<String> = Flux.mergeSequential(flux1, flux2, flux1)
            .delayElements(Duration.ofMillis(200))
            .log()
        StepVerifier
            .create(mergeFlux)
            .expectSubscription()
            .expectNext("a", "b", "c", "d", "a", "b")
            .expectComplete()
            .verify()
    }

    @Test
    fun mergeDelayErrorOperator() {
        val flux1: Flux<String> = Flux.just("a", "b")
            .map{ s: String ->
                if ((s == "b")) {
                    throw java.lang.IllegalArgumentException()
                }
                s
            }.doOnError{ t: Throwable? -> log.error("We could do something with this") }
        val flux2: Flux<String> = Flux.just("c", "d")
        val mergeFlux: Flux<String> = Flux.mergeDelayError(2, flux1, flux2, flux1)
            .log()
        mergeFlux.subscribe { s: kotlin.String? -> log.info(s) }
        StepVerifier
            .create(mergeFlux)
            .expectSubscription()
            .expectNext("a", "c", "d", "a")
            .expectError()
            .verify()
    }

    private fun emptyFlux() : Flux<Any>{
        return Flux.empty()
    }

    @Test
    fun flatMapOperator() {
        val flux1: Flux<String> = Flux.just("a", "b")
        val flatFlux = flux1.map{it.uppercase()}
            .flatMap{findByName(it)}
            .log()

        flatFlux.subscribe { s -> log.info(s.toString()) }
        Thread.sleep(500)

        StepVerifier
            .create(flatFlux)
            .expectSubscription()
            .expectNext("NameA1", "NameA2", "NameB1", "NameB2")
            .verifyComplete()
    }

    @Test
    fun flatMapSequentialOperator() {
        val flux1: Flux<String> = Flux.just("a", "b")
        val flatFlux = flux1.map{it.uppercase()}
            .flatMapSequential{findByName2(it)}
            .log()

        flatFlux.subscribe { s -> log.info(s.toString()) }
        Thread.sleep(500)

        StepVerifier
            .create(flatFlux)
            .expectSubscription()
            .expectNext("NameA1", "NameA2", "NameB1", "NameB2")
            .verifyComplete()
    }

    fun findByName(name : String) : Flux<String>{
        if(name.equals("A")) {
            return Flux.just("NameA1","NameA2")
        } else {
            return Flux.just("NameB1","NameB2")
        }
    }

    fun findByName2(name : String) : Flux<String>{
        if(name.equals("A")) {
            return Flux.just("NameA1","NameA2").delayElements(Duration.ofMillis(100))
        } else {
            return Flux.just("NameB1","NameB2")
        }
    }

    @Test
    public fun zipOperator(){
        var titles = Flux.just("Anime teste", "Simba")
        var studios = Flux.just("Estudio teste", "Estudio Simba")
        var episodes = Flux.just(12, 23)

        var zip = Flux.zip(titles, studios, episodes)
            .flatMap { tuple ->
                Flux.just(
                    Anime(tuple.t1, tuple.t2, tuple.t3)
                )
            }

        zip.subscribe{ s -> log.info(s.toString()) }

        StepVerifier
            .create(zip)
            .expectSubscription()
            .expectNext(
                Anime("Anime teste","Estudio teste",12),
                Anime("Simba","Estudio Simba",23)
            )
            .verifyComplete()

    }

    @Test
    public fun zipOperator2(){
        var titles = Flux.just("Anime teste", "Simba")
        var studios = Flux.just("Estudio teste", "Estudio Simba")
        var episodes = Flux.just(12, 23)

        var zip = titles
            .zipWith(studios)
            .zipWith(episodes)
            .flatMap { tuple -> Flux.just(Anime(tuple.t1.t1,tuple.t1.t2, tuple.t2))}
            .subscribe{ anime -> println(anime)}

//        StepVerifier
//            .create(zip)
//            .expectSubscription()
//            .expectNext(
//                Anime("Anime teste","Estudio teste",12),
//                Anime("Simba","Estudio Simba",23)
//            )
//            .verifyComplete()

    }

    @AllArgsConstructor
    @Getter
    @ToString
    @EqualsAndHashCode
    data class Anime(
        val title : String,
        val studio : String,
        val episodes : Int
    )



}