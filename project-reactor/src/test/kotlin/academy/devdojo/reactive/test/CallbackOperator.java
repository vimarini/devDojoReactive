package academy.devdojo.reactive.test;

import reactor.core.publisher.Flux;

public class CallbackOperator {
    public static void main(String[] args) {
    CallbackOperator callbackOperator = new CallbackOperator();
    callbackOperator.first();
    }

    public void first(){
        Flux.create(fluxSink -> {
            System.out.println("inside create");
            for (int i = 0; i < 5; i++) {
                fluxSink.next(i);
            }
            fluxSink.complete();
            System.out.println("--completed");
        })
                .doOnComplete(() -> System.out.println("doOnComplete"))
                .doFirst(() -> System.out.println("doFirst"))
                .doOnNext(o ->System.out.println("doOnNext: "+ o ))
                .doOnSubscribe(s -> System.out.println("doOnSubscribe: " + s))
                .doOnRequest(r -> System.out.println("doOnRequest: "+ r ))
                .doOnError(r -> System.out.println("doOnError: "+ r.getMessage() ))
                .doOnTerminate(() -> System.out.println("doOnTerminate"))
                .doOnCancel(()-> System.out.println("doOnCancel"))
                .doFinally(signalType -> System.out.println("doFinally: "+ signalType))
                .doOnDiscard(Object.class, o -> System.out.println("doOnDiscard"+o))
                .subscribe(s -> System.out.println("Received: "+s));
    }
}
