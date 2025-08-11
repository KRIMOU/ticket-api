package com.pluralsight.ticket_api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FizzBuzzTest {

    FizzBuzz fizzBuzz;

    @BeforeEach
    void setup() {
        fizzBuzz = new FizzBuzz();
    }

    @Test
    void testFizz() {
        assertEquals("Fizz", fizzBuzz.checkMultiple(3));
    }

    @Test
    void testBuzz() {
        assertEquals("Buzz", fizzBuzz.checkMultiple(5));
    }

    @Test
    void testNoBuzzNoFizz() {
        assertEquals("1", fizzBuzz.checkMultiple(1));
    }

    @Test
    void testBuzzFizz() {
        assertEquals("FizzBuzz", fizzBuzz.checkMultiple(15));
    }
}
