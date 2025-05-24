package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

class EpicTaskTest {
    EpicTask epicTask;
    List<Integer> listPartTaskId;

    @BeforeEach
    public void setUp(){
        epicTask = new EpicTask("test", "test");
        try{
            Field fieldListPartTaskId = epicTask.getClass().getDeclaredField("listPartTaskId");
            fieldListPartTaskId.setAccessible(true);
            listPartTaskId = (List<Integer>) fieldListPartTaskId.get(epicTask);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    void ShouldReturnTrueWhenListPartTaskIdContainsAddValue(){
        //When
        epicTask.addPartTaskId(10);
        //Then
        Assertions.assertTrue(listPartTaskId.contains(10));
    }

    @Test
    void shouldReturnFalseWhenCheckIfListPartTaskIdContainsRemovedValue() {
        //Given
        listPartTaskId.add(10);
        //When
        epicTask.removePartTaskById(10);
        //Then
        Assertions.assertFalse(listPartTaskId.contains(10));
    }

    @Test
    void shouldReturnFalseWhenCheckIfListPartTaskIdContainsAnyRemovedValue() {
        //Given
        listPartTaskId.add(1);
        listPartTaskId.add(2);
        listPartTaskId.add(3);
        listPartTaskId.add(4);
        listPartTaskId.add(5);
        //When
        epicTask.removeAllPartTask();
        //Then
        Assertions.assertFalse(listPartTaskId.contains(1)
                || listPartTaskId.contains(2)
                || listPartTaskId.contains(3)
                || listPartTaskId.contains(4)
                || listPartTaskId.contains(5));
    }

    @Test
    void shouldReturnFalseWhenCheckIfListPartTaskEqualsListPartTaskEpicTask() {
        //Given
        listPartTaskId.add(1);
        listPartTaskId.add(2);
        listPartTaskId.add(3);
        listPartTaskId.add(4);
        listPartTaskId.add(5);
        //When
        List<Integer> epicTaskListPartTaskId = epicTask.getListPartTaskId();
        //Then
        Assertions.assertIterableEquals(listPartTaskId, epicTaskListPartTaskId);
        Assertions.assertIterableEquals(listPartTaskId, epicTaskListPartTaskId);
    }
}