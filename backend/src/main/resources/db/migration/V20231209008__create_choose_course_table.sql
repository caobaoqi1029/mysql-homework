-- ----------------------------
-- Table structure for t_choose_course
-- ----------------------------
DROP TABLE IF EXISTS `t_choose_course`;
CREATE TABLE `t_choose_course`
(
    `choose_course_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '选课 id',
    `stu_id`           varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '学生 id',
    `course_id`        varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '课程 id',
    `create_time`      datetime                                                     NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `state`            int                                                          NULL DEFAULT 0 COMMENT '是否选择',
    `stu_name`         varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '学生名称',
    `course_name`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '课程名称',
    `major_name`       varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '专业名称',
    `course_period`    varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '课程时间',
    `if_degree`        varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT '0' COMMENT '是否选修',
    `deleted`          int                                                          NULL DEFAULT 0 COMMENT '是否被删除',
    PRIMARY KEY (`choose_course_id`) USING BTREE,
    INDEX `f12` (`stu_id` ASC) USING BTREE,
    INDEX `f13` (`course_id` ASC) USING BTREE,
    CONSTRAINT `f12` FOREIGN KEY (`stu_id`) REFERENCES `t_student` (`stu_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `f13` FOREIGN KEY (`course_id`) REFERENCES `t_course` (`course_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '选课表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_choose_course
-- ----------------------------
INSERT INTO `t_choose_course` VALUES ('202301101choosecourse0101', '202301101', 'course0101', '2023-12-09 14:05:58', 1, '曹宝琪', '大学英语', '计算机科学与技术', '大一上', '1', 0);
INSERT INTO `t_choose_course` VALUES ('202301101choosecourse0102', '202301101', 'course0102', '2023-12-09 14:05:58', 1, '曹宝琪', '高等数学', '计算机科学与技术', '大一上', '1', 0);
INSERT INTO `t_choose_course` VALUES ('202301101choosecourse0103', '202301101', 'course0103', '2023-12-09 14:05:58', 1, '曹宝琪', 'C 程序设计', '计算机科学与技术', '大一上', '1', 0);
INSERT INTO `t_choose_course` VALUES ('202301101choosecourse0104', '202301101', 'course0104', '2023-12-09 14:05:58', 1, '曹宝琪', '体育', '计算机科学与技术', '大一上', '1', 0);
INSERT INTO `t_choose_course` VALUES ('202301101choosecourse0105', '202301101', 'course0105', '2023-12-09 14:05:58', 1, '曹宝琪', 'MySQL 数据库设计', '计算机科学与技术', '大一下', '1', 0);
INSERT INTO `t_choose_course` VALUES ('202301101choosecourse0106', '202301101', 'course0106', '2023-12-09 14:05:58', 1, '曹宝琪', 'Java 程序开发', '计算机科学与技术', '大一下', '1', 0);
INSERT INTO `t_choose_course` VALUES ('202301102choosecourse0101', '202301102', 'course0101', '2023-12-09 14:05:58', 1, '曹蓓', '大学英语', '计算机科学与技术', '大一上', '1', 0);
INSERT INTO `t_choose_course` VALUES ('202301102choosecourse0102', '202301102', 'course0102', '2023-12-09 14:05:58', 1, '曹蓓', '高等数学', '计算机科学与技术', '大一上', '1', 0);
INSERT INTO `t_choose_course` VALUES ('202301102choosecourse0103', '202301102', 'course0103', '2023-12-09 14:05:58', 1, '曹蓓', 'C 程序设计', '计算机科学与技术', '大一上', '1', 0);
INSERT INTO `t_choose_course` VALUES ('202301102choosecourse0104', '202301102', 'course0104', '2023-12-09 14:05:58', 1, '曹蓓', '体育', '计算机科学与技术', '大一上', '1', 0);
INSERT INTO `t_choose_course` VALUES ('202301102choosecourse0105', '202301102', 'course0105', '2023-12-09 14:05:58', 1, '曹蓓', 'MySQL 数据库设计', '计算机科学与技术', '大一下', '1', 0);
INSERT INTO `t_choose_course` VALUES ('202301102choosecourse0106', '202301102', 'course0106', '2023-12-09 14:05:58', 1, '曹蓓', 'Java 程序开发', '计算机科学与技术', '大一下', '1', 0);
INSERT INTO `t_choose_course` VALUES ('202301103choosecourse0101', '202301103', 'course0101', '2023-12-09 14:05:58', 1, '程柄惠', '大学英语', '计算机科学与技术', '大一上', '1', 0);
INSERT INTO `t_choose_course` VALUES ('202301103choosecourse0102', '202301103', 'course0102', '2023-12-09 14:05:58', 1, '程柄惠', '高等数学', '计算机科学与技术', '大一上', '1', 0);
INSERT INTO `t_choose_course` VALUES ('202301103choosecourse0103', '202301103', 'course0103', '2023-12-09 14:05:58', 1, '程柄惠', 'C 程序设计', '计算机科学与技术', '大一上', '1', 0);
INSERT INTO `t_choose_course` VALUES ('202301103choosecourse0104', '202301103', 'course0104', '2023-12-09 14:05:58', 1, '程柄惠', '体育', '计算机科学与技术', '大一上', '1', 0);
INSERT INTO `t_choose_course` VALUES ('202301103choosecourse0105', '202301103', 'course0105', '2023-12-09 14:05:58', 1, '程柄惠', 'MySQL 数据库设计', '计算机科学与技术', '大一下', '1', 0);
INSERT INTO `t_choose_course` VALUES ('202301103choosecourse0106', '202301103', 'course0106', '2023-12-09 14:05:58', 1, '程柄惠', 'Java 程序开发', '计算机科学与技术', '大一下', '1', 0);
INSERT INTO `t_choose_course` VALUES ('202301104choosecourse0101', '202301104', 'course0101', '2023-12-09 14:05:58', 1, '焦慧静', '大学英语', '计算机科学与技术', '大一上', '1', 0);
INSERT INTO `t_choose_course` VALUES ('202301104choosecourse0102', '202301104', 'course0102', '2023-12-09 14:05:58', 1, '焦慧静', '高等数学', '计算机科学与技术', '大一上', '1', 0);
INSERT INTO `t_choose_course` VALUES ('202301104choosecourse0103', '202301104', 'course0103', '2023-12-09 14:05:58', 1, '焦慧静', 'C 程序设计', '计算机科学与技术', '大一上', '1', 0);
INSERT INTO `t_choose_course` VALUES ('202301104choosecourse0104', '202301104', 'course0104', '2023-12-09 14:05:58', 1, '焦慧静', '体育', '计算机科学与技术', '大一上', '1', 0);
INSERT INTO `t_choose_course` VALUES ('202301104choosecourse0105', '202301104', 'course0105', '2023-12-09 14:05:58', 1, '焦慧静', 'MySQL 数据库设计', '计算机科学与技术', '大一下', '1', 0);
INSERT INTO `t_choose_course` VALUES ('202301104choosecourse0106', '202301104', 'course0106', '2023-12-09 14:05:58', 1, '焦慧静', 'Java 程序开发', '计算机科学与技术', '大一下', '1', 0);
INSERT INTO `t_choose_course` VALUES ('202302101choosecourse0202', '202302101', 'course0202', '2023-12-09 14:11:16', 1, 'testStudent', 'testMust', 'test', '大一上', '1', 0);