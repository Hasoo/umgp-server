package com.hasoo.message.umgp;

import com.hasoo.message.dto.ClientContext;
import com.hasoo.message.dto.MessageLog;
import com.hasoo.message.dto.ReportQue;
import com.hasoo.message.util.HUtil;
import io.netty.channel.Channel;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class SendLineHandler implements LineHandler {

  private DeliveryRepository deliveryRepository;

  @Autowired
  private MessageLogService mybatisMessageLogService;

  @SuppressWarnings("unused")
  private SendLineHandler() {
  }

  public SendLineHandler(DeliveryRepository deliveryRepository) {
    this.deliveryRepository = deliveryRepository;
  }

  @Override
  public void handle(Channel channel, ClientContext clientContext) {
    Umgp.HType headerType = clientContext.getHeaderType();
    Umgp umgp = clientContext.getUmgp();

    if (Umgp.HType.SEND == headerType) {
      log.debug("-> {} key:{} phone:{} callback:{} message:{}", Umgp.SEND, umgp.getKey(),
          umgp.getReceivernum(), umgp.getCallback(), umgp.getData().toString().replace('\n', '\\'));

      //@formatter:off
      mybatisMessageLogService.save(MessageLog.builder()
          .userKey(umgp.getKey())
          .msgType("SMS")
          .phone(umgp.getReceivernum())
          .callback(umgp.getCallback())
          .message(umgp.getData().toString())
          .resDate(LocalDateTime.now())
          .build()
      );
      //@formatter:on

      createReport(clientContext.getUsername(), umgp.getReceivernum(), umgp.getKey());
      sendAck(channel, umgp.getKey(), "100", "Success");
    } else if (Umgp.HType.MMS == headerType) {
      log.debug("-> {} key:{} phone:{} callback:{}", Umgp.SEND, umgp.getKey(),
          umgp.getReceivernum(), umgp.getCallback());
      createReport(clientContext.getUsername(), umgp.getReceivernum(), umgp.getKey());
      sendAck(channel, umgp.getKey(), "100", "Success");
    } else if (Umgp.HType.PING == headerType) {
      log.debug("-> {} key:{}", Umgp.PING, umgp.getKey());
      sendPong(channel, umgp.getKey());
    } else {
      throw new RuntimeException(String.format("invalid sendline packet -> %s", headerType));
    }
  }

  public void sendAck(Channel channel, String key, String code, String data) {
    log.debug("<- {} key:{} code:{} data:{}", Umgp.ACK, key, code, data);
    StringBuilder packet = new StringBuilder();
    packet.append(Umgp.headerPart(Umgp.ACK));
    packet.append(Umgp.dataPart(Umgp.KEY, key));
    packet.append(Umgp.dataPart(Umgp.CODE, code));
    packet.append(Umgp.dataPart(Umgp.DATA, data));
    packet.append(Umgp.end());
    channel.writeAndFlush(packet.toString());
  }

  public void sendPong(Channel channel, String key) {
    log.debug("<- {} key:{}", Umgp.PONG, key);
    StringBuilder packet = new StringBuilder();
    packet.append(Umgp.headerPart(Umgp.PONG));
    packet.append(Umgp.dataPart(Umgp.KEY, key));
    packet.append(Umgp.end());
    channel.writeAndFlush(packet.toString());
  }

  public void createReport(String username, String phone, String key) {
    String net = "ETC", code = "410", data = "0/Invalid";

    if (11 == phone.length()) {
      int idNumber = Integer.valueOf(phone.substring(0, 3));
      int prefix = Integer.valueOf(phone.substring(3, 7));
      int last2Digit = Integer.valueOf(phone.substring(9, 11));

      log.debug("idNumber:{} prefix:{} last2Digit:{}", idNumber, prefix, last2Digit);

      if (10 == idNumber) {
        if (1000 <= prefix && 4000 > prefix) {
          net = "SKT";
        } else if (4000 <= prefix && 7000 > prefix) {
          net = "KTF";
        } else if (7000 <= prefix && 10000 > prefix) {
          net = "LGT";
        }
      }

      switch (last2Digit) {
        case 0:
          code = "100";
          data = "Success";
          break;
        case 1:
          code = "400";
          data = "0/Failure";
          break;
        case 2:
          code = "410";
          data = "0/Invalid";
          break;
      }
    }

    deliveryRepository.push(username,
        new ReportQue(key, code, data, HUtil.getCurrentDate12(), net));
  }
}
