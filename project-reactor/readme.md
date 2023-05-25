Reactor: O Reactor é uma biblioteca Java para programação reativa. Ele fornece um modelo de programação assíncrona baseado em fluxos de dados (Flux) e valores singulares (Mono).

Flux: O Flux é uma sequência de zero ou mais elementos que emite dados de forma assíncrona. É usado para representar uma coleção de dados que podem ser transmitidos de maneira assíncrona ao longo do tempo.

Mono: O Mono é uma sequência que emite zero ou um elemento. Ele é usado para representar um valor opcional ou um resultado futuro assíncrono.

Scheduler: O Scheduler é responsável por agendar a execução de tarefas em threads ou recursos subjacentes. Os schedulers do Reactor fornecem controle sobre onde e como as tarefas são executadas.

Backpressure: O backpressure é uma técnica usada para lidar com a disparidade de velocidades entre o produtor e o consumidor de dados em um fluxo. Ele permite que o consumidor informe ao produtor quando está sobrecarregado, controlando a taxa de produção de dados.

Overflow Strategy: A overflow strategy se refere à estratégia usada quando um fluxo atinge sua capacidade máxima e tenta adicionar um elemento adicional. O Reactor fornece diferentes opções, como buffer, drop, latest e error.

Flux Transformation Operators: O Reactor fornece uma variedade de operadores para transformar e combinar fluxos, como map, filter, reduce, merge, concat, zip, entre outros. Esses operadores permitem manipular os dados emitidos pelos fluxos de forma declarativa.

Error Handling: O Reactor oferece operadores para lidar com erros, como onErrorReturn, onErrorResume, retry, entre outros. Eles permitem tratar exceções e fornecer estratégias de recuperação.

Hot and Cold Publishers: Os fluxos do Reactor podem ser "hot" ou "cold". Um fluxo cold começa a emitir elementos somente quando um consumidor se inscreve, enquanto um fluxo hot emite elementos independentemente de haver ou não consumidores inscritos.

Operações assíncronas: O Reactor suporta operações assíncronas, como chamadas de API de rede, acesso a banco de dados e operações de E/S. Ele permite que você escreva código reativo e assíncrono de forma concisa e eficiente.



O Flux e o Mono são duas classes principais do Reactor que permitem trabalhar com programação reativa. Aqui está um resumo das funções de cada uma delas:

Flux:

Representa uma sequência de zero ou mais elementos que são emitidos de forma assíncrona ao longo do tempo.
Pode emitir elementos de forma síncrona ou assíncrona.
É útil quando você precisa lidar com uma coleção de dados que pode ser transmitida ao longo do tempo, como um stream de eventos, uma lista de resultados de um banco de dados, entre outros.
Permite a aplicação de operadores de transformação, filtragem, combinação e agrupamento de dados, entre outros.
Pode ser usado para processar dados de forma paralela e concorrente.
Permite a manipulação de erros usando operadores dedicados, como onErrorReturn, onErrorResume, retry, entre outros.
Fornece métodos para lidar com backpressure, permitindo controlar a taxa de produção e consumo de dados.
Mono:

Representa uma sequência que emite zero ou um elemento.
É útil quando você precisa lidar com um valor opcional ou um resultado futuro assíncrono, como o resultado de uma chamada de API ou uma operação de E/S.
Pode emitir um elemento de forma síncrona ou assíncrona.
Fornece operadores para transformar e combinar dados de forma semelhante ao Flux, mas com foco em um único valor.
Também oferece operadores para lidar com erros e recuperar de falhas, como onErrorReturn, onErrorResume, retry, entre outros.
Pode ser convertido em um Flux com um único elemento usando o operador "flux()".
Em resumo, o Flux é usado para trabalhar com sequências assíncronas de zero ou mais elementos, enquanto o Mono é usado para representar uma sequência assíncrona que emite zero ou um elemento. Ambas as classes oferecem suporte a operadores para manipular e transformar os dados, lidar com erros e controlar o backpressure. O uso adequado de Flux e Mono ajuda na construção de aplicativos reativos e assíncronos usando o Reactor Framework.


No Reactor, o termo "overflow strategy" refere-se à estratégia usada quando um fluxo (ou "Flux") atinge sua capacidade máxima e tenta adicionar um elemento adicional. O Reactor oferece diferentes opções de estratégia para lidar com essa situação.

Existem várias estratégias de overflow disponíveis no Reactor:

1. Buffer: Essa é a estratégia padrão. Quando o fluxo atinge sua capacidade máxima, ele adiciona o elemento adicional em um buffer interno. Isso permite que o fluxo continue a receber elementos até que o buffer esteja cheio. Se o buffer ficar cheio, a próxima tentativa de adicionar um elemento resultará em um erro, chamado "BufferOverflowException".

2. Drop: Com essa estratégia, quando o fluxo atinge sua capacidade máxima, ele simplesmente descarta o elemento adicional, sem lançar um erro. O fluxo continua recebendo elementos subsequentes normalmente.

3. Latest: Nessa estratégia, quando o fluxo atinge sua capacidade máxima, ele substitui o elemento mais antigo pelo elemento mais recente. Isso significa que o fluxo sempre mantém o elemento mais recente e descarta os elementos mais antigos.

4. Error: Essa estratégia resulta em um erro imediato quando o fluxo atinge sua capacidade máxima e uma tentativa de adicionar um novo elemento é feita. Isso gera uma exceção do tipo "IllegalStateException".

Essas estratégias permitem que você controle como o fluxo reage quando a capacidade máxima é atingida. Você pode escolher a estratégia mais adequada com base nos requisitos do seu aplicativo e no comportamento desejado.

É importante observar que você pode especificar a estratégia de overflow ao criar um Flux usando o método `onBackpressureBuffer()`, `onBackpressureDrop()`, `onBackpressureLatest()`, ou `onBackpressureError()`. Por exemplo:
```
Flux<Integer> flux = Flux.range(1, 100)
    .onBackpressureBuffer(10); // Utiliza a estratégia de buffer com capacidade máxima de 10 elementos
```

Dessa forma, você pode personalizar o comportamento do fluxo em caso de capacidade máxima atingida.

No Reactor, o threading e os schedulers estão relacionados ao agendamento e execução de tarefas em threads ou recursos subjacentes. Aqui está um resumo desses conceitos:

Threading:

O threading refere-se ao uso de threads para executar tarefas de forma concorrente em um programa.
No Reactor, o threading é usado para executar operações assíncronas e evitar bloqueios, permitindo que o programa continue a responder a outras solicitações enquanto uma tarefa está em andamento.
O Reactor usa threads para lidar com a execução de tarefas assíncronas, como chamadas de API, operações de E/S ou qualquer operação que possa levar tempo para ser concluída.
O número de threads e a forma como são gerenciados podem ser configurados no Reactor para atender aos requisitos específicos de desempenho e escalabilidade do aplicativo.
Schedulers:

Os schedulers são componentes do Reactor responsáveis por agendar e controlar a execução de tarefas em threads ou recursos subjacentes.
Eles fornecem uma abstração para agendar tarefas em um ambiente assíncrono e reativo.
Existem diferentes tipos de schedulers disponíveis no Reactor, como o Schedulers.immediate(), Schedulers.single(), Schedulers.parallel(), Schedulers.elastic() e Schedulers.fromExecutor().
Cada tipo de scheduler tem suas características e finalidades específicas. Por exemplo, alguns são adequados para tarefas em um único thread, enquanto outros são otimizados para tarefas em paralelo.
Os schedulers permitem que você especifique onde e como as tarefas serão executadas, controlando o uso de threads e recursos do sistema.
Em resumo, o threading no Reactor refere-se ao uso de threads para executar tarefas assíncronas e evitar bloqueios. Os schedulers, por sua vez, são responsáveis pelo agendamento e controle da execução dessas tarefas, permitindo que você especifique onde e como elas serão executadas. Ao usar threading e schedulers adequadamente, você pode otimizar o desempenho e a escalabilidade de aplicativos reativos e assíncronos construídos com o Reactor.


Os operators (operadores) no Reactor são métodos fornecidos pelo framework que permitem transformar, filtrar, combinar e manipular fluxos de dados assíncronos. Eles oferecem uma forma declarativa e poderosa de processar os elementos dentro de um fluxo. Aqui está um resumo dos operadores mais comuns:

Transformação:
map: Aplica uma função a cada elemento do fluxo, retornando um novo valor.
flatMap: Aplica uma função a cada elemento do fluxo, retornando um fluxo de valores que é concatenado.
switchMap: Substitui o fluxo anterior pelo novo fluxo gerado a partir de cada elemento.
Filtragem:
filter: Filtra os elementos do fluxo com base em uma condição booleana.
take: Limita o número máximo de elementos emitidos pelo fluxo.
skip: Pula os primeiros N elementos do fluxo.
Combinação:
merge: Combina vários fluxos em um único fluxo, mesclando os elementos em ordem de emissão.
concat: Concatena vários fluxos em um único fluxo, emitindo os elementos em ordem sequencial.
zip: Combina elementos correspondentes de vários fluxos em pares ou tuplas.
Agrupamento:
groupBy: Agrupa elementos com base em uma chave comum, criando fluxos separados para cada chave.
window: Divide o fluxo em janelas menores com base em uma condição de abertura e fechamento.
Gerenciamento de erros:
onErrorReturn: Substitui um erro por um valor padrão.
onErrorResume: Substitui um erro por um fluxo de elementos alternativo.
retry: Tenta novamente a operação quando ocorre um erro.
Esses são apenas alguns dos operadores disponíveis no Reactor. Existem muitos outros operadores que podem ser usados para diversas finalidades, como transformar, filtrar, combinar e manipular fluxos de dados de maneiras sofisticadas. A escolha adequada e a combinação dos operadores permitem criar pipelines de processamento de dados reativos de forma expressiva e eficiente.

Combining Publishers (combinando publicadores) refere-se a uma operação no Reactor que permite combinar os elementos emitidos por dois ou mais fluxos em um único fluxo. Essa operação é útil quando você precisa processar múltiplos fluxos de dados e obter um único fluxo combinado com base em determinadas regras ou lógica.

Existem vários operadores disponíveis no Reactor para combinar publicadores, sendo os principais:

1. `zip`: Combina elementos correspondentes de dois ou mais fluxos em pares ou tuplas. Ele aguarda que todos os fluxos tenham emitido um elemento antes de combinar os valores. Por exemplo:
   ```java
   Flux<Integer> flux1 = Flux.just(1, 2, 3);
   Flux<String> flux2 = Flux.just("A", "B", "C");

   Flux<Tuple2<Integer, String>> combined = Flux.zip(flux1, flux2);
   ```

2. `merge`: Combina múltiplos fluxos em um único fluxo, mesclando os elementos em ordem de emissão. Os elementos são combinados à medida que são emitidos por qualquer um dos fluxos. Por exemplo:
   ```java
   Flux<Integer> flux1 = Flux.just(1, 2, 3);
   Flux<Integer> flux2 = Flux.just(4, 5, 6);

   Flux<Integer> combined = Flux.merge(flux1, flux2);
   ```

3. `concat`: Concatena múltiplos fluxos em um único fluxo, emitindo os elementos em ordem sequencial. Cada fluxo é processado sequencialmente antes de passar para o próximo. Por exemplo:
   ```java
   Flux<Integer> flux1 = Flux.just(1, 2, 3);
   Flux<Integer> flux2 = Flux.just(4, 5, 6);

   Flux<Integer> combined = Flux.concat(flux1, flux2);
   ```

4. `combineLatest`: Combina os elementos mais recentes de dois ou mais fluxos em um fluxo combinado. A cada vez que um fluxo emite um novo elemento, o fluxo combinado é atualizado com os elementos mais recentes de todos os fluxos. Por exemplo:
   ```java
   Flux<Integer> flux1 = Flux.just(1, 2, 3);
   Flux<Integer> flux2 = Flux.just(4, 5, 6);

   Flux<Tuple2<Integer, Integer>> combined = Flux.combineLatest(flux1, flux2, Tuple2::of);
   ```

Esses são apenas alguns dos operadores disponíveis para combinar publicadores no Reactor. Eles fornecem maneiras flexíveis de processar e combinar elementos de fluxos diferentes com base em suas necessidades. Ao combinar publicadores, você pode criar pipelines de processamento de dados mais avançados e realizar operações complexas em fluxos de dados reativos.


1. Exemplo usando `zip`:
```java
Flux<Integer> flux1 = Flux.just(1, 2, 3);
Flux<String> flux2 = Flux.just("A", "B", "C");

Flux<Tuple2<Integer, String>> combined = Flux.zip(flux1, flux2);

combined.subscribe(System.out::println);
```
Output:
```
(1, A)
(2, B)
(3, C)
```

2. Exemplo usando `merge`:
```java
Flux<Integer> flux1 = Flux.just(1, 2, 3);
Flux<Integer> flux2 = Flux.just(4, 5, 6);

Flux<Integer> combined = Flux.merge(flux1, flux2);

combined.subscribe(System.out::println);
```
Output:
```
1
2
3
4
5
6
```

3. Exemplo usando `concat`:
```java
Flux<Integer> flux1 = Flux.just(1, 2, 3);
Flux<Integer> flux2 = Flux.just(4, 5, 6);

Flux<Integer> combined = Flux.concat(flux1, flux2);

combined.subscribe(System.out::println);
```
Output:
```
1
2
3
4
5
6
```

4. Exemplo usando `combineLatest`:
```java
Flux<Integer> flux1 = Flux.just(1, 2, 3);
Flux<Integer> flux2 = Flux.just(4, 5, 6);

Flux<Tuple2<Integer, Integer>> combined = Flux.combineLatest(flux1, flux2, Tuple2::of);

combined.subscribe(System.out::println);
```
Output:
```
(3, 4)
(3, 5)
(3, 6)
```

Lembrando que esses são apenas exemplos básicos para ilustrar o comportamento dos operadores de combinação de publicadores no Reactor. Os outputs podem variar dependendo dos fluxos e valores fornecidos.

UC-995a3100-39fb-46db-9678-f682aa1e6acb

https://grupouol.udemy.com/certificate/UC-995a3100-39fb-46db-9678-f682aa1e6acb/

This certificate above verifies that Vinícius Marini Costa e Oliveira successfully completed the course Java Reactive Programming [ From Scratch ] on 05/25/2023 as taught by Vinoth Selvaraj on Udemy. The certificate indicates the entire course was completed as validated by the student. The course duration represents the total video hours of the course at time of most recent completion.