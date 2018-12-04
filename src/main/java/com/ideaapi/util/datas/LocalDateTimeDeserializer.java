package com.ideaapi.util.datas;

import java.io.IOException;
import java.time.LocalDateTime;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * @Created by: Alex Alves de Souza Classe responsável por setar como a data no formate DateTime de
 *          LocalDateTime será deserializada no json.
 */
public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

  @Override
  public LocalDateTime deserialize(
          JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    String data = jp.readValueAs(String.class);
    if (data != null && !data.isEmpty()) {
      return LocalDateTime.parse(data);
    }
    return null;
  }
}
