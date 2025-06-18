package service.task;

import model.EpicTask;
import model.PartEpicTask;
import model.SimpleTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends InMemoryTaskManagerTest {
    FileBackedTaskManager fileBackedTaskManager;
    File tempFile;

    @BeforeEach
    public void setUp() throws IOException {
        super.setUp();
        this.tempFile = File.createTempFile("Test", ".csv");
        this.fileBackedTaskManager = new FileBackedTaskManager(tempFile);
    }

    @Test
    void shouldReturnTrueWhenSaveWorksCorrect() throws IOException {
        //Given
        BufferedReader reader = new BufferedReader(new FileReader(tempFile, StandardCharsets.UTF_8));
        SimpleTask simpleTask = new SimpleTask("st1", "st1");
        EpicTask epicTask = new EpicTask("epic1", "epic2");
        PartEpicTask partEpicTask = new PartEpicTask("sub1", "sub1", epicTask.getId());
        fileBackedTaskManager.simpleTaskMap.put(simpleTask.getId(), simpleTask);
        fileBackedTaskManager.epicTaskMap.put(epicTask.getId(), epicTask);
        fileBackedTaskManager.partEpicTaskMap.put(partEpicTask.getId(), partEpicTask);

        //When
        fileBackedTaskManager.save();

        //Then
        assertEquals("id,type,name,status,description,epic", reader.readLine());
        assertEquals(simpleTask.toString(), reader.readLine());
        assertEquals(epicTask.toString(), reader.readLine());
        assertEquals(partEpicTask.toString(), reader.readLine());
        assertNull(reader.readLine());
        reader.close();
    }

    @Test
    void shouldReturnTrueWhenCorrectLoadFromFile() throws IOException {
        //Given
        String title = "id,type,name,status,description,epic";
        String simleTask = "1,SIMPLE,SimpleTask,DONE,simple,SimpleTask";
        String epic = "2,EPIC,epic,NEW,epic,epic";
        String partEpicTask = "3,PARTEPIC,partEpicTask,IN_PROGRESS,partEpicTask,2";
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile, StandardCharsets.UTF_8));
        writer.write(title);
        writer.newLine();
        writer.write(simleTask);
        writer.newLine();
        writer.write(epic);
        writer.newLine();
        writer.write(partEpicTask);
        writer.close();

        //When
        fileBackedTaskManager.loadFromFile(tempFile);

        //Then
        assertEquals(simleTask, fileBackedTaskManager.simpleTaskMap.get(1).toString());
        assertEquals(epic, fileBackedTaskManager.epicTaskMap.get(2).toString());
        assertEquals(partEpicTask, fileBackedTaskManager.partEpicTaskMap.get(3).toString());
    }
}