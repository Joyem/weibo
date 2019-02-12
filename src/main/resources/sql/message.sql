DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`
(
  `id`              INT         NOT NULL AUTO_INCREMENT,
  `from_id`         INT NULL,
  `to_id`           INT NULL,
  `content`         TEXT NULL,
  `create_time`     TIMESTAMP NULL,
  `has_read`        INT NULL,
  `conversation_id` VARCHAR(64) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX             `CREATE_TIME` (`create_time` ASC),
  INDEX             `CONVERSATION_INDEX` (`conversation_id` ASC)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;