import com.github.javafaker.Faker;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

public class CountryMainCreate {
//    public static void main(String[] args) {
//        List<String> countries = new ArrayList<>();
//        Flux.create(fluxSink -> {
//                    String country;
//            do {
//                country = Faker.instance().country().name();
//                if(!countries.contains(country))
//                    countries.add(country);
//                fluxSink.next(country);
//            } while (!country.toLowerCase().equals("brazil"));
//            fluxSink.complete();
//        })
//                .filter(countries::contains)
//                .subscribe(s -> System.out.println("Received: " + s));
//
//    }

    public static void main(String[] args) {
        List<String> countries = new ArrayList<>();
        Flux.create(fluxSink -> {
                    String country;
                    do {
                        country = Faker.instance().country().name();
                        if(!countries.contains(country))
                            countries.add(country);
                        fluxSink.next(country);
                    } while (!country.toLowerCase().equals("brazil") && !fluxSink.isCancelled());
                    fluxSink.complete();
                })
                .filter(countries::contains)
                .take(3)
                .subscribe(s -> System.out.println("Received: " + s));

    }
}
