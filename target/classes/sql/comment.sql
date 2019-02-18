DROP TABLE IF EXISTS `commemt`;
CREATE TABLE `comment`
(
  `id`          INT       NOT NULL AUTO_INCREMENT,
  `content`     TEXT      NOT NULL,
  `user_id`     INT       NOT NULL,
  `entity_id`   INT       NOT NULL,
  `entity_type` INT       NOT NULL,
  `create_time` TIMESTAMP NOT NULL,
  `status`      INT       NOT NULL,
  PRIMARY KEY (`id`),
  INDEX         `entity_index` (`entity_id` ASC,`entity_type` ASC)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;
