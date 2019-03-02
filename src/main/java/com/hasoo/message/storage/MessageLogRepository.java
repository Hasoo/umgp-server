package com.hasoo.message.storage;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageLogRepository extends CrudRepository<MessageLog, Long> {
  Optional<MessageLog> findByMsgKey(Long msgKey);

  List<MessageLog> findTop100ByStatus(Integer status);
}
