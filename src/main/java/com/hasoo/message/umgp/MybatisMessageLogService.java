package com.hasoo.message.umgp;

import com.hasoo.message.dto.MessageLog;
import com.hasoo.message.mapper.MessageLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MybatisMessageLogService implements MessageLogService {

  private MessageLogMapper messageLogMapper;

  @Autowired
  public MybatisMessageLogService(MessageLogMapper messageLogMapper) {
    this.messageLogMapper = messageLogMapper;
  }

  @Override
  public void save(MessageLog messageLog) {

    messageLogMapper.save(messageLog);
  }
}
