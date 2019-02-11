DROP TABLE IF EXISTS `login_ticket`;
CREATE TABLE `login_ticket`
(
  `id`      INT          NOT NULL AUTO_INCREMENT,
  `user_id` INT          NOT NULL,
  `ticket`  VARCHAR(128) NOT NULL,
  `expired` TIMESTAMP    NOT NULL,
  `status`  INT          NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ticket_UNIQUE` (`ticket` ASC)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8;