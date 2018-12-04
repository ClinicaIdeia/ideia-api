package com.ideaapi.util.datas;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @Created by Alex Alves de Souza Classe responsável por setar como a data no formato DateTime de
 *          LocalDateTime será serializada no json.
 */
public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

  public static final String PATTERN = "yyyy/MM/dd HH:mm:ss";

  @Override
  public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
    if (value != null) {
      gen.writeString(value.format(DateTimeFormatter.ofPattern(PATTERN)));
    } else {
      gen.writeNull();
    }
  }

}
