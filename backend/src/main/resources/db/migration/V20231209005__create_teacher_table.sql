-- ----------------------------
-- Table structure for t_teacher
-- ----------------------------
DROP TABLE IF EXISTS `t_teacher`;
CREATE TABLE `t_teacher`
(
    `tea_id`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '教师 id',
    `major_id`    varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '专业 id',
    `tea_name`    varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '教师名称',
    `class_name`  varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '班级名称',
    `tea_pwd`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '教师密码',
    `tea_ID_card` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '教师 IDCard',
    `create_time` datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `class_id`    varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '班级 id',
    `major_name`  varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '专业名称',
    `class_no`    varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   NULL DEFAULT NULL COMMENT '班级编号',
    `class_year`  varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   NULL DEFAULT NULL COMMENT '年级',
    `deleted`     int                                                           NULL DEFAULT 0 COMMENT '是否被删除',
    PRIMARY KEY (`tea_id`) USING BTREE,
    INDEX `FK_Reference_3` (`class_id` ASC) USING BTREE,
    INDEX `FK_Reference_4` (`major_id` ASC) USING BTREE,
    CONSTRAINT `FK_Reference_3` FOREIGN KEY (`class_id`) REFERENCES `t_class` (`class_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `FK_Reference_4` FOREIGN KEY (`major_id`) REFERENCES `t_major` (`major_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_teacher
-- ----------------------------
INSERT INTO `t_teacher` VALUES ('tea2023011', '01', '耿老师', '2023级计算机科学与技术1班', '$2a$10$aO/votnmx4IxiWtU6U4Iee8up4W4WTO43PCKW6SfYl33zJyrEztZ.', '141192200205060091', '2023-12-09 14:03:53', '2023011', '计算机科学与技术', '1', '2023', 0);
INSERT INTO `t_teacher` VALUES ('tea2023021', '02', 'testTeacher', '2023级test1班', '$2a$10$eApdFGDieNjNuwcemSPoQufFaA61mRg7GfDpq0EevP2XC7LxHWeZK', '141192200202020041', '2023-12-09 14:09:58', '2023021', 'test', '1', '2023', 0);