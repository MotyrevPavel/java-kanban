package exceptions;

/**
 * Исключение возникает при пересечении временного интервала задач.
 */

public class ScheduleConflictException extends RuntimeException {
    /**
     * Создает новое исключение с указанным сообщением.
     *
     * @param message описание ошибки
     */
    public ScheduleConflictException(String message) {
        super(message);
    }
}
