package managers.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.EpicTask;
import tasks.PartEpicTask;
import tasks.SimpleTask;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

class InMemoryTaskManagerTest {
    private HashMap<Integer, SimpleTask> simpleTaskMap;
    private HashMap<Integer, EpicTask> epicTaskMap;
    private HashMap<Integer, PartEpicTask> partEpicTaskMap;
    InMemoryTaskManager inMemoryTaskManager;

    @BeforeEach
    public void setUp() {
        inMemoryTaskManager = new InMemoryTaskManager();
        try {
            Field fieldSimpleTaskMap = inMemoryTaskManager.getClass().getDeclaredField("simpleTaskMap");
            fieldSimpleTaskMap.setAccessible(true);
            simpleTaskMap = (HashMap<Integer, SimpleTask>) fieldSimpleTaskMap.get(inMemoryTaskManager);

            Field fieldEpicTaskMap = inMemoryTaskManager.getClass().getDeclaredField("epicTaskMap");
            fieldEpicTaskMap.setAccessible(true);
            epicTaskMap = (HashMap<Integer, EpicTask>) fieldEpicTaskMap.get(inMemoryTaskManager);

            Field fieldPartEpicTaskMap = inMemoryTaskManager.getClass().getDeclaredField("partEpicTaskMap");
            fieldPartEpicTaskMap.setAccessible(true);
            partEpicTaskMap = (HashMap<Integer, PartEpicTask>) fieldPartEpicTaskMap.get(inMemoryTaskManager);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void shouldReturnTrueWhenValuesInSimpleTaskMapEqualsSimpleTaskArrayInMemoryTaskManager() {
        //Given
        SimpleTask simpleTask1 = new SimpleTask("", "");
        SimpleTask simpleTask2 = new SimpleTask("", "");
        SimpleTask simpleTask3 = new SimpleTask("", "");
        simpleTaskMap.put(simpleTask1.getId(), simpleTask1);
        simpleTaskMap.put(simpleTask2.getId(), simpleTask2);
        simpleTaskMap.put(simpleTask3.getId(), simpleTask3);
        SimpleTask[] simpleTaskArray = simpleTaskMap.values().toArray(new SimpleTask[3]);
        //When
        SimpleTask[] simpleTaskArrayInMemoryTaskManager = inMemoryTaskManager.getAllSimpleTask()
                .toArray(new SimpleTask[3]);
        //Then
        Assertions.assertArrayEquals(simpleTaskArray, simpleTaskArrayInMemoryTaskManager);
    }

    @Test
    void shouldReturnTrueWhenValuesInEpicTaskMapEqualsEpicTaskArrayInMemoryTaskManager() {
        //Given
        EpicTask epicTask1 = new EpicTask("", "");
        EpicTask epicTask2 = new EpicTask("", "");
        EpicTask epicTask3 = new EpicTask("", "");
        epicTaskMap.put(epicTask1.getId(), epicTask1);
        epicTaskMap.put(epicTask2.getId(), epicTask2);
        epicTaskMap.put(epicTask3.getId(), epicTask3);
        EpicTask[] epicTasksArray = epicTaskMap.values().toArray(new EpicTask[3]);
        //When
        EpicTask[] epicTasksArrayInMemoryTaskManager = inMemoryTaskManager.getAllEpicTask()
                .toArray(new EpicTask[3]);
        //Then
        Assertions.assertArrayEquals(epicTasksArray, epicTasksArrayInMemoryTaskManager);
    }

    @Test
    void shouldReturnTrueWhenValuesInPartEpicTaskMapEqualsPartEpicTasksArrayInMemoryTaskManager() {
        //Given
        EpicTask epicTask = new EpicTask("", "");
        PartEpicTask partEpicTask1 = new PartEpicTask("", "", epicTask.getId());
        PartEpicTask partEpicTask2 = new PartEpicTask("", "", epicTask.getId());
        PartEpicTask partEpicTask3 = new PartEpicTask("", "", epicTask.getId());
        partEpicTaskMap.put(partEpicTask1.getId(), partEpicTask1);
        partEpicTaskMap.put(partEpicTask2.getId(), partEpicTask2);
        partEpicTaskMap.put(partEpicTask3.getId(), partEpicTask3);
        PartEpicTask[] partEpicTasksArray = partEpicTaskMap.values().toArray(new PartEpicTask[3]);
        //When
        PartEpicTask[] partEpicTasksArrayInMemoryTaskManager = inMemoryTaskManager.getAllPartEpicTask()
                .toArray(new PartEpicTask[3]);
        //Then
        Assertions.assertArrayEquals(partEpicTasksArray, partEpicTasksArrayInMemoryTaskManager);
    }

    @Test
    void shouldReturnTrueWhenSimpleTaskMapIsEmptyAfterUsingRemoveAllSimpleTaskInMemoryTaskManager() {
        //Given
        SimpleTask simpleTask1 = new SimpleTask("", "");
        SimpleTask simpleTask2 = new SimpleTask("", "");
        SimpleTask simpleTask3 = new SimpleTask("", "");
        simpleTaskMap.put(simpleTask1.getId(), simpleTask1);
        simpleTaskMap.put(simpleTask2.getId(), simpleTask2);
        simpleTaskMap.put(simpleTask3.getId(), simpleTask3);
        //When
        inMemoryTaskManager.removeAllSimpleTask();
        //Then
        Assertions.assertTrue(simpleTaskMap.isEmpty());
    }

    @Test
    void shouldReturnTrueWhenEpicTaskMapIsEmptyAfterUsingRemoveAllEpicTaskInMemoryTaskManager() {
        //Given
        EpicTask epicTask1 = new EpicTask("", "");
        EpicTask epicTask2 = new EpicTask("", "");
        EpicTask epicTask3 = new EpicTask("", "");
        epicTaskMap.put(epicTask1.getId(), epicTask1);
        epicTaskMap.put(epicTask2.getId(), epicTask2);
        epicTaskMap.put(epicTask3.getId(), epicTask3);
        //When
        inMemoryTaskManager.removeAllEpicTask();
        //Then
        Assertions.assertTrue(epicTaskMap.isEmpty());
    }

    @Test
    void shouldReturnTrueWhenPartEpicTaskMapIsEmptyAfterUsingRemoveAllPartEpicTaskInMemoryTaskManager() {
        //Given
        EpicTask epicTask = new EpicTask("", "");
        PartEpicTask partEpicTask1 = new PartEpicTask("", "", epicTask.getId());
        PartEpicTask partEpicTask2 = new PartEpicTask("", "", epicTask.getId());
        PartEpicTask partEpicTask3 = new PartEpicTask("", "", epicTask.getId());
        partEpicTaskMap.put(partEpicTask1.getId(), partEpicTask1);
        partEpicTaskMap.put(partEpicTask2.getId(), partEpicTask2);
        partEpicTaskMap.put(partEpicTask3.getId(), partEpicTask3);
        //When
        inMemoryTaskManager.removeAllPartEpicTask();
        //Then
        Assertions.assertTrue(partEpicTaskMap.isEmpty());
    }

    @Test
    void shouldReturnTrueWhenPartEpicTaskMapIsEmptyAfterUsingRemoveAllEpicTaskInMemoryTaskManager() {
        //Given
        EpicTask epicTask = new EpicTask("", "");
        epicTaskMap.put(epicTask.getId(), epicTask);

        PartEpicTask partEpicTask1 = new PartEpicTask("", "", epicTask.getId());
        PartEpicTask partEpicTask2 = new PartEpicTask("", "", epicTask.getId());
        PartEpicTask partEpicTask3 = new PartEpicTask("", "", epicTask.getId());
        partEpicTaskMap.put(partEpicTask1.getId(), partEpicTask1);
        partEpicTaskMap.put(partEpicTask2.getId(), partEpicTask2);
        partEpicTaskMap.put(partEpicTask3.getId(), partEpicTask3);
        //When
        inMemoryTaskManager.removeAllEpicTask();
        //Then
        Assertions.assertTrue(partEpicTaskMap.isEmpty());
    }

    @Test
    void shouldReturnTrueWhenGetSimpleTaskByIdReturnCorrectValue() {
        //Given
        SimpleTask simpleTaskExample = new SimpleTask("", "");
        SimpleTask simpleTask2 = new SimpleTask("", "");
        SimpleTask simpleTask3 = new SimpleTask("", "");
        simpleTaskMap.put(simpleTaskExample.getId(), simpleTaskExample);
        simpleTaskMap.put(simpleTask2.getId(), simpleTask2);
        simpleTaskMap.put(simpleTask3.getId(), simpleTask3);
        //When
        SimpleTask result = inMemoryTaskManager.getSimpleTaskById(simpleTaskExample.getId());
        //Then
        Assertions.assertEquals(simpleTaskExample, result);
    }

    @Test
    void shouldReturnTrueWhenGetEpicTaskByIdReturnCorrectValue() {
        //Given
        EpicTask epicTaskExample = new EpicTask("", "");
        EpicTask epicTask2 = new EpicTask("", "");
        EpicTask epicTask3 = new EpicTask("", "");
        epicTaskMap.put(epicTaskExample.getId(), epicTaskExample);
        epicTaskMap.put(epicTask2.getId(), epicTask2);
        epicTaskMap.put(epicTask3.getId(), epicTask3);
        //When
        EpicTask result = inMemoryTaskManager.getEpicTaskById(epicTaskExample.getId());
        //Then
        Assertions.assertEquals(epicTaskExample, result);

    }

    @Test
    void shouldReturnTrueWhenGetPartEpicTaskByIdReturnCorrectValue() {
        //Given
        EpicTask epicTask = new EpicTask("", "");
        PartEpicTask partEpicTaskExample = new PartEpicTask("", "", epicTask.getId());
        PartEpicTask partEpicTask2 = new PartEpicTask("", "", epicTask.getId());
        PartEpicTask partEpicTask3 = new PartEpicTask("", "", epicTask.getId());
        partEpicTaskMap.put(partEpicTaskExample.getId(), partEpicTaskExample);
        partEpicTaskMap.put(partEpicTask2.getId(), partEpicTask2);
        partEpicTaskMap.put(partEpicTask3.getId(), partEpicTask3);
        //When
        PartEpicTask result = inMemoryTaskManager.getPartEpicTaskById(partEpicTaskExample.getId());
        //Then
        Assertions.assertEquals(partEpicTaskExample, result);
    }

    @Test
    void ShouldReturnTrueWhenMakeNewSimpleTaskMakesCorrectValue() {
        //Given
        SimpleTask example = new SimpleTask("test", "test");
        //When
        inMemoryTaskManager.makeNewSimpleTask(example);
        //Then
        Assertions.assertTrue(simpleTaskMap.containsValue(example));
    }

    @Test
    void ShouldReturnTrueWhenMakeNewEpicTaskMakesCorrectValue() {
        //Given
        EpicTask example = new EpicTask("test", "test");
        //When
        inMemoryTaskManager.makeNewEpicTask(example);
        //Then
        Assertions.assertTrue(epicTaskMap.containsValue(example));
    }

    @Test
    void ShouldReturnTrueWhenMakeNewPartEpicTaskMakesCorrectValue() {
        //Given
        EpicTask epicTask = new EpicTask("test", "test");
        epicTaskMap.put(epicTask.getId(), epicTask);
        PartEpicTask example = new PartEpicTask("test", "test", epicTask.getId());
        //When
        inMemoryTaskManager.makeNewPartEpicTask(example);
        //Then
        Assertions.assertTrue(partEpicTaskMap.containsValue(example));
    }

    @Test
    void ShouldReturnTrueWhenUpdateSimpleTaskUpdateTheTaskAndNotMakeNewTasks() {
        //Given
        SimpleTask example = new SimpleTask("", "");
        simpleTaskMap.put(example.getId(), example);
        //When
        inMemoryTaskManager.updateSimpleTask(example);
        SimpleTask result = simpleTaskMap.get(example.getId());
        //Then
        Assertions.assertEquals(example, result);
        Assertions.assertEquals(1, simpleTaskMap.size());
    }

    @Test
    void ShouldReturnTrueWhenUpdateEpicTaskUpdateTheTaskAndNotMakeNewTasks() {
        //Given
        EpicTask example = new EpicTask("", "");
        epicTaskMap.put(example.getId(), example);
        //When
        inMemoryTaskManager.updateEpicTask(example);
        EpicTask result = epicTaskMap.get(example.getId());
        //Then
        Assertions.assertEquals(example, result);
        Assertions.assertEquals(1, epicTaskMap.size());
    }

    @Test
    void ShouldReturnTrueWhenUpdatePartEpicTaskUpdateTheTaskAndNotMakeNewTasks() {
        //Given
        EpicTask epicTask = new EpicTask("", "");
        epicTaskMap.put(epicTask.getId(), epicTask);
        PartEpicTask example = new PartEpicTask("", "", epicTask.getId());
        partEpicTaskMap.put(example.getId(), example);
        //When
        inMemoryTaskManager.updatePartEpicTask(example);
        PartEpicTask result = partEpicTaskMap.get(example.getId());
        //Then
        Assertions.assertEquals(example, result);
        Assertions.assertEquals(1, partEpicTaskMap.size());
    }

    @Test
    void ShouldReturnTrueWhenRemoveSimpleTaskByIdRemovedCorrectTask() {
        //Given
        SimpleTask simpleTask = new SimpleTask("", "");
        simpleTaskMap.put(simpleTask.getId(), simpleTask);
        //When
        inMemoryTaskManager.removeSimpleTaskById(simpleTask.getId());
        //Then
        Assertions.assertTrue(simpleTaskMap.isEmpty());
    }

    @Test
    void ShouldReturnTrueWhenRemoveEpicTaskByIdRemovedCorrectTaskAndRemovedAllConnectedPartEpicTask () {
        //Given
        EpicTask epicTask = new EpicTask("", "");
        epicTaskMap.put(epicTask.getId(), epicTask);

        ArrayList<Integer> listPartTaskId = new ArrayList<>();
        try{
            Field fieldEpicTask = epicTask.getClass().getDeclaredField("listPartTaskId");
            fieldEpicTask.setAccessible(true);
            listPartTaskId = (ArrayList<Integer>) fieldEpicTask.get(epicTask);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        PartEpicTask partEpicTask1 = new PartEpicTask("", "", epicTask.getId());
        PartEpicTask partEpicTask2 = new PartEpicTask("", "", epicTask.getId());
        PartEpicTask partEpicTask3 = new PartEpicTask("", "", epicTask.getId());

        listPartTaskId.add(partEpicTask1.getId());
        listPartTaskId.add(partEpicTask2.getId());
        listPartTaskId.add(partEpicTask3.getId());

        partEpicTaskMap.put(partEpicTask1.getId(), partEpicTask1);
        partEpicTaskMap.put(partEpicTask2.getId(), partEpicTask2);
        partEpicTaskMap.put(partEpicTask3.getId(), partEpicTask3);

        //When
        inMemoryTaskManager.removeEpicTaskById(epicTask.getId());

        //Then
        Assertions.assertTrue(epicTaskMap.isEmpty());
        Assertions.assertTrue(partEpicTaskMap.isEmpty());
    }

    @Test
    void ShouldReturnTrueWhenRemovePartEpicTaskByIdRemovedCorrectTaskFromPartEpicTaskMapAndFromListOfItsEpic() {
        //Given
        EpicTask epicTask = new EpicTask("", "");
        epicTaskMap.put(epicTask.getId(), epicTask);

        ArrayList<Integer> listPartTaskId = new ArrayList<>();
        try{
            Field fieldEpicTask = epicTask.getClass().getDeclaredField("listPartTaskId");
            fieldEpicTask.setAccessible(true);
            listPartTaskId = (ArrayList<Integer>) fieldEpicTask.get(epicTask);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        PartEpicTask partEpicTask = new PartEpicTask("", "", epicTask.getId());

        listPartTaskId.add(partEpicTask.getId());

        partEpicTaskMap.put(partEpicTask.getId(), partEpicTask);

        //When
        inMemoryTaskManager.removePartEpicTaskById(partEpicTask.getId());

        //Then
        Assertions.assertTrue(partEpicTaskMap.isEmpty());
        Assertions.assertTrue(listPartTaskId.isEmpty());
    }

    @Test
    void ShouldReturnTrueWhenGetListOfAllPartEpicTaskExactEpicReturnCorrectList() {
        //Given
        EpicTask epicTask = new EpicTask("", "");
        epicTaskMap.put(epicTask.getId(), epicTask);

        ArrayList<Integer> listPartTaskId = new ArrayList<>();
        try{
            Field fieldEpicTask = epicTask.getClass().getDeclaredField("listPartTaskId");
            fieldEpicTask.setAccessible(true);
            listPartTaskId = (ArrayList<Integer>) fieldEpicTask.get(epicTask);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        PartEpicTask partEpicTask1 = new PartEpicTask("", "", epicTask.getId());
        PartEpicTask partEpicTask2 = new PartEpicTask("", "", epicTask.getId());

        listPartTaskId.add(partEpicTask1.getId());
        listPartTaskId.add(partEpicTask2.getId());

        partEpicTaskMap.put(partEpicTask1.getId(), partEpicTask1);
        partEpicTaskMap.put(partEpicTask2.getId(), partEpicTask2);

        ArrayList<PartEpicTask> example = new ArrayList<>();
        example.add(partEpicTask1);
        example.add(partEpicTask2);

        //When
        ArrayList<PartEpicTask> result = inMemoryTaskManager.getListOfAllPartEpicTaskExactEpic(epicTask);

        //Then
        Assertions.assertEquals(example, result);
    }
}