-- ----------------------------
-- Table structure for t_course
-- ----------------------------
DROP TABLE IF EXISTS `t_course`;
CREATE TABLE `t_course`
(
    `course_id`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '课程 id',
    `major_id`       varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '专业 id',
    `course_name`    varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '课程名称',
    `if_degree`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci     NULL DEFAULT '0' COMMENT '是否选修',
    `major_name`     varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '专业名称',
    `create_time`    datetime                                                     NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `course_period`  varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT '' COMMENT '课程时间',
    `stu_choose_num` int                                                          NULL DEFAULT 0 COMMENT '选择该课程的学生数量',
    `deleted`        int                                                          NULL DEFAULT 0 COMMENT '是否被删除',
    PRIMARY KEY (`course_id`) USING BTREE,
    INDEX `FK_Reference_11` (`major_id` ASC) USING BTREE,
    CONSTRAINT `FK_Reference_11` FOREIGN KEY (`major_id`) REFERENCES `t_major` (`major_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '课程表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_course
-- ----------------------------
INSERT INTO `t_course` VALUES ('course0101', '01', '大学英语', '1', '计算机科学与技术', '2023-12-09 14:04:05', '大一上', 4, 0);
INSERT INTO `t_course` VALUES ('course0102', '01', '高等数学', '1', '计算机科学与技术', '2023-12-09 14:04:17', '大一上', 4, 0);
INSERT INTO `t_course` VALUES ('course0103', '01', 'C 程序设计', '1', '计算机科学与技术', '2023-12-09 14:04:26', '大一上', 4, 0);
INSERT INTO `t_course` VALUES ('course0104', '01', '体育', '1', '计算机科学与技术', '2023-12-09 14:04:32', '大一上', 4, 0);
INSERT INTO `t_course` VALUES ('course0105', '01', 'MySQL 数据库设计', '1', '计算机科学与技术', '2023-12-09 14:04:43', '大一下', 4, 0);
INSERT INTO `t_course` VALUES ('course0106', '01', 'Java 程序开发', '1', '计算机科学与技术', '2023-12-09 14:04:52', '大一下', 4, 0);
INSERT INTO `t_course` VALUES ('course0107', '01', '音乐鉴赏', '0', '计算机科学与技术', '2023-12-09 14:04:59', '大一上', 0, 0);
INSERT INTO `t_course` VALUES ('course0201', '02', 'testNo', '0', 'test', '2023-12-09 14:11:07', '大一上', 0, 0);
INSERT INTO `t_course` VALUES ('course0202', '02', 'testMust', '1', 'test', '2023-12-09 14:11:14', '大一上', 1, 0);