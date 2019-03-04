CREATE TABLE IF NOT EXISTS `message_log` (
  `msg_key` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `user_key` varchar(40) NOT NULL,
  `msg_type` varchar(3) NULL,
  `phone` varchar(20) NULL,
  `callback` varchar(20) NULL,
  `message` varchar(4000) NULL,
  `status` int NULL,
  `res_date` datetime NULL,
  `sent_date` datetime NULL,
  `done_date` datetime NULL,
  `report_date` datetime NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;