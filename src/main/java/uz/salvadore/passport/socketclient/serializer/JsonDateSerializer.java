package uz.salvadore.passport.socketclient.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JsonDateSerializer  extends JsonSerializer<Date> {

    private final DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

    public JsonDateSerializer() {
    }

    public void serialize(Date date, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(this.formatter.format(date));
    }

}
