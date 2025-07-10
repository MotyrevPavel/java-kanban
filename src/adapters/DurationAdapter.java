package adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationAdapter extends TypeAdapter<Duration> {

    @Override
    public void write(final JsonWriter jsonWriter, final Duration value) throws IOException {
        if (value != null) {
            jsonWriter.value(value.toMinutes());
        } else {
            jsonWriter.value("null");
        }

    }

    @Override
    public Duration read(final JsonReader jsonReader) throws IOException {
        String s = jsonReader.nextString();
        if (s.equals("null")) {
            return null;
        } else {
            return Duration.ofMinutes(Long.parseLong(s));
        }
    }

}
