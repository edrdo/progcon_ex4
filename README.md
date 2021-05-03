
# Ficha de Exercícios 4


__[Programação Concorrente (CC3037)](https://www.dcc.fc.up.pt/~edrdo/aulas/pc), DCC/FCUP__

_Eduardo R. B. Marques, DCC/FCUP_ 

**Objectivos**: Introdução ao uso da ferramenta Cooperari.


## 0. Configuração

### 0.1. Configuração no repl.it

Certifique-se que a variável de ambiente PATH é configurada

 ```
 export PATH=$HOME/progconex4/cooperari-0.4/bin:$PATH
 ```

### 0.1. Instalação do Cooperari em PC

Se quiser instalar no seu PC siga os passos disponibilizados no guia ["Getting Started"](https://github.com/Cooperari/cooperari/blob/master/GettingStarted.md) para instalar o Cooperari no PC local e testar o funcionamento dos utilitários `cjavac` e `cjunit`.


__Notas__:

- Os comandos `cjavac` e `cjunit` deverão ser 
executados a partir do directório de topo.

- O código fonte de apoio aos exercícios encontra-se na pasta `src`.
Usando `cjavac` o código será compilado para o directório `classes`. 

- Logs e outros ficheiros produzidos pelo Cooperari serão armazenados na pasta `cdata`.

## 1. Contadores (directório `src/pc/counter`)

O interface `Counter` define em abstracto as operações para um "contador" inteiro com as operações:

  - `increment()`: incrementa o valor do contador;
  - `value()`: obtém valor do contador.

As seguintes classes definem implementações de `Counter`, com diferentes
tipos de sincronização entre threads:

  - `UCounter`: sem mecanismo de sincronização ("unsynchronized");
  - `LCounter`: baseada em locks (na forma de blocos `synchronized`);
  - `ACounter`: baseada no uso de [AtomicInteger](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/atomic/AtomicInteger.html).

Execute `cjunit pc.counter.AllTests` onde cada uma destas implementações  é testada pelo Cooperari. O teste feito é expresso pelo código abaixo,
correspondente à execução de 2 threads que incrementam cada uma o contador 
uma vez, seguida da verificação do valor do contador (isto é,
que o contador foi incrementado em duas unidades).

```java
  @Test
  public void test() {
    Counter c = createCounter(0);
    CSystem.forkAndJoin(
      () -> c.increment(),
      () -> c.increment()
    );
    assertEquals(2, c.value());
  }
```

Executando no terminal a partir do directório de topo (`pcex3`):

```
cjavac # para compilar ou re-compilar
cjunit pc.counter.AllTests
```

deverá obter um output semelhante a:

```
Configuring load-time weaving ...
JAR file for 'pc.counter.AllTests' saved to './cdata/pc.counter.AllTests-cooperari.jar'
== Cooperari 0.4 - JUnit test execution - mode: cooperative ==
pc.counter.UTest
  test                                                    [failed: java.lang.AssertionError]
    > trials: 1 time: 124 ms coverage: 100.0 % (5 / 5 yp)
    > failure trace: '/Users/edrdo/Desktop/Worklog/2019/aulas/pc/homepage/aulas/lab2/pcex3/cdata/pc.counter.UTest/test.1.trace.log'
pc.counter.LTest
  test                                                    [passed]
    > trials: 25 time: 353 ms coverage: 100.0 % (7 / 7 yp)
pc.counter.ATest
  test                                                    [failed: java.lang.AssertionError]
    > trials: 2 time: 57 ms coverage: 100.0 % (8 / 8 yp)
    > failure trace: '/Users/edrdo/Desktop/Worklog/2019/aulas/pc/homepage/aulas/lab2/pcex3/cdata/pc.counter.ATest/test.2.trace.log'
== Summary ==
Executed: 3; Skipped: 0;  Failed: 2; Execution time: 637 ms
== Yield point coverage ==
Coverage rate: 100.0 % (18 / 18 yp)
Global coverage report: '/Users/edrdo/Desktop/Worklog/2019/aulas/pc/homepage/aulas/lab2/pcex3/cdata/all_yield_points.coverage.log'
```

### 1.1. `UCounter`

Examine o log da execução assinalado em correspondência 
à falha na execução de `UTest.test()`. Dado que o contador não é sincronizado,
"race conditions" são possíveis e é isso que o log assinala nas entradas
em que o campo `EVENT` tem valor `R`, como no seguinte fragmento em que temos
uma corrida entre as threads 1 (lê) e 2 (escreve) no acesso ao valor do contador.
  
```
# EXECUTION TRACE
#       TID     STEP    EVENT   SOURCE FILE     LINE    YIELD POINT     STAGE
...
10      1       2       R       UCounter.java   12      field-get(pc.counter.UCounter.value)        0
11      1       2       -       UCounter.java   12      field-set(pc.counter.UCounter.value)        0
12      2       3       R       UCounter.java   12      field-set(pc.counter.UCounter.value)        0
```

### 1.2. `LCounter`

O teste `LCounter.test()` não falha. Observe que o código em `LCounter` usa um bloco `synchronized` em 
`increment()` prevenindo "race conditions". Experimente modificar o código da forma
ilustrada abaixo  e repita a execução do teste. Já deverá obter falhas na execução para porque
"race conditions" se tornam possíveis. 

```java
  @Override
  public void increment() {
    int v = value;
    synchronized(this) {
      value = v + 1;
    }
  }
```

### 1.3. `ACounter` 

O teste `ATest.test()` falha. Note que `ACounter`  usa um objecto `AtomicInteger` mas `increment()` 
compreende 2 instruções atómicas sobre esse objecto, uma de leitura (`get()`) e outra de escrita (`set()`). Como pode resolver o problema?


## 2. Registos (directório `src/pc/register`) 

O interface `Register<T>` define um TAD para um registo com as operações:

  - `T read()`: lê valor do registo;
  - `void write(T value)`: escreve valor `value` no registo;
  - `transform(Function<T,T> func)`: aplica `func` sobre
  o valor anterior do registo, e escreve o resultado obtido.

Analogamente ao exercício anterior temos classes `URegister`, `LRegister` e `ARegister`
que implementam `Register`, respectivamente: sem uso de mecanismos de sincronização, 
baseada em locks, e em instruções atómicas via [`AtomicReference`](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/atomic/AtomicReference.html).

Em `test` encontra o seguinte código sobre um registo `r` com tipo `Register<Integer>`
em que:
  
  - são executadas 3 threads cada uma executando uma operação diferente sobre o registo;
  - são validados todos os valores possíveis para a leitura subsequente do registo 
  numa execução correcta. Note que haverá 6 execuções (correctas) possíveis das 3 operações mas só 4 valores possíveis resultantes para o registo no final.

```java
  @Test
  public void test() {
    Register<Integer> r = createRegister(0);
    CSystem.forkAndJoin(
      () -> r.transform(x -> x + 1),
      () -> r.transform(x -> x * x * x),
      () -> r.transform(x -> 2 * x + 1)
    );
    int v = r.read();
    assertTrue(v == 3 || v == 27 || v == 2 || v == 8);
  }
```

Executando a partir do directório de topo:

```
cjavac 
cjunit pc.register.AllTests
```

deverá obter um output semelhante a:

```
...
pc.register.UTest
  test                                                    [failed: java.lang.AssertionError]
    > trials: 2 time: 142 ms coverage: 83.3 % (5 / 6 yp)
    > failure trace: '/Users/edrdo/Desktop/Worklog/2019/aulas/pc/homepage/aulas/lab2/pcex3/cdata/pc.register.UTest/test.2.trace.log'
pc.register.LTest
  test                                                    [passed]
    > trials: 25 time: 510 ms coverage: 69.2 % (9 / 13 yp)
pc.register.ATest
  test                                                    [failed: java.lang.AssertionError]
    > trials: 2 time: 65 ms coverage: 80.0 % (8 / 10 yp)
    > failure trace: '/Users/edrdo/Desktop/Worklog/2019/aulas/pc/homepage/aulas/lab2/pcex3/cdata/pc.register.ATest/test.2.trace.log'
...
```

### 2.1. `URegister`

Analogamente a 1.1 examine o log de falha e perceba o que acontece em termos de "race conditions".

### 2.2. `LRegister`

Analogamente a 1.2 introduza uma "race condition" em `LRegister` e verifique 
que `LTest.test()` falha.

### 2.3. `ARegister`

O teste `ATest.test()` falha porque a implementação de `ARegister.transform()` aplica a transformação do registo recorrendo a uma leitura e uma escrita sobre a instância de `AtomicReference`.     

Modifique o código por forma a funcionar correctamente usando [`AtomicReference.compareAndSet()`](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/atomic/AtomicReference.html#compareAndSet-V-V-).
Valide as suas alterações voltando a executar os testes.

Numa 2ª versão poderá fazer uso de [`AtomicReference.getAndUpdate()`](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/atomic/AtomicReference.html#getAndUpdate-java.util.function.UnaryOperator-) para código mais sucinto; note é que a implementação de `getAndUpdate()` recorre na verdade a `compareAndSet()` de uma forma que deverá ser muito similar à sua 1ª versão (como pode ver [aqui](https://hg.openjdk.java.net/jdk8/jdk8/jdk/file/687fd7c7986d/src/share/classes/java/util/concurrent/atomic/AtomicReference.java)).


## 3. Stacks (directório `src/pc/stack`)

O interface `Stack<T>` define um TAD para uma stack com as operações:

  - `T pop()`: obtém valor no topo da stack, ou lança `EmptyStackException` se 
stack estiver vazia;
  - `void push(T value)`: adiciona `value` ao topo da stack;
  - `int size()`: devolve número de elementos na stack.

Como nos exercícios anteriores são dadas 3 implementações de `Stack`:

  - `UStack`: sem qualquer mecanismo de sincronização;
  - `LStack`: baseada no uso de locks;
  - `AStack`: baseada no uso de [`AtomicReference`](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/atomic/AtomicReference.html).

Todas as classes empregam uma lista ligada para guardar os elementos da stack, 
com nós da lista expressos pela classe `Node`, onde a cabeça da lista (caso definida, i.e.,
diferente de `null`) contém o elemento no topo da stack.

Para testar as implementações, execute:

```
cjavac 
cjunit pc.stack.AllTests
```

### 3.1. `UStack` 

Faça uma análise análoga a 1.1 e 2.1 para `UStack`.

### 3.2. `LStack`


Os testes de `LTest` falham devido a um pequeno "bug" no código de `LStack`!
Analise o código para perceber qual é o bug, e corrija-o. 
Execute de novo `LTest` para validar a correcção.

### 3.3. `AStack`

O código de `AStack` está incompleto, pois o corpo do método `push()` está vazio (e claro, os testes em `ATest` falham)!

Defina uma implementação do método, baseado na ideia expressa na implementação de `pop()` 
que emprega
[`AtomicReference.compareAndSet()`](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/atomic/AtomicReference.html#compareAndSet-V-V-).


