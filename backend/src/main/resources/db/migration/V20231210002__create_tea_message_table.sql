-- ----------------------------
-- Table structure for t_tea_message
-- ----------------------------
DROP TABLE IF EXISTS `t_tea_message`;
CREATE TABLE `t_tea_message`
(
    `msg_id`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '消息 id',
    `tea_id`      varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '教师 id',
    `stu_id`      varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '学生 id',
    `msg_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '消息内容',
    `msg_time`    datetime                                                      NULL DEFAULT NULL COMMENT '消息时间',
    `deleted`     int                                                           NULL DEFAULT 0 COMMENT '是否被删除',
    PRIMARY KEY (`msg_id`) USING BTREE,
    INDEX `f15` (`stu_id` ASC) USING BTREE,
    INDEX `f16` (`tea_id` ASC) USING BTREE,
    CONSTRAINT `f15` FOREIGN KEY (`stu_id`) REFERENCES `t_student` (`stu_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `f16` FOREIGN KEY (`tea_id`) REFERENCES `t_teacher` (`tea_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '老师消息表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_tea_message
-- ----------------------------
INSERT INTO `t_tea_message` VALUES ('tea2023011msg202301101cab2244b-3bdb-4d53-9f02-f73c74c16222', 'tea2023011', '202301101', '曹宝琪 加油', '2023-12-09 14:07:44', 0);
INSERT INTO `t_tea_message` VALUES ('tea2023011msg202301102501dcd01-5532-491d-af29-18e584738b79', 'tea2023011', '202301102', '曹蓓 非常棒', '2023-12-09 14:07:36', 0);
INSERT INTO `t_tea_message` VALUES ('tea2023011msg2023011032ce49ac1-7d72-4ce7-bae6-3c26e52e5396', 'tea2023011', '202301103', '程柄惠 很好', '2023-12-09 14:07:27', 0);
INSERT INTO `t_tea_message` VALUES ('tea2023011msg202301104e4a95ba1-4c81-4f83-bb40-557e10dabd5f', 'tea2023011', '202301104', '焦慧静 加油', '2023-12-09 14:07:53', 0);