package com.ideaapi.util.datas;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * @Created by: Alex Alves de Souza Classe responsável por setar como a data no formate DateTime de LocalDate será
 * deserializada no json.
 */
public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {

        String data = jp.readValueAs(String.class);

        if (data != null && !data.isEmpty()) {
            LocalDate date = null;
            try {
                date = LocalDate.parse(data);
            } catch (DateTimeParseException e) {
                date = UtilsData.getDateFormatted(data, "/");
            } catch (Exception e) {
                System.out.println(e);
            }
            return date;
        }
        return null;
    }

}
