package adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public void write(final JsonWriter jsonWriter, final LocalDateTime localDateTime) throws IOException {
        if (localDateTime != null) {
            jsonWriter.value(localDateTime.format(dtf));
        } else {
            jsonWriter.value("null");
        }

    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
        try {
            return LocalDateTime.parse(jsonReader.nextString(), dtf);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

}
