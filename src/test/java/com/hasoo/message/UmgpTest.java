package com.hasoo.message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.hasoo.message.umgp.Umgp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class UmgpTest {

  private Umgp umgp = new Umgp();

  @Test
  public void testHeader() {
    assertFalse(umgp.isCompletedBegin());
    assertTrue(Umgp.HType.CONNECT == umgp.parseHeaderPart("BEGIN CONNECT"));
    assertTrue(Umgp.HType.SEND == umgp.parseHeaderPart("BEGIN SEND"));
    assertTrue(Umgp.HType.MMS == umgp.parseHeaderPart("BEGIN MMS"));
    assertTrue(Umgp.HType.PING == umgp.parseHeaderPart("BEGIN PING"));
    assertTrue(Umgp.HType.PONG == umgp.parseHeaderPart("BEGIN PONG"));
    assertTrue(Umgp.HType.ACK == umgp.parseHeaderPart("BEGIN ACK"));
    assertTrue(umgp.isCompletedBegin());
  }

  @Test(expected = RuntimeException.class)
  public void testHeaderPartException1() {
    umgp.parseHeaderPart("BGIN CONNECT");
  }

  @Test(expected = RuntimeException.class)
  public void testHeaderPartException2() {
    umgp.parseHeaderPart("BIGINCONNECT");
  }

  @Test(expected = RuntimeException.class)
  public void testDataPartException() {
    umgp.parseDataPart("REPORTLINE:y");
  }

  @Test
  public void testData() {
    assertFalse(umgp.isCompletedEnd());

    umgp.parseDataPart("ID:test");
    assertEquals("test", umgp.getId());

    umgp.parseDataPart("PASSWORD:test123");
    assertEquals("test123", umgp.getPassword());

    umgp.parseDataPart("REPORTLINE:Y");
    assertEquals("Y", umgp.getReportline());

    umgp.parseDataPart("REPORTLINE:N");
    assertEquals("N", umgp.getReportline());

    umgp.parseDataPart("VERSION:UMGP/1.0");
    assertEquals("UMGP/1.0", umgp.getVersion());

    umgp.parseDataPart("SUBJECT:MMSSUBJECT");
    assertEquals("MMSSUBJECT", umgp.getSubject());

    umgp.parseDataPart("RECEIVERNUM:01012341234");
    assertEquals("01012341234", umgp.getReceivernum());

    umgp.parseDataPart("CALLBACK:01022222222");
    assertEquals("01022222222", umgp.getCallback());

    umgp.parseDataPart("CODE:100");
    assertEquals("100", umgp.getCode());

    umgp.parseDataPart("KEY:1");
    assertEquals("1", umgp.getKey());

    umgp.parseDataPart("DATE:20190101091011");
    assertEquals("20190101091011", umgp.getDate());

    umgp.parseDataPart("NET:SKT");
    assertEquals("SKT", umgp.getNet());

    umgp.parseDataPart("CONTENTTYPE:TXT");
    assertEquals("TXT", umgp.getContenttype());

    umgp.parseDataPart("DATA:testmessage");
    assertEquals("testmessage\n", umgp.getData().toString());

    umgp.parseDataPart("DATA:hi");
    assertEquals("testmessage\nhi\n", umgp.getData().toString());

    umgp.parseDataPart("IMAGE:base64");
    assertEquals("base64", umgp.getImage().toString());

    umgp.parseDataPart("END");
    assertTrue(umgp.isCompletedEnd());

    assertEquals("testmessage\nhi", umgp.getData().toString());
  }

  @Test
  public void testCreationPacket() {
    StringBuilder packet = new StringBuilder();
    packet.append(Umgp.headerPart(Umgp.CONNECT));
    packet.append(Umgp.dataPart(Umgp.ID, "test"));
    packet.append(Umgp.dataPart(Umgp.PASSWORD, "testpwd"));
    packet.append(Umgp.dataPart(Umgp.REPORTLINE, "N"));
    packet.append(Umgp.dataPart(Umgp.VERSION, "SMGP/2.0.5"));
    packet.append(Umgp.end());
    assertEquals(
        "BEGIN CONNECT\r\nID:test\r\nPASSWORD:testpwd\r\nREPORTLINE:N\r\nVERSION:SMGP/2.0.5\r\nEND\r\n",
        packet.toString());
  }

}
