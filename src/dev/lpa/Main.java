package dev.lpa;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

record Person(String firstName, String lastName, int age) {

  private final static String[] firsts =
    {"Able", "Bob", "Charlie", "Donna", "Eve", "Fred"};
  private final static String[] lasts =
    {"Norton", "Ohara", "Peterson", "Quincy", "Richardson", "Smith"};

  private final static Random random = new Random();

  public Person() {
    this(firsts[random.nextInt(firsts.length)],
      lasts[random.nextInt(lasts.length)],
      random.nextInt(18, 100));
  }

  @Override
  public String toString() {
    return "%s, %s (%d)".formatted(firstName, lastName, age);
  }
}
public class Main {

  public static void main(String[] args) {

    var persons = Stream.generate(Person::new)
        .limit(10)
        .sorted(Comparator.comparing(Person::lastName))
        .toArray();

    System.out.println("Persons array:");
    for (var p : persons) {
      System.out.println(p.toString());
    }

    System.out.println("--------------------------");

    System.out.println("Persons stream:");
    Arrays.stream(persons)
      .parallel()
      .forEachOrdered(System.out::println); // keeps the source order for a parallel stream
    // increases overhead
//      .forEach(System.out::println);

    System.out.println("--------------------------");

    int sum = IntStream.range(1, 101)
      .parallel()
      .reduce(0, Integer::sum);

    System.out.println("The sum of the numbers is:" + sum);

    String humptyDumpty = """
      Humpty Dumpty sat on a Wall.
      Humpty Dumpty had a great fall.
      All the king's horses and all the king's men
      couldn't put Humpty together again.
      """;

    System.out.println("------------------------------");
    var words = new Scanner(humptyDumpty).tokens().toList();
    words.forEach(System.out::println);
    System.out.println("------------------------------");

    var backTogetherAgain = words
      .parallelStream()
//      .reduce("", (st, s) ->  st += s + " ", String::concat);
      .collect(Collectors.joining(" "));

    System.out.println(backTogetherAgain + "\n");

    System.out.println("------------------------------");

    var lastNameCounts =
      Stream.generate(Person::new)
        .limit(10_000)
        .parallel()
        .collect(Collectors.groupingByConcurrent(Person::lastName, Collectors.counting()));
    // not efficient to use parallel here for recombining map streams

    System.out.println("lastNameCounts:");
    lastNameCounts.entrySet().forEach(System.out::println);

    long total = 0;
    for ( var count : lastNameCounts.values()) {
      total += count;
    }
    System.out.println("Total count: " + total);

    System.out.println(lastNameCounts.getClass().getName());

    System.out.println("---------------------------------------");

    // side-effects, stream will not create map but add or modify
    var lastCounts = new ConcurrentSkipListMap<String, Long>(); // TreeMap alternative, sorted
    Stream.generate(Person::new)
      .limit(10_000)
      .parallel()
      .forEach((person) -> lastCounts.merge(person.lastName(), 1L, Long::sum));

    System.out.println(lastCounts);

    total = 0;
    for ( var count : lastCounts.values()) {
      total += count;
    }
    System.out.println("Total count: " + total);

    System.out.println(lastCounts.getClass().getName());

    System.out.println("---------------------------------------");

    // side-effects, stream will not create map but add or modify
    var lastCounts2 = Collections.synchronizedMap(
      new TreeMap<String, Long>()); // TreeMap alternative, sorted
    Stream.generate(Person::new)
      .limit(10_000)
      .parallel()
      .forEach((person) -> lastCounts2.merge(person.lastName(), 1L, Long::sum));

    System.out.println(lastCounts2);

    total = 0;
    for ( var count : lastCounts2.values()) {
      total += count;
    }
    System.out.println("Total count: " + total);

    System.out.println(lastCounts2.getClass().getName());
  }
}
