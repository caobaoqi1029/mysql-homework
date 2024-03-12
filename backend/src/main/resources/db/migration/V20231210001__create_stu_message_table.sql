-- ----------------------------
-- Table structure for t_stu_message
-- ----------------------------
DROP TABLE IF EXISTS `t_stu_message`;
CREATE TABLE `t_stu_message`
(
    `msg_id`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '消息 id',
    `stu_id`      varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '学生 id',
    `tea_id`      varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '教师 id',
    `msg_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '消息内容',
    `msg_time`    datetime                                                      NULL DEFAULT CURRENT_TIMESTAMP COMMENT '消息时间',
    `msg_state`   varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   NULL DEFAULT '未读' COMMENT '消息状态(已读|未读)',
    `deleted`     int                                                           NULL DEFAULT 0 COMMENT '是否被删除',
    PRIMARY KEY (`msg_id`) USING BTREE,
    INDEX `FK_Reference_5` (`tea_id` ASC) USING BTREE,
    INDEX `FK_Reference_6` (`stu_id` ASC) USING BTREE,
    CONSTRAINT `FK_Reference_5` FOREIGN KEY (`tea_id`) REFERENCES `t_teacher` (`tea_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `FK_Reference_6` FOREIGN KEY (`stu_id`) REFERENCES `t_student` (`stu_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '学生消息表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_stu_message
-- ----------------------------
INSERT INTO `t_stu_message` VALUES ('tea2023011msg202301101547f29d4-d7b3-4acb-bcf3-f5dfd210b3ab', '202301101', 'tea2023011', '曹宝琪 加油', '2023-12-09 14:07:44', '已读', 0);
INSERT INTO `t_stu_message` VALUES ('tea2023011msg2023011027b9e2d00-1b79-42e6-bc2d-7fe0916d19d1', '202301102', 'tea2023011', '曹蓓 非常棒', '2023-12-09 14:07:36', '未读', 0);
INSERT INTO `t_stu_message` VALUES ('tea2023011msg2023011034d117c70-d000-461c-9da3-d7b801a8568f', '202301103', 'tea2023011', '程柄惠 很好', '2023-12-09 14:07:27', '未读', 0);
INSERT INTO `t_stu_message` VALUES ('tea2023011msg20230110417ac4fdc-9059-47a8-9128-6e0fd268c7ab', '202301104', 'tea2023011', '焦慧静 加油', '2023-12-09 14:07:53', '未读', 0);