DROP TABLE IF EXISTS `news`;
CREATE TABLE news
(
  `id`            INT          NOT NULL AUTO_INCREMENT,
  `title`         VARCHAR(256) NOT NULL DEFAULT '',
  `link`          VARCHAR(256) NOT NULL DEFAULT '',
  `image`         VARCHAR(256) NOT NULL DEFAULT '',
  `create_time`   TIMESTAMP    NOT NULL,
  `user_id`       VARCHAR(128) NOT NULL,
  `like_count`    INT          NOT NULL,
  `comment_count` INT          NOT NULL,
  PRIMARY KEY (`id`)
)ENGINE=InnoDB AUTO_INCREMENT=10 CHARSET=UTF8;