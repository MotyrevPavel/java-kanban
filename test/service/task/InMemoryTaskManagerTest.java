package service.task;

import exceptions.ScheduleConflictException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.EpicTask;
import model.PartEpicTask;
import model.SimpleTask;
import util.TaskStatus;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

class InMemoryTaskManagerTest {
    private Map<Integer, SimpleTask> simpleTaskMap;
    private Map<Integer, EpicTask> epicTaskMap;
    private Map<Integer, PartEpicTask> partEpicTaskMap;
    InMemoryTaskManager inMemoryTaskManager;

    @BeforeEach
    public void setUp() throws IOException {
        inMemoryTaskManager = new InMemoryTaskManager();
        try {
            Field fieldSimpleTaskMap = inMemoryTaskManager.getClass().getDeclaredField("simpleTaskMap");
            fieldSimpleTaskMap.setAccessible(true);
            simpleTaskMap = (Map<Integer, SimpleTask>) fieldSimpleTaskMap.get(inMemoryTaskManager);

            Field fieldEpicTaskMap = inMemoryTaskManager.getClass().getDeclaredField("epicTaskMap");
            fieldEpicTaskMap.setAccessible(true);
            epicTaskMap = (Map<Integer, EpicTask>) fieldEpicTaskMap.get(inMemoryTaskManager);

            Field fieldPartEpicTaskMap = inMemoryTaskManager.getClass().getDeclaredField("partEpicTaskMap");
            fieldPartEpicTaskMap.setAccessible(true);
            partEpicTaskMap = (Map<Integer, PartEpicTask>) fieldPartEpicTaskMap.get(inMemoryTaskManager);
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
    void shouldReturnTrueWhenGetSimpleTaskByIdReturnCorrectValue() throws CloneNotSupportedException {
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
    void shouldReturnTrueWhenGetEpicTaskByIdReturnCorrectValue() throws CloneNotSupportedException {
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
    void shouldReturnTrueWhenGetPartEpicTaskByIdReturnCorrectValue() throws CloneNotSupportedException {
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
    void ShouldReturnTrueWhenRemoveEpicTaskByIdRemovedCorrectTaskAndRemovedAllConnectedPartEpicTask() {
        //Given
        EpicTask epicTask = new EpicTask("", "");
        epicTaskMap.put(epicTask.getId(), epicTask);

        List<Integer> listPartTaskId = new ArrayList<>();
        try {
            Field fieldEpicTask = epicTask.getClass().getDeclaredField("listPartTaskId");
            fieldEpicTask.setAccessible(true);
            listPartTaskId = (List<Integer>) fieldEpicTask.get(epicTask);
        } catch (Exception e) {
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
        PartEpicTask partEpicTask = new PartEpicTask("", "", epicTask.getId());
        partEpicTaskMap.put(partEpicTask.getId(), partEpicTask);

        //When
        inMemoryTaskManager.removePartEpicTaskById(partEpicTask.getId());

        //Then
        Assertions.assertTrue(partEpicTaskMap.isEmpty());
        Assertions.assertTrue(epicTaskMap.get(epicTask.getId()).getListPartTaskId().isEmpty());
    }

    @Test
    void ShouldReturnTrueWhenGetListOfAllPartEpicTaskExactEpicReturnCorrectList() {
        //Given
        EpicTask epicTask = new EpicTask("", "");
        epicTaskMap.put(epicTask.getId(), epicTask);

        List<Integer> listPartTaskId = new ArrayList<>();
        try {
            Field fieldEpicTask = epicTask.getClass().getDeclaredField("listPartTaskId");
            fieldEpicTask.setAccessible(true);
            listPartTaskId = (List<Integer>) fieldEpicTask.get(epicTask);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        PartEpicTask partEpicTask1 = new PartEpicTask("", "", epicTask.getId());
        PartEpicTask partEpicTask2 = new PartEpicTask("", "", epicTask.getId());

        listPartTaskId.add(partEpicTask1.getId());
        listPartTaskId.add(partEpicTask2.getId());

        partEpicTaskMap.put(partEpicTask1.getId(), partEpicTask1);
        partEpicTaskMap.put(partEpicTask2.getId(), partEpicTask2);

        List<PartEpicTask> example = new ArrayList<>();
        example.add(partEpicTask1);
        example.add(partEpicTask2);

        //When
        List<PartEpicTask> result = inMemoryTaskManager.getEpicSubtasks(epicTask);

        //Then
        Assertions.assertEquals(example, result);
    }

    @Test
    void ShouldReturnTrueWhenEpicTaskHasStatusNew() {
        //Given
        EpicTask epicTask = new EpicTask("", "");
        PartEpicTask partEpicTask1 = new PartEpicTask("", "", epicTask.getId());
        PartEpicTask partEpicTask2 = new PartEpicTask("", "", epicTask.getId());
        PartEpicTask partEpicTask3 = new PartEpicTask("", "", epicTask.getId());

        //When
        inMemoryTaskManager.makeNewEpicTask(epicTask);
        inMemoryTaskManager.makeNewPartEpicTask(partEpicTask1);
        inMemoryTaskManager.makeNewPartEpicTask(partEpicTask2);
        inMemoryTaskManager.makeNewPartEpicTask(partEpicTask3);

        //Then
        Assertions.assertEquals(TaskStatus.NEW, inMemoryTaskManager.getEpicTaskById(epicTask.getId()).getStatus());
    }

    @Test
    void ShouldReturnTrueWhenEpicTaskHasStatusDone() {
        //Given
        EpicTask epicTask = new EpicTask("", "");
        PartEpicTask subTask1 = new PartEpicTask(2001, "subTask1", "subTask1",
                TaskStatus.DONE, null, null, epicTask.getId());
        PartEpicTask subTask2 = new PartEpicTask(2002, "subTask2", "subTask2",
                TaskStatus.DONE, null, null, epicTask.getId());
        PartEpicTask subTask3 = new PartEpicTask(2003, "subTask3", "subTask3",
                TaskStatus.DONE, null, null, epicTask.getId());

        //When
        inMemoryTaskManager.makeNewEpicTask(epicTask);
        inMemoryTaskManager.makeNewPartEpicTask(subTask1);
        inMemoryTaskManager.makeNewPartEpicTask(subTask2);
        inMemoryTaskManager.makeNewPartEpicTask(subTask3);

        //Then
        Assertions.assertEquals(TaskStatus.DONE, inMemoryTaskManager.getEpicTaskById(epicTask.getId()).getStatus());
    }

    @Test
    void ShouldReturnTrueWhenEpicTaskHasStatusInProgress() {
        //Given
        EpicTask epicTask = new EpicTask("", "");
        PartEpicTask subTask1 = new PartEpicTask(2001, "subTask1", "subTask1",
                TaskStatus.NEW, null, null, epicTask.getId());
        PartEpicTask subTask2 = new PartEpicTask(2002, "subTask2", "subTask2",
                TaskStatus.DONE, null, null, epicTask.getId());
        PartEpicTask subTask3 = new PartEpicTask(2003, "subTask3", "subTask3",
                TaskStatus.DONE, null, null, epicTask.getId());

        //When
        inMemoryTaskManager.makeNewEpicTask(epicTask);
        inMemoryTaskManager.makeNewPartEpicTask(subTask1);
        inMemoryTaskManager.makeNewPartEpicTask(subTask2);
        inMemoryTaskManager.makeNewPartEpicTask(subTask3);

        //Then
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, inMemoryTaskManager.getEpicTaskById(epicTask.getId()).getStatus());
    }

    @Test
    void ShouldReturnTrueWhenEpicTaskHasStatusInProgressAndAllSubTaskInProgress() {
        //Given
        EpicTask epicTask = new EpicTask("", "");
        PartEpicTask subTask1 = new PartEpicTask(2001, "subTask1", "subTask1",
                TaskStatus.IN_PROGRESS, null, null, epicTask.getId());
        PartEpicTask subTask2 = new PartEpicTask(2002, "subTask2", "subTask2",
                TaskStatus.IN_PROGRESS, null, null, epicTask.getId());
        PartEpicTask subTask3 = new PartEpicTask(2003, "subTask3", "subTask3",
                TaskStatus.IN_PROGRESS, null, null, epicTask.getId());

        //When
        inMemoryTaskManager.makeNewEpicTask(epicTask);
        inMemoryTaskManager.makeNewPartEpicTask(subTask1);
        inMemoryTaskManager.makeNewPartEpicTask(subTask2);
        inMemoryTaskManager.makeNewPartEpicTask(subTask3);

        //Then
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, inMemoryTaskManager.getEpicTaskById(epicTask.getId()).getStatus());
    }

    @Test
    void ShouldReturnTrueWhenEpicTaskHasEndTimeIsSumOfDurationAllSubTask() {
        //Given
        LocalDateTime ldt = LocalDateTime.now();
        Duration duration = Duration.ofMinutes(100);
        EpicTask epicTask = new EpicTask("", "");
        PartEpicTask subTask1 = new PartEpicTask(2001, "subTask1", "subTask1",
                TaskStatus.IN_PROGRESS, ldt, duration, epicTask.getId());
        PartEpicTask subTask2 = new PartEpicTask(2002, "subTask2", "subTask2",
                TaskStatus.IN_PROGRESS, ldt.plusMinutes(duration.toMinutes()), duration, epicTask.getId());
        PartEpicTask subTask3 = new PartEpicTask(2003, "subTask3", "subTask3",
                TaskStatus.IN_PROGRESS, ldt.plusMinutes(duration.toMinutes() * 2), duration, epicTask.getId());

        //When
        inMemoryTaskManager.makeNewEpicTask(epicTask);
        inMemoryTaskManager.makeNewPartEpicTask(subTask1);
        inMemoryTaskManager.makeNewPartEpicTask(subTask2);
        inMemoryTaskManager.makeNewPartEpicTask(subTask3);

        //Then
        Assertions.assertEquals(ldt.plusMinutes(duration.toMinutes() * 3),
                inMemoryTaskManager.getEpicTaskById(epicTask.getId()).getEndTime().get());
    }

    @Test
    void ShouldReturnTrueWhenEpicTaskHasCorrectEndTime() {
        //Given
        LocalDateTime ldt = LocalDateTime.now();
        Duration duration = Duration.ofMinutes(100);
        EpicTask epicTask = new EpicTask("", "");
        PartEpicTask subTask1 = new PartEpicTask(2001, "subTask1", "subTask1",
                TaskStatus.IN_PROGRESS, ldt, duration, epicTask.getId());
        PartEpicTask subTask2 = new PartEpicTask(2002, "subTask2", "subTask2",
                TaskStatus.IN_PROGRESS, null, null, epicTask.getId());
        PartEpicTask subTask3 = new PartEpicTask(2003, "subTask3", "subTask3",
                TaskStatus.IN_PROGRESS, ldt.plusMinutes(duration.toMinutes()), duration, epicTask.getId());

        //When
        inMemoryTaskManager.makeNewEpicTask(epicTask);
        inMemoryTaskManager.makeNewPartEpicTask(subTask1);
        inMemoryTaskManager.makeNewPartEpicTask(subTask2);
        inMemoryTaskManager.makeNewPartEpicTask(subTask3);

        //Then
        Assertions.assertEquals(ldt.plusMinutes(duration.toMinutes() * 2),
                inMemoryTaskManager.getEpicTaskById(epicTask.getId()).getEndTime().get());
    }

    @Test
    void ShouldThrowScheduleConflictExceptionWhenTasksOverlapInTime() {
        //Given
        LocalDateTime ldt = LocalDateTime.now();
        Duration duration = Duration.ofMinutes(100);
        EpicTask epicTask = new EpicTask("", "");
        PartEpicTask subTask1 = new PartEpicTask(2001, "subTask1", "subTask1",
                TaskStatus.NEW, ldt, duration, epicTask.getId());
        PartEpicTask subTask2 = new PartEpicTask(2002, "subTask2", "subTask2",
                TaskStatus.NEW, ldt.plusMinutes(duration.toMinutes()), duration, epicTask.getId());
        PartEpicTask subTask3 = new PartEpicTask(2003, "subTask3", "subTask3",
                TaskStatus.NEW, ldt.plusMinutes(duration.toMinutes()), duration, epicTask.getId());

        //When
        inMemoryTaskManager.makeNewEpicTask(epicTask);
        inMemoryTaskManager.makeNewPartEpicTask(subTask1);
        inMemoryTaskManager.makeNewPartEpicTask(subTask2);

        //Then
        Assertions.assertThrows(ScheduleConflictException.class,
                () -> inMemoryTaskManager.makeNewPartEpicTask(subTask3));
    }

    @Test
    void ShouldReturnTrueWhenPrioritizedTaskListIsCorrect() {
        //Given
        LocalDateTime ldt = LocalDateTime.now();
        Duration duration = Duration.ofMinutes(100);
        EpicTask epicTask = new EpicTask("", "");
        PartEpicTask subTask1 = new PartEpicTask(2001, "subTask1", "subTask1",
                TaskStatus.NEW, ldt, duration, epicTask.getId());
        PartEpicTask subTask2 = new PartEpicTask(2002, "subTask2", "subTask2",
                TaskStatus.NEW, null, null, epicTask.getId());
        PartEpicTask subTask3 = new PartEpicTask(2003, "subTask3", "subTask3",
                TaskStatus.NEW, ldt.plusMinutes(duration.toMinutes()), duration, epicTask.getId());
        Comparator<SimpleTask> comparator = Comparator.comparing(task -> task.getStartTime().get());
        comparator = comparator.thenComparing(SimpleTask::getId);
        Set<SimpleTask> prioritizedTaskList = new TreeSet<>(comparator);
        prioritizedTaskList.add(subTask1);
        prioritizedTaskList.add(subTask3);


        //When
        inMemoryTaskManager.makeNewEpicTask(epicTask);
        inMemoryTaskManager.makeNewPartEpicTask(subTask1);
        inMemoryTaskManager.makeNewPartEpicTask(subTask2);
        inMemoryTaskManager.makeNewPartEpicTask(subTask3);

        //Then
        Assertions.assertEquals(prioritizedTaskList, inMemoryTaskManager.getPrioritizedTasks());
    }
}