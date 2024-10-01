package dev.lpa;

import java.util.Random;

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


  }
}
