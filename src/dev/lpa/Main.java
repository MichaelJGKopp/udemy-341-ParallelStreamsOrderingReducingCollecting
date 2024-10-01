package dev.lpa;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
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
//      .forEach(System.out::println);
  }
}
