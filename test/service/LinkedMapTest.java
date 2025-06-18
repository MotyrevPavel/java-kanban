package service;

import model.SimpleTask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinkedMapTest {
    LinkedMap<Integer> linkedMap;

    @BeforeEach
    void setUp() {
        linkedMap = new LinkedMap<>();
        linkedMap.add(0, 1);
        linkedMap.add(0, 2);
        linkedMap.add(0, 3);
        linkedMap.add(1, 10);
        linkedMap.add(0, 3);
        linkedMap.add(0, 3);
        linkedMap.add(2, 20);
        linkedMap.add(3, 30);

    }

    @Test
    void shouldContainsValueAfterAdd() {
        Assertions.assertTrue(linkedMap.getAllValues().contains(3));
    }

    @Test
    void shouldNotHaveValueAfterAddNewValueWithTheSameId() {
        Assertions.assertFalse(linkedMap.getAllValues().contains(1));
    }

    @Test
    void shouldNotHaveValuesDuplicates() {
        assertEquals(4, linkedMap.getAllValues().size());
    }

    @Test
    void shouldNotContainsValueAfterRemove() {
        //When
        linkedMap.removeItemById(0);
        linkedMap.removeItemById(1);
        linkedMap.removeItemById(2);
        linkedMap.removeItemById(3);
        //Then
        assertEquals(0, linkedMap.getAllValues().size());
    }

    @Test
    void shouldReturnValuesInCorrectSequence() {
        //Given
        Integer[] intArray = {10, 3, 20, 30};
        //Then
        Assertions.assertArrayEquals(intArray, linkedMap.getAllValues().toArray());
    }
}