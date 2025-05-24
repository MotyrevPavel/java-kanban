package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class SimpleTaskTest{
    static SimpleTask task1;
    static SimpleTask task2;

    @BeforeAll
    public static void setUp(){
        task1 = new SimpleTask("task", "task", 1);
        task2 = new SimpleTask("task", "task", 1);
    }

    @Test
    void shouldReturnTrueWhenComparingEqualObjects() {
        //When
        boolean isEquals = task1.equals(task2);
        //Then
        Assertions.assertTrue(isEquals);
    }

    @Test
    void shouldReturnTrueWhenComparingHashCodeEqualObjects() {
        //When
        boolean isEqualsHashCode = false;
        if (task1.equals(task2)){
            isEqualsHashCode = task1.hashCode() == task2.hashCode();
        }
        //Then
        Assertions.assertTrue(isEqualsHashCode);
    }
}