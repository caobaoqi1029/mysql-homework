-- ----------------------------
-- Table structure for t_class
-- ----------------------------
DROP TABLE IF EXISTS `t_class`;
CREATE TABLE `t_class`
(
    `class_id`    varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '班级 id',
    `major_id`    varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '专业 id',
    `class_name`  varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '班级名称',
    `major_name`  varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '专业名称',
    `stu_total`   int                                                          NULL DEFAULT 0 COMMENT '学生数量',
    `create_time` datetime                                                     NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `class_year`  varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT '' COMMENT '年级',
    `tea_name`    varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '老师名称',
    `deleted`     int                                                          NULL DEFAULT 0 COMMENT '是否被删除',
    PRIMARY KEY (`class_id`) USING BTREE,
    INDEX `FK_Reference_7` (`major_id` ASC) USING BTREE,
    CONSTRAINT `FK_Reference_7` FOREIGN KEY (`major_id`) REFERENCES `t_major` (`major_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '班级表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_class
-- ----------------------------
INSERT INTO `t_class` VALUES ('2023011', '01', '2023级计算机科学与技术1班', '计算机科学与技术', 4, '2023-12-09 14:03:29', '2023', '耿老师', 0);
INSERT INTO `t_class` VALUES ('2023021', '02', '2023级test1班', 'test', 1, '2023-12-09 14:09:30', '2023', 'testTeacher', 0);