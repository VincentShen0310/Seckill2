SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `seckill`
-- ----------------------------
DROP TABLE IF EXISTS `seckill`;
CREATE TABLE `seckill` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(120) NOT NULL,
  `number` int(11) NOT NULL,
  `start_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `end_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_start_time` (`start_time`),
  KEY `idx_end_time` (`end_time`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of seckill
-- ----------------------------
INSERT INTO `seckill` VALUES ('1', '1000元ipone6', '100', '2016-07-28 13:28:16', '2016-07-29 00:00:00', '2016-07-27 13:34:03');
INSERT INTO `seckill` VALUES ('2', '500元ipad2', '200', '2016-08-03 13:28:19', '2016-08-10 00:00:00', '2016-07-27 13:34:03');
INSERT INTO `seckill` VALUES ('3', '300元小米4s', '300', '2016-08-01 18:46:07', '2016-08-02 00:00:00', '2016-07-27 13:34:03');
INSERT INTO `seckill` VALUES ('4', '200元小米Note', '400', '2016-08-02 09:30:01', '2016-08-03 00:00:00', '2016-07-27 13:34:03');

-- ----------------------------
-- Table structure for `success_killed`
-- ----------------------------
DROP TABLE IF EXISTS `success_killed`;
CREATE TABLE `success_killed` (
  `id` bigint(20) NOT NULL,
  `user_phone` bigint(20) NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '1',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`user_phone`),
  KEY `idx_start_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;