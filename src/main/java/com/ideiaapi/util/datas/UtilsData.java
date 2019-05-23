package com.ideiaapi.util.datas;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
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

  public static String getDataFormatadaPorExtenso() {
    Locale localeBR = new Locale("pt", "BR");
    SimpleDateFormat fmt = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", localeBR);
    return fmt.format(new Date()).toUpperCase();
  }

    public static String getDataFormatadaRecibo(LocalDate emissao) {

        int year = emissao.getYear();
        int month = emissao.getMonthValue();
        int day = emissao.getDayOfMonth();

        String mes = "JANEIRO";

        switch (month) {
            case 1:
                mes = "JANEIRO";
                break;
            case 2:
                mes = "FEVEREIRO";
                break;
            case 3:
                mes = "MARÇO";
                break;
            case 4:
                mes = "ABRIL";
                break;
            case 5:
                mes = "MAIO";
                break;
            case 6:
                mes = "JUNHO";
                break;
            case 7:
                mes = "JULHO";
                break;
            case 8:
                mes = "AGOSTO";
                break;
            case 9:
                mes = "SETEMBRO";
                break;
            case 10:
                mes = "OUTUBRO";
                break;
            case 11:
                mes = "NOVEMBRO";
                break;
            case 12:
                mes = "DEZEMBRO";
                break;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(day).append(" de ")
                .append(mes).append(" de ")
                .append(year);

        return sb.toString();
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

  public static String getDataConvertida(LocalDate data, String pattern) {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

    return data.format(formatter);

  }

}
