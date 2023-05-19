import com.github.javafaker.Faker;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

public class CountryMainGenerate {
    public static void simples() {
        Flux.generate(fluxSink -> {
                    String country;
                    country = Faker.instance().country().name();
                    fluxSink.next(country);
                    if(country.toLowerCase().equals("brazil")){
                        fluxSink.complete();
                    }
                })
                .subscribe(s -> System.out.println("Received: " + s));

    }

    public void comCounter() {
        Flux.generate(
                () -> 1,
                (counter, sink) -> {
                    String country;
                    country = Faker.instance().country().name();
                    sink.next(country);
                    if(counter>=20 || country.toLowerCase().equals("brazil"))
                        sink.complete();
                    return counter + 1;
                }
        )
                .subscribe(s -> System.out.println("Received: " + s));

    }
    public static void main(String[] args) {
        CountryMainGenerate countryMainGenerate = new CountryMainGenerate();
        countryMainGenerate.comCounter();
    }
}
