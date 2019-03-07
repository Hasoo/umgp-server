package com.hasoo.message;

import com.hasoo.message.dto.MessageLog;
import com.hasoo.message.mapper.MessageLogMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@MapperScan(basePackageClasses = MessageLogMapper.class)
@ActiveProfiles("test")
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MessageLogMapperTest {

  @Autowired
  MessageLogMapper messageLogMapper;

  @Test
  public void testSave() {
    MessageLog messageLog = MessageLog.builder().userKey("1").build();
    messageLogMapper.save(messageLog);

    Assert.assertEquals(messageLog.getUserKey(),
        messageLogMapper.findByMsgKey(messageLog.getMsgKey()).getUserKey());
  }
}