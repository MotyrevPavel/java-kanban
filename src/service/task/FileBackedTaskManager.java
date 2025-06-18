package service.task;

import exceptions.ManagerLoadException;
import exceptions.ManagerSaveException;
import model.EpicTask;
import model.PartEpicTask;
import model.SimpleTask;
import util.TaskStatus;
import util.TaskType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    //При изменении заголовка таблицы, нужно проверить метод fromString, т.к. массив
    //этого метода жестко привязан к порядку элементов заголовка таблицы
    private final String tableTitle = "id,type,name,status,description,epic";
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
        checkFileExist();
        loadFromFile(this.file);
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {

            writer.write(tableTitle);
            writer.newLine();

            for (SimpleTask value : simpleTaskMap.values()) {
                writer.write(value.toString());
                writer.newLine();
            }
            for (SimpleTask value : epicTaskMap.values()) {
                writer.write(value.toString());
                writer.newLine();
            }
            for (SimpleTask value : partEpicTaskMap.values()) {
                writer.write(value.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    public void loadFromFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();
            while (reader.ready()) {
                String value = reader.readLine();
                SimpleTask task = fromString(value);
                if (task instanceof PartEpicTask) {
                    partEpicTaskMap.put(task.getId(), (PartEpicTask) task);
                } else if (task instanceof EpicTask) {
                    epicTaskMap.put(task.getId(), (EpicTask) task);
                } else {
                    simpleTaskMap.put(task.getId(), task);
                }
            }
        } catch (IOException e) {
            throw new ManagerLoadException();
        }
    }

    @Override
    public void removeAllSimpleTask() {
        super.removeAllSimpleTask();
        save();
    }

    @Override
    public void removeAllEpicTask() {
        super.removeAllEpicTask();
        save();
    }

    @Override
    public void removeAllPartEpicTask() {
        super.removeAllPartEpicTask();
        save();
    }

    @Override
    public void makeNewSimpleTask(SimpleTask simpleTask) {
        super.makeNewSimpleTask(simpleTask);
        save();
    }

    @Override
    public void makeNewEpicTask(EpicTask epicTask) {
        super.makeNewEpicTask(epicTask);
        save();
    }

    @Override
    public void makeNewPartEpicTask(PartEpicTask partEpicTask) {
        super.makeNewPartEpicTask(partEpicTask);
        save();
    }

    @Override
    public void updateSimpleTask(SimpleTask simpleTask) {
        super.updateSimpleTask(simpleTask);
        save();
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) {
        super.updateEpicTask(epicTask);
        save();
    }

    @Override
    public void updatePartEpicTask(PartEpicTask partEpicTask) {
        super.updatePartEpicTask(partEpicTask);
        save();
    }

    @Override
    public void removeSimpleTaskById(int id) {
        super.removeSimpleTaskById(id);
        save();
    }

    @Override
    public void removeEpicTaskById(int id) {
        super.removeEpicTaskById(id);
        save();
    }

    @Override
    public void removePartEpicTaskById(int id) {
        super.removePartEpicTaskById(id);
        save();
    }

    private void checkFileExist() {
        Path filePath = file.toPath();
        try {
            if (!Files.isRegularFile(filePath)) {
                Files.createFile(filePath);
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private SimpleTask fromString(String value) {
        //Порядок данных в массиве "id,type,name,status,description,idConnectEpicTask";
        String[] taskParams = value.split(",");
        int id = Integer.parseInt(taskParams[0]);
        TaskType type = TaskType.valueOf(taskParams[1]);
        String name = taskParams[2];
        TaskStatus status = TaskStatus.valueOf(taskParams[3]);
        String description = taskParams[4];
        int idConnectEpicTask = type == TaskType.PARTEPIC ? Integer.parseInt(taskParams[5]) : -1;

        if (type == TaskType.SIMPLE) {
            return new SimpleTask(id, name, status, description);
        } else if (type == TaskType.EPIC) {
            return new EpicTask(id, name, status, description);
        } else {
            return new PartEpicTask(id, name, status, description, idConnectEpicTask);
        }
    }
}