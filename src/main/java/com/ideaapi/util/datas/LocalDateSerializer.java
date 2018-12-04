package com.ideaapi.util.datas;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @Created by Alex Alves de Souza Classe responsável por setar como a data no formato DateTime de
 *          LocalDateTime será serializada no json.
 */
public class LocalDateSerializer extends JsonSerializer<LocalDate> {

  public static final String PATTERN = "dd/MM/yyyy";

  @Override
  public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers)
      throws IOException, JsonProcessingException {
    if (value != null) {      
     gen.writeString(value.format(DateTimeFormatter.ofPattern(PATTERN)));
    //  gen.writeString(value.format(DateTimeFormatter.ISO_DATE));
    } else {
      gen.writeNull();
    }
  }

}
