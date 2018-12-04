package com.ideaapi.util.datas;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public abstract class UtilsData {

  public static String getDateTimeShort(final LocalDateTime agora) {

    DateTimeFormatter formatador = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                                                    .withLocale(new Locale("pt",
                                                                           "br"));
    String[] format = agora.format(formatador)
                           .split(" "); // 08/04/14 10:02

    return format[0];
  }

  public static LocalDateTime getDateTimeFormatted(final String dateTime, final String split) {

    String[] dataQuebrada = dateTime.split(split);
    String dia = dataQuebrada[0];
    String mes = dataQuebrada[1];
    String ano = dataQuebrada[2];
    String horaMinut = "00";

    return LocalDateTime.of(Integer.parseInt(ano),
                            Integer.parseInt(mes),
                            Integer.parseInt(dia),
                            Integer.parseInt(horaMinut),
                            Integer.parseInt(horaMinut));
  }

  public static LocalDate getDateFormatted(final String dateTime, final String split) {

    String[] dataQuebrada = dateTime.split(split);
    String dia = dataQuebrada[0];
    String mes = dataQuebrada[1];
    String ano = dataQuebrada[2];

    return LocalDate.of(Integer.parseInt(ano), Integer.parseInt(mes), Integer.parseInt(dia));
  }

  public static String getLocalDateTimeToString(final LocalDateTime now, final String locale, final String patthern) {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String formatDateTime = now.format(formatter);
    String split = "/";
    String dateResult = null;

    if ("US".equalsIgnoreCase(locale)) {
      split = "-";
    }

    if ("BR".equals(locale)) {
      split = "/";
    }

    final String[] dateTimeArray = formatDateTime.split(split);
    final String dia = dateTimeArray[2];
    final String mes = dateTimeArray[1];
    final String ano = dateTimeArray[0];

    dateResult = patthern.replace("dd", dia)
                         .replaceAll("mm", mes)
                         .replaceAll("yyyy", ano);

    return dateResult;
  }

  public static String getLocalDateToString(final LocalDate now, final String locale, final String patthern) {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String formatDateTime = now.format(formatter);
    String split = "/";
    String dateResult = null;

    if ("US".equalsIgnoreCase(locale)) {
      split = "-";
    }

    if ("BR".equals(locale)) {
      split = "/";
    }

    final String[] dateTimeArray = formatDateTime.split(split);
    final String dia = dateTimeArray[2];
    final String mes = dateTimeArray[1];
    final String ano = dateTimeArray[0];

    dateResult = patthern.replace("dd", dia)
                         .replaceAll("mm", mes)
                         .replaceAll("yyyy", ano);

    return dateResult;
  }

  private String reformattDate(String dateFormatt) {
    StringBuffer sb = new StringBuffer();

    String[] formats = dateFormatt.split("-");
    for (int i = 0; i < formats.length; i++) {

      String parteOfDate = formats[i];
      if (parteOfDate.length() == 1) {
        parteOfDate = "0".concat(parteOfDate);
        sb.append(parteOfDate)
          .append("-");
      } else {
        sb.append(parteOfDate)
          .append("-");
      }
    }

    String formattFinal = sb.toString();
    int tamanho = formattFinal.length();
    formattFinal = formattFinal.substring(0, tamanho - 1);

    return formattFinal;
  }

}
