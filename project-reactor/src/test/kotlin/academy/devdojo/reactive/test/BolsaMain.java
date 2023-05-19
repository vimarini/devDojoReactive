package academy.devdojo.reactive.test;

public class BolsaMain {
    public static void main(String[] args) {
        RandomValueProducer producer = new RandomValueProducer();
        int count = 20;

        producer.generateRandomValues(count)
                .filter(i -> i<23)
                .subscribe(i -> System.out.println("Indicação de Compra: " + i));

        producer.generateRandomValues(count)
                .filter(i -> i>26)
                .subscribe(i -> System.out.println("Indicação de Venda: " + i));
    }
}
