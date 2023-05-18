package academy.devdojo.reactive.test;

public class Main {
    public static void main(String[] args) {
        RandomValueProducer producer = new RandomValueProducer();
        int count = 20;

        producer.generateRandomValues(count)
                .filter(i -> i<30)
                .subscribe(i -> System.out.println("Indicação de Compra: " + i));

        producer.generateRandomValues(count)
                .filter(i -> i>50)
                .subscribe(i -> System.out.println("Indicação de Venda: " + i));
    }
}
