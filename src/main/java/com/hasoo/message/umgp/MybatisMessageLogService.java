package com.hasoo.message.umgp;

import com.hasoo.message.dto.MessageLog;
import com.hasoo.message.mapper.MessageLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MybatisMessageLogService implements MessageLogService {

  @Autowired
  private MessageLogMapper messageLogMapper;

  @Override
  public void save(MessageLog messageLog) {
    messageLogMapper.save(messageLog);
  }
}
