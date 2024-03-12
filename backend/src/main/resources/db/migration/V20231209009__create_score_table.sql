-- ----------------------------
-- Table structure for t_score
-- ----------------------------
DROP TABLE IF EXISTS `t_score`;
CREATE TABLE `t_score`
(
    `score_id`         varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '成绩 id',
    `choose_course_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '选课 id',
    `stu_id`           varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '学生 id',
    `score`            int                                                          NOT NULL COMMENT '成绩',
    `course_name`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '课程名称',
    `stu_name`         varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '学生名称',
    `create_time`      datetime                                                     NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `deleted`          int                                                          NULL DEFAULT 0 COMMENT '是否被删除',
    PRIMARY KEY (`score_id`) USING BTREE,
    INDEX `FK_Reference_1` (`stu_id` ASC) USING BTREE,
    INDEX `f14` (`choose_course_id` ASC) USING BTREE,
    CONSTRAINT `f14` FOREIGN KEY (`choose_course_id`) REFERENCES `t_choose_course` (`choose_course_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `FK_Reference_1` FOREIGN KEY (`stu_id`) REFERENCES `t_student` (`stu_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '成绩表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_score
-- ----------------------------
INSERT INTO `t_score` VALUES ('202301101-202301101choosecourse0101', '202301101choosecourse0101', '202301101', 22, '大学英语', '曹宝琪', '2023-12-09 14:06:01', 0);
INSERT INTO `t_score` VALUES ('202301101-202301101choosecourse0102', '202301101choosecourse0102', '202301101', 3, '高等数学', '曹宝琪', '2023-12-09 14:06:01', 0);
INSERT INTO `t_score` VALUES ('202301101-202301101choosecourse0103', '202301101choosecourse0103', '202301101', 40, 'C 程序设计', '曹宝琪', '2023-12-09 14:06:02', 0);
INSERT INTO `t_score` VALUES ('202301101-202301101choosecourse0104', '202301101choosecourse0104', '202301101', 18, '体育', '曹宝琪', '2023-12-09 14:06:02', 0);
INSERT INTO `t_score` VALUES ('202301101-202301101choosecourse0105', '202301101choosecourse0105', '202301101', 74, 'MySQL 数据库设计', '曹宝琪', '2023-12-09 14:06:02', 0);
INSERT INTO `t_score` VALUES ('202301101-202301101choosecourse0106', '202301101choosecourse0106', '202301101', 95, 'Java 程序开发', '曹宝琪', '2023-12-09 14:06:02', 0);
INSERT INTO `t_score` VALUES ('202301102-202301102choosecourse0101', '202301102choosecourse0101', '202301102', 76, '大学英语', '曹蓓', '2023-12-09 14:06:02', 0);
INSERT INTO `t_score` VALUES ('202301102-202301102choosecourse0102', '202301102choosecourse0102', '202301102', 49, '高等数学', '曹蓓', '2023-12-09 14:06:02', 0);
INSERT INTO `t_score` VALUES ('202301102-202301102choosecourse0103', '202301102choosecourse0103', '202301102', 46, 'C 程序设计', '曹蓓', '2023-12-09 14:06:02', 0);
INSERT INTO `t_score` VALUES ('202301102-202301102choosecourse0104', '202301102choosecourse0104', '202301102', 66, '体育', '曹蓓', '2023-12-09 14:06:02', 0);
INSERT INTO `t_score` VALUES ('202301102-202301102choosecourse0105', '202301102choosecourse0105', '202301102', 43, 'MySQL 数据库设计', '曹蓓', '2023-12-09 14:06:02', 0);
INSERT INTO `t_score` VALUES ('202301102-202301102choosecourse0106', '202301102choosecourse0106', '202301102', 3, 'Java 程序开发', '曹蓓', '2023-12-09 14:06:02', 0);
INSERT INTO `t_score` VALUES ('202301103-202301103choosecourse0101', '202301103choosecourse0101', '202301103', 33, '大学英语', '程柄惠', '2023-12-09 14:06:02', 0);
INSERT INTO `t_score` VALUES ('202301103-202301103choosecourse0102', '202301103choosecourse0102', '202301103', 19, '高等数学', '程柄惠', '2023-12-09 14:06:02', 0);
INSERT INTO `t_score` VALUES ('202301103-202301103choosecourse0103', '202301103choosecourse0103', '202301103', 81, 'C 程序设计', '程柄惠', '2023-12-09 14:06:02', 0);
INSERT INTO `t_score` VALUES ('202301103-202301103choosecourse0104', '202301103choosecourse0104', '202301103', 80, '体育', '程柄惠', '2023-12-09 14:06:02', 0);
INSERT INTO `t_score` VALUES ('202301103-202301103choosecourse0105', '202301103choosecourse0105', '202301103', 79, 'MySQL 数据库设计', '程柄惠', '2023-12-09 14:06:02', 0);
INSERT INTO `t_score` VALUES ('202301103-202301103choosecourse0106', '202301103choosecourse0106', '202301103', 27, 'Java 程序开发', '程柄惠', '2023-12-09 14:06:02', 0);
INSERT INTO `t_score` VALUES ('202301104-202301104choosecourse0101', '202301104choosecourse0101', '202301104', 76, '大学英语', '焦慧静', '2023-12-09 14:06:02', 0);
INSERT INTO `t_score` VALUES ('202301104-202301104choosecourse0102', '202301104choosecourse0102', '202301104', 46, '高等数学', '焦慧静', '2023-12-09 14:06:02', 0);
INSERT INTO `t_score` VALUES ('202301104-202301104choosecourse0103', '202301104choosecourse0103', '202301104', 11, 'C 程序设计', '焦慧静', '2023-12-09 14:06:02', 0);
INSERT INTO `t_score` VALUES ('202301104-202301104choosecourse0104', '202301104choosecourse0104', '202301104', 32, '体育', '焦慧静', '2023-12-09 14:06:02', 0);
INSERT INTO `t_score` VALUES ('202301104-202301104choosecourse0105', '202301104choosecourse0105', '202301104', 15, 'MySQL 数据库设计', '焦慧静', '2023-12-09 14:06:02', 0);
INSERT INTO `t_score` VALUES ('202301104-202301104choosecourse0106', '202301104choosecourse0106', '202301104', 11, 'Java 程序开发', '焦慧静', '2023-12-09 14:06:02', 0);
INSERT INTO `t_score` VALUES ('202302101-202302101choosecourse0202', '202302101choosecourse0202', '202302101', 67, 'testMust', 'testStudent', '2023-12-09 14:11:19', 0);