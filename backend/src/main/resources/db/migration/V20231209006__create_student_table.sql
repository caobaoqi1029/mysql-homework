-- ----------------------------
-- Table structure for t_student
-- ----------------------------
DROP TABLE IF EXISTS `t_student`;
CREATE TABLE `t_student`
(
    `stu_id`         varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL DEFAULT '' COMMENT '学生 id',
    `tea_id`         varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL     DEFAULT NULL COMMENT '教师 id',
    `class_id`       varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL     DEFAULT NULL COMMENT '班级 id',
    `major_id`       varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL     DEFAULT NULL COMMENT '专业 id',
    `stu_name`       varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL     DEFAULT '' COMMENT '学生姓名',
    `admission_year` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   NULL     DEFAULT '' COMMENT '年级',
    `class_name`     varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL     DEFAULT '' COMMENT '班级名称',
    `stu_ID_card`    varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL     DEFAULT '' COMMENT '学生 IDCard',
    `stu_pwd`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT '' COMMENT '学生密码',
    `class_no`       varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   NULL     DEFAULT '' COMMENT '班级编号',
    `major_name`     varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL     DEFAULT '' COMMENT '专业名称',
    `create_time`    datetime                                                      NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `tea_name`       varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL     DEFAULT NULL COMMENT '老师名称(班主任)',
    `message_num`    int                                                           NULL     DEFAULT 0 COMMENT '信息数量',
    `deleted`        int                                                           NULL     DEFAULT 0 COMMENT '是否被删除',
    PRIMARY KEY (`stu_id`) USING BTREE,
    INDEX `FK_Reference_10` (`major_id` ASC) USING BTREE,
    INDEX `FK_Reference_8` (`class_id` ASC) USING BTREE,
    CONSTRAINT `FK_Reference_10` FOREIGN KEY (`major_id`) REFERENCES `t_major` (`major_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `FK_Reference_8` FOREIGN KEY (`class_id`) REFERENCES `t_class` (`class_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '学生表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_student
-- ----------------------------
INSERT INTO `t_student` VALUES ('202301101', 'tea2023011', '2023011', '01', '曹宝琪', '2023', '2023 级计算机科学与技术1 班', '141182200205060092', '$2a$10$cpd2S70cHyLaSFU0jW.JS.mouD02RUKT0XNr6BO1LD35yBPxUMhnC', '1', '计算机科学与技术', '2023-12-09 14:05:22', '耿老师', 1, 0);
INSERT INTO `t_student` VALUES ('202301102', 'tea2023011', '2023011', '01', '曹蓓', '2023', '2023 级计算机科学与技术1 班', '141182200205060093', '$2a$10$LjT/AbINVIROH/nGPXcXtO/xNfUurjCYZQOj.o8Z/ubgZYVswG4sK', '1', '计算机科学与技术', '2023-12-09 14:05:31', '耿老师', 1, 0);
INSERT INTO `t_student` VALUES ('202301103', 'tea2023011', '2023011', '01', '程柄惠', '2023', '2023 级计算机科学与技术1 班', '141182200205060094', '$2a$10$ufiewYJjy0zMBMeQ/cfmKeoW5KNZRh5RpDjGvPd/1Sqx6K7LO5bRO', '1', '计算机科学与技术', '2023-12-09 14:05:43', '耿老师', 1, 0);
INSERT INTO `t_student` VALUES ('202301104', 'tea2023011', '2023011', '01', '焦慧静', '2023', '2023 级计算机科学与技术1 班', '141182200205060096', '$2a$10$I1hjc/o/qgm7ugeXHm/mt.9CwN8vcI1jTciJ8fhpxezWfNHxNTC4W', '1', '计算机科学与技术', '2023-12-09 14:05:55', '耿老师', 1, 0);
INSERT INTO `t_student` VALUES ('202302101', 'tea2023021', '2023021', '02', 'testStudent', '2023', '2023 级test1 班', '141182200205060041', '$2a$10$.gzyaNtTC59Peq3bVC7bFe1EzZnsvgE6OH91PqUiL5htCBpWVzJR2', '1', 'test', '2023-12-09 14:10:27', 'testTeacher', 0, 0);