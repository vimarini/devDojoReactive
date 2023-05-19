package academy.devdojo.reactive.test;

import com.github.javafaker.Faker;
import reactor.core.publisher.Flux;

public class HandleOperator {

    public static void main(String[] args) {
        HandleOperator handleOperator = new HandleOperator();
        handleOperator.handleCondition();
    }

    public void simpleHandle() {
        Flux.range(1, 20)
                .handle((integer, syncSink) -> {
                    if (integer % 2 == 0)
                        syncSink.next(integer);//filter
                    else
                        syncSink.next(integer + "a");//map
                })
                .subscribe(s -> System.out.println(s));
    }

    public void handleCondition(){
        Flux.generate(synchronousSink -> synchronousSink.next(Faker.instance().country().name()))
                .map(Object::toString)
                .handle((s, synchronousSink)->{
                    synchronousSink.next(s);
                    if (s.toLowerCase().equals("brazil"))
                        synchronousSink.complete();
                })
                .subscribe(s -> System.out.println(s));
    }

}
