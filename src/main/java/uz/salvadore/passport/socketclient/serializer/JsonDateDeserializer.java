package uz.salvadore.passport.socketclient.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JsonDateDeserializer extends JsonDeserializer<Date> {

    private final DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

    public JsonDateDeserializer() {
    }

    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String date = jsonParser.getText();

        try {
            return this.formatter.parse(date);
        } catch (ParseException var5) {
            throw new RuntimeException(var5);
        }
    }

}
