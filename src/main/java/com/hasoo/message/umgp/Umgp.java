package com.hasoo.message.umgp;

import java.util.Optional;
import lombok.Data;

@Data
public class Umgp {

  /* @formatter:off */
  public static final String BEGIN = "BEGIN";
  public static final String CONNECT = "CONNECT";
  public static final String SEND = "SEND";
  public static final String MMS = "MMS";
  public static final String REPORT = "REPORT";
  public static final String PING = "PING";
  public static final String PONG = "PONG";
  public static final String ACK = "ACK";
  public static final String END = "END";

  public static final String ID = "ID";
  public static final String PASSWORD = "PASSWORD";
  public static final String REPORTLINE = "REPORTLINE";
  public static final String VERSION = "VERSION";
  public static final String SUBJECT = "SUBJECT";
  public static final String RECEIVERNUM = "RECEIVERNUM";
  public static final String CALLBACK = "CALLBACK";
  public static final String CODE = "CODE";
  public static final String DATA = "DATA";
  public static final String KEY = "KEY";
  public static final String DATE = "DATE";
  public static final String NET = "NET";
  public static final String IMAGE = "IMAGE";
  public static final String CONTENTTYPE = "CONTENTTYPE";
  /* @formatter:on */
  private String id;
  private String password;
  private String reportline;
  private String version;
  private String subject;
  private String receivernum;
  private String callback;
  private StringBuffer data = new StringBuffer();
  private String key;
  private String date;
  private String net;
  private String code;
  private StringBuffer image = new StringBuffer();
  private String contenttype;
  private String agent;
  private String ttl;
  private boolean completedBegin = false;
  private boolean completedEnd = false;
  private HType headerType;

  public static String headerPart(String part) {
    return Umgp.BEGIN + " " + part + "\r\n";
  }

  public static String dataPart(String field, String value) {
    return field + ":" + value + "\r\n";
  }

  public static String end() {
    return Umgp.END + "\r\n";
  }

  public void reset() {
    id = "";
    password = "";
    reportline = "";
    version = "";
    subject = "";
    receivernum = "";
    callback = "";
    data.setLength(0);
    key = "";
    date = "";
    net = "";
    code = "";
    image.setLength(0);
    contenttype = "";
    agent = "";
    ttl = "";

    completedBegin = false;
    completedEnd = false;
    headerType = null;
  }

  /**
   * @param buf line packet
   * @return if received BEGIN part, true and not false
   */
  public boolean parseHeaderPart(String buf) {
    if (completedBegin) {
      return false;
    }

    String[] headerPart = buf.split(" ");
    if (2 != headerPart.length) {
      throw new RuntimeException(String.format("invalid packet[%s]", buf));
    }

    String umgpSeperator = headerPart[0], umgpMethod = headerPart[1];
    if (!umgpSeperator.equals(BEGIN)) {
      throw new RuntimeException(String.format("invalid packet[%s]", buf));
    }

    headerType = Optional.ofNullable(getHeaderType(umgpMethod))
        .orElseThrow(() -> new RuntimeException(String.format("invalid packet[%s]", buf)));

    completedBegin = true;

    return true;
  }

  private HType getHeaderType(String data) {
    switch (data) {
      case CONNECT:
        return HType.CONNECT;
      case SEND:
        return HType.SEND;
      case REPORT:
        return HType.REPORT;
      case MMS:
        return HType.MMS;
      case PING:
        return HType.PING;
      case PONG:
        return HType.PONG;
      case ACK:
        return HType.ACK;
    }
    return null;
  }

  /**
   * @param buf line packet
   * @return if received END part, true and not false
   */
  public boolean parseDataPart(String buf) {
    if (buf.equals(END)) {
      if (0 < data.length()) {
        data.deleteCharAt(data.length() - 1);
      }
      completedEnd = true;
      return true;
    }

    String[] dataPart = buf.split(":");
    if (2 != dataPart.length) {
      throw new RuntimeException(String.format("invalid packet[%s]", buf));
    }

    saveDataPart(dataPart[0], dataPart[1]);

    return false;
  }

  private void saveDataPart(String field, String value) {
    switch (field) {
      case ID:
        id = value;
        break;
      case PASSWORD:
        password = value;
        break;
      case REPORTLINE:
        if (!value.equals("Y") && !value.equals("N")) {
          throw new RuntimeException(String.format("invalid packet[REPORTLINE:%s]", value));
        }
        reportline = value;
        break;
      case VERSION:
        version = value;
        break;
      case SUBJECT:
        subject = value;
        break;
      case RECEIVERNUM:
        receivernum = value;
        break;
      case CALLBACK:
        callback = value;
        break;
      case CODE:
        code = value;
        break;
      case KEY:
        key = value;
        break;
      case DATE:
        date = value;
        break;
      case NET:
        net = value;
        break;
      case CONTENTTYPE:
        contenttype = value;
        break;
      case DATA:
        data.append(value);
        data.append('\n');
        break;
      case IMAGE:
        image.append(value);
        break;
      default:
        throw new RuntimeException(String.format("invalid packet[%s]", field));
    }
  }

  public enum HType {
    CONNECT, SEND, MMS, REPORT, PING, PONG, ACK
  }
}
