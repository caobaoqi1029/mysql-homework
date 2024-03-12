---
title: 学生管理系统-数据库
order: 2
icon: /project-01.svg
category:
    - PROJECT
    - MD
---

## 数据库 DB

## Init-DB

```sql
/*
 Navicat Premium Data Transfer

 Source Server         : MySQL
 Source Server Type    : MySQL
 Source Server Version : 80035
 Source Host           : localhost:3306
 Source Schema         : mysql-homework

 Target Server Type    : MySQL
 Target Server Version : 80035
 File Encoding         : 65001

 Date: 07/12/2023 21:53:36
*/

CREATE DATABASE IF NOT EXISTS `mysql-homework`;

USE `mysql-homework`;
```

## 表 Table

## admin 管理员表

### Structure

```sql
-- ----------------------------
-- Table structure for t_admin
-- ----------------------------
DROP TABLE IF EXISTS `t_admin`;
CREATE TABLE `t_admin`
(
    `admin_id`    varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL DEFAULT '' COMMENT '管理员 id',
    `admin_name`  varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL     DEFAULT NULL COMMENT '管理员名称',
    `admin_pwd`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '管理员密码',
    `create_time` datetime                                                      NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `deleted`     int                                                           NULL     DEFAULT 0 COMMENT '是否被删除',
    PRIMARY KEY (`admin_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员表'
  ROW_FORMAT = DYNAMIC;
```

### Data
```sql
-- ----------------------------
-- Records of t_admin
-- ----------------------------
INSERT INTO `t_admin` VALUES ('202301', '曹宝琪', '$2a$10$jaJzd0NOeS2h1XXSAsy6UufvcmTx/Execx4jdML9zwYUEYB7OkSWm', '2023-12-09 14:02:58', 0);
```

### Navicat

![image-20231209210411374](https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209210411374.png)

### 数据字典

![image-20231209204621767](https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209204621767.png)

### E-R 图

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231214114959225.png" alt="image-20231214114959225" style="zoom:67%;" />

## major 专业表

### Structure

```sql
-- ----------------------------
-- Table structure for t_major
-- ----------------------------
DROP TABLE IF EXISTS `t_major`;
CREATE TABLE `t_major`
(
    `major_id`     varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '专业 id',
    `major_name`   varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '专业名称',
    `class_total`  int                                                          NULL DEFAULT 0 COMMENT '班级数量',
    `course_total` int                                                          NULL DEFAULT 0 COMMENT '课程数量',
    `create_time`  datetime                                                     NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `stu_total`    int                                                          NULL DEFAULT 0 COMMENT '学生数量',
    `tea_total`    int                                                          NULL DEFAULT 0 COMMENT '老师数量',
    `deleted`      int                                                          NULL DEFAULT 0 COMMENT '是否被删除',
    PRIMARY KEY (`major_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '专业表'
  ROW_FORMAT = DYNAMIC;
```

### Data
```sql
-- ----------------------------
-- Records of t_major
-- ----------------------------
INSERT INTO `t_major` VALUES ('01', '计算机科学与技术', 1, 7, '2023-12-09 14:03:18', 4, 1, 0);
INSERT INTO `t_major` VALUES ('02', 'test', 1, 2, '2023-12-09 14:09:12', 1, 1, 0);
```

### Navicat

![image-20231209210203544](https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209210203544.png)

### 数据字典

![image-20231209204651222](https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209204651222.png)

### E-R 图

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231214115015144.png" alt="image-20231214115015144" style="zoom:67%;" />

## class 班级表

### Structure

```sql
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
```

### Data
```sql
-- ----------------------------
-- Records of t_class
-- ----------------------------
INSERT INTO `t_class` VALUES ('2023011', '01', '2023级计算机科学与技术1班', '计算机科学与技术', 4, '2023-12-09 14:03:29', '2023', '耿老师', 0);
INSERT INTO `t_class` VALUES ('2023021', '02', '2023级test1班', 'test', 1, '2023-12-09 14:09:30', '2023', 'testTeacher', 0);
```

### Navicat

![image-20231209210317686](https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209210317686.png)

![image-20231209210314976](https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209210314976.png)

![image-20231209210312641](https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209210312641.png)

### 数据字典

![image-20231209204710931](https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209204710931.png)

### E-R 图

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231214115034522.png" alt="image-20231214115034522" style="zoom:67%;" />

## teacher 老师表

### Structure

```sql
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
```

### Data
```sql
-- ----------------------------
-- Records of t_teacher
-- ----------------------------
INSERT INTO `t_teacher` VALUES ('tea2023011', '01', '耿老师', '2023级计算机科学与技术1班', '$2a$10$aO/votnmx4IxiWtU6U4Iee8up4W4WTO43PCKW6SfYl33zJyrEztZ.', '141192200205060091', '2023-12-09 14:03:53', '2023011', '计算机科学与技术', '1', '2023', 0);
INSERT INTO `t_teacher` VALUES ('tea2023021', '02', 'testTeacher', '2023级test1班', '$2a$10$eApdFGDieNjNuwcemSPoQufFaA61mRg7GfDpq0EevP2XC7LxHWeZK', '141192200202020041', '2023-12-09 14:09:58', '2023021', 'test', '1', '2023', 0);
```

### Navicat

![image-20231209205854521](https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209205854521.png)

![image-20231209205905657](https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209205905657.png)

![image-20231209205917990](https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209205917990.png)

### 数据字典

![image-20231209204829671](https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209204829671.png)

### E-R 图

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231214115120532.png" alt="image-20231214115120532" style="zoom:67%;" />

## student 学生表

### Structure

```sql
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
```

### Data
```sql
-- ----------------------------
-- Records of t_student
-- ----------------------------
INSERT INTO `t_student` VALUES ('202301101', 'tea2023011', '2023011', '01', '曹宝琪', '2023', '2023 级计算机科学与技术1 班', '141182200205060092', '$2a$10$cpd2S70cHyLaSFU0jW.JS.mouD02RUKT0XNr6BO1LD35yBPxUMhnC', '1', '计算机科学与技术', '2023-12-09 14:05:22', '耿老师', 1, 0);
INSERT INTO `t_student` VALUES ('202301102', 'tea2023011', '2023011', '01', '曹蓓', '2023', '2023 级计算机科学与技术1 班', '141182200205060093', '$2a$10$LjT/AbINVIROH/nGPXcXtO/xNfUurjCYZQOj.o8Z/ubgZYVswG4sK', '1', '计算机科学与技术', '2023-12-09 14:05:31', '耿老师', 1, 0);
INSERT INTO `t_student` VALUES ('202301103', 'tea2023011', '2023011', '01', '程柄惠', '2023', '2023 级计算机科学与技术1 班', '141182200205060094', '$2a$10$ufiewYJjy0zMBMeQ/cfmKeoW5KNZRh5RpDjGvPd/1Sqx6K7LO5bRO', '1', '计算机科学与技术', '2023-12-09 14:05:43', '耿老师', 1, 0);
INSERT INTO `t_student` VALUES ('202301104', 'tea2023011', '2023011', '01', '焦慧静', '2023', '2023 级计算机科学与技术1 班', '141182200205060096', '$2a$10$I1hjc/o/qgm7ugeXHm/mt.9CwN8vcI1jTciJ8fhpxezWfNHxNTC4W', '1', '计算机科学与技术', '2023-12-09 14:05:55', '耿老师', 1, 0);
INSERT INTO `t_student` VALUES ('202302101', 'tea2023021', '2023021', '02', 'testStudent', '2023', '2023 级test1 班', '141182200205060041', '$2a$10$.gzyaNtTC59Peq3bVC7bFe1EzZnsvgE6OH91PqUiL5htCBpWVzJR2', '1', 'test', '2023-12-09 14:10:27', 'testTeacher', 0, 0);
```
### Navicat

![image-20231209210012560](https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209210012560.png)

![image-20231209210007765](https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209210007765.png)

![image-20231209210004073](https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209210004073.png)

### 数据字典

![image-20231209204754619](https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209204754619.png)

### E-R 图

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231214115143381.png" alt="image-20231214115143381" style="zoom:67%;" />

## course 课程表

### Structure

```sql
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
```

### Data
```sql
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
```

### Navicat

![image-20231209210239823](https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209210239823.png)

![image-20231209210236958](https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209210236958.png)

![image-20231209210234340](https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209210234340.png)

### 数据字典

![image-20231209204857271](https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209204857271.png)

### E-R 图

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231214115204972.png" alt="image-20231214115204972" style="zoom:67%;" />

## choose_course 选课表

### Structure

```sql
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
```

### Data
```sql
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
```

### Navicat

![image-20231209210356198](https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209210356198.png)

![image-20231209210353910](https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209210353910.png)

![image-20231209210350468](https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209210350468.png)

### 数据字典

![image-20231209205035760](https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209205035760.png)

### E-R 图

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231214115223450.png" alt="image-20231214115223450" style="zoom:67%;" />

## score 成绩表

### Structure

```sql
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
```

### Data
```sql
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
```

### Navicat

![image-20231209210140673](https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209210140673.png)

![v](https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209210137957.png)

![image-20231209210135119](https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209210135119.png)

### 数据字典

![image-20231209205019453](https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209205019453.png)

### E-R 图

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231214115236347.png" alt="image-20231214115236347" style="zoom:67%;" />

## stu_message 学生消息表

### Structure

```sql
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
```

### Data
```sql
-- ----------------------------
-- Records of t_stu_message
-- ----------------------------
INSERT INTO `t_stu_message` VALUES ('tea2023011msg202301101547f29d4-d7b3-4acb-bcf3-f5dfd210b3ab', '202301101', 'tea2023011', '曹宝琪 加油', '2023-12-09 14:07:44', '已读', 0);
INSERT INTO `t_stu_message` VALUES ('tea2023011msg2023011027b9e2d00-1b79-42e6-bc2d-7fe0916d19d1', '202301102', 'tea2023011', '曹蓓 非常棒', '2023-12-09 14:07:36', '未读', 0);
INSERT INTO `t_stu_message` VALUES ('tea2023011msg2023011034d117c70-d000-461c-9da3-d7b801a8568f', '202301103', 'tea2023011', '程柄惠 很好', '2023-12-09 14:07:27', '未读', 0);
INSERT INTO `t_stu_message` VALUES ('tea2023011msg20230110417ac4fdc-9059-47a8-9128-6e0fd268c7ab', '202301104', 'tea2023011', '焦慧静 加油', '2023-12-09 14:07:53', '未读', 0);
```

### Navicat

![image-20231209210101011](https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209210101011.png)

![image-20231209210058194](https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209210058194.png)

![image-20231209210055991](https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209210055991.png)

### 数据字典

![image-20231209204933041](https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209204933041.png)

### E-R 图

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231214115253876.png" alt="image-20231214115253876" style="zoom:67%;" />

## tea_message 老师消息表

### Structure

```sql
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
```

### Data
```sql
-- ----------------------------
-- Records of t_tea_message
-- ----------------------------
INSERT INTO `t_tea_message` VALUES ('tea2023011msg202301101cab2244b-3bdb-4d53-9f02-f73c74c16222', 'tea2023011', '202301101', '曹宝琪 加油', '2023-12-09 14:07:44', 0);
INSERT INTO `t_tea_message` VALUES ('tea2023011msg202301102501dcd01-5532-491d-af29-18e584738b79', 'tea2023011', '202301102', '曹蓓 非常棒', '2023-12-09 14:07:36', 0);
INSERT INTO `t_tea_message` VALUES ('tea2023011msg2023011032ce49ac1-7d72-4ce7-bae6-3c26e52e5396', 'tea2023011', '202301103', '程柄惠 很好', '2023-12-09 14:07:27', 0);
INSERT INTO `t_tea_message` VALUES ('tea2023011msg202301104e4a95ba1-4c81-4f83-bb40-557e10dabd5f', 'tea2023011', '202301104', '焦慧静 加油', '2023-12-09 14:07:53', 0);
```

### Navicat

![image-20231209205758802](https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209205758802.png)

![image-20231209205816432](https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209205816432.png)

![image-20231209205735845](https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209205735845.png)

### 数据字典

![image-20231209204955601](https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209204955601.png)

### E-R 图

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231214115314883.png" alt="image-20231214115314883" style="zoom:67%;" />