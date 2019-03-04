package com.hasoo.message.mapper;

import com.hasoo.message.storage.MessageLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageLogMapper {
  public void save(MessageLog messageLog);

  public MessageLog findByMsgKey(Long msgKey);
}
