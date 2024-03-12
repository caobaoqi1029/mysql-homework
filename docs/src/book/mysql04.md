---
title: MySQL 数据库及 SQL 简介
order: 4
icon: /mysql-item.svg
category:
  - BOOK
  - MD
---

## 一、MySQL 数据库操作环境

MySQL 是最流行的关系型数据库管理系统，在 WEB 应用方面 MySQL 是最好的 RDBMS（ Relational Database Management System ：关系数据库管理系统）应用软件之一它由瑞典MySQL AB 公司开发，目前属于 Oracle 公司。MySQL 数据库能够支持在多种操作系统上运行，包括 Solaris 、 Mac OS 、 FreeBSD 和 Windows ， Linux 等。

相比一些大型 DBMS 比如 Oracle 、 DB2 、 SQL Server 等， MySQL 有一些不足之处，但它以其开源、免费、体积小、便于安装，而且功能强大等特点，受到了市场尤其个人和中小企业的广泛欢迎，成为全球最热门的数据库管理系统之一 Navicat 是一套快速、可靠并价格相宜的数据库管理工具，专为简化数据库的管理及降低系统管理成本而设。它的设计符合数据库管理员、开发人员及中小企业的需要。 Navicat 是以可视化的图形用户界面而建的，让用户能以安全并且简单的方式创建、组织、访问并共用信息。

### MySQL 数据库管理系统简介

MySQL 数据库管理系统基于 C/S 模型（即客户端和服务端模型），客户端通过账号、密码来连接服务器，连接成功之后才可以进行数据库的操作（增加、删除、修改、查询）。MySQL 中的数据库一般也不直接面向数据存储，存储是交给表/索引这类对象完成的。

###   MySQL 的系统特性

1. 它是开源的，不需要支付额外的费用
2. 支持大型的数据库，可以处理拥有上千万条记录的大型数据库，使用标准的 SQL 数据语言形式
3. 支持多种语言，这些编程语言包括 C 、 C++ 、 P ython 、 Java 、和 PHP 等
4. 支持大型数据库，支持 5000 万条记录的数据仓库， 32 位系统表文件最大可支持 4GB ,64 位系统支持最大的表文件为 8 TB
5. 可以定制，它采用了 GPL 协议，可以通过修改源码来开发专属于自己的 MySQL 系统。

###  MySQL 的优势

1. 兼容性好：可以运行于 Windows 、 UNIX 或等多个系统上，跨平台性好
2. 易于使用：安装便捷，有多个图形客户端管理工具（ MySQL Workbench/Navicat 、MySQL Front 等客户端），操作简单
3. 价格低：开放源代码，对个人用户免费
4. 性能高：利用命令行客户机程序、 web 浏览器或 X-Window System 客户机程序，多个客户机可同时连接使用多个数据库，交互式地访问 MySQL ，实现高效的数据访问，具有良好的性能
5. 支持 SQL ：作为关系数据库的国际标准语言， SQL 具有简单易学、功能一体化的特点，能提供优化的查询算法，有效地提供查询速度

###  MySQL 安装

### - 使用 Docker 安装

### 1. 拉取 MySQL 镜像

```Bash
docker pull mysql
```

### 2. 创建容器卷

```Bash
docker volume create mysql8_3306
```

### 3. 创建创建 Docker 网络

```Bash
docker network create mysql8-net
```

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231201233348595.png" alt="image-20231201233348595" style="zoom:67%;" />



-  运行 Docker 容器

```Bash
docker run -d --name mysql8_3306 -e MYSQL_ROOT_PASSWORD=root -v mysql8_3306:/var/lib/mysql -p 3306:3306 --network=mysql8-net <镜像 id>

```

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231201233400061.png" alt="image-20231201233400061" style="zoom:67%;" />

### - 使用 MySQL Installer 

使用 MySQL Installer 安装

### 图形化管理工具 Navicat

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231201233410470.png" alt="image-20231201233410470" style="zoom:67%;" />

[Navicat Premium](https://www.navicat.com.cn/) 是一套可创建多个连接的数据库开发工具，让你从单一应用程序中同时连接 MySQL、Redis、MariaDB、MongoDB、SQL Server、Oracle、PostgreSQL 和 SQLite 。它与 GaussDB 主备版、OceanBase 数据库及 Amazon RDS、Amazon Aurora、Amazon Redshift、Amazon ElastiCache、Microsoft Azure、Oracle Cloud、MongoDB Atlas、Redis Enterprise Cloud、阿里云、腾讯云和华为云等云数据库兼容。你可以快速轻松地创建、管理和维护数据库。

### 安装 Navicat Premium 16

### - Tools

1. [点击下载open in new window](https://yong-gan-niu-niu-1311841992.cos.ap-beijing.myqcloud.com/tools/navicat16Tools.zip) 激活工具
2. [Navicat Premium 16](https://www.navicat.com.cn/download/direct-download?product=navicat_premium_cs_x64.exe&location=1)

### - Run

-  **以管理员身份运行**



<img src="https://yong-gan-niu-niu-1311841992.cos.ap-beijing.myqcloud.com/images/image-20230928220125872.png" style="zoom:67%;" />



<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231201233432860.png" alt="image-20231201233432860" style="zoom:67%;" />

### - 断网、断网、断网

复制上方生成的 Keygen

<img src="https://yong-gan-niu-niu-1311841992.cos.ap-beijing.myqcloud.com/images/image-20230928220747466.png" style="zoom:67%;" />



<img src="https://yong-gan-niu-niu-1311841992.cos.ap-beijing.myqcloud.com/images/image-20230928220818538.png" style="zoom:67%;" />



点击手动激活将 请求码 复制到 NavicatCracker 后点击 Generate Activation Code



<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231201233452614.png" alt="image-20231201233452614" style="zoom:67%;" />



<img src="https://yong-gan-niu-niu-1311841992.cos.ap-beijing.myqcloud.com/images/image-20230928220901430.png" style="zoom:67%;" />



### - 验证



<img src="https://yong-gan-niu-niu-1311841992.cos.ap-beijing.myqcloud.com/images/image-20230928220908514.png" style="zoom:67%;" />



<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231201233506931.png" alt="image-20231201233506931" style="zoom:67%;" />

### Navicat 中数据库的基本操作

### - 创建连接

名字起错了应该是 JingDongYun-3306 但没啥影响只是一个标识



<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231201233521637.png" alt="image-20231201233521637" style="zoom:67%;" />



<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231201233532167.png" alt="image-20231201233532167" style="zoom:67%;" />



### - 创建数据库



<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231201233541118.png" alt="image-20231201233541118" style="zoom:67%;" />



<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231201233548979.png" alt="image-20231201233548979" style="zoom:67%;" />



### - 创建表

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231201233559112.png" alt="image-20231201233559112" style="zoom:67%;" />



<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231201233604915.png" alt="image-20231201233604915" style="zoom:67%;" />



### - 修改表



<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231201233615999.png" alt="image-20231201233615999" style="zoom:67%;" />

### - 表中主键和外键的设置

TODO

### - 生成数据

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231201233628809.png" alt="image-20231201233628809" style="zoom:67%;" />

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231201233643055.png" alt="image-20231201233643055" style="zoom:67%;" />

### - 运行和转储 SQL 文件

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231201233651116.png" alt="image-20231201233651116" style="zoom:67%;" />

### - 创建查询

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231201233700250.png" alt="image-20231201233700250" style="zoom:67%;" />



### MySQL 支持的字符集

MySQL 支持的字符集

## 二、SQL 概述及功能

### SQL 概述

**SQL** 的全称是 ==Structure Query Language== ，即结构化查询语言，是关系数据库的国际标准语言。 1974 年， IBM 公司的研究员 Boyce 和 Chamberlin 开发了 **SEQUEL** 语言 (SQL 的原型），而后 IBM 的 San Jose 实验室研制了关系数据库实验系统 System R ，并在该系统上成功实现了SEQUEL 语言，之后在此基础上推出 SQL 语言。

1986 年 10 月，美国国家标准局发布了 **SQL** 的第一个标准 **SQL-86** ； 1987 年，国际标准化组织也通过了这一标准，使 **SQL** 成为了国际标准； 1989 年发布的 **SQL- 89** ，是对 **SQL** 的进一步修改和完善，标志着 **SQL 语言**在推动数据库技术发展中的重要地位。

自 20 世纪 80 年代以来，几乎所有的关系数据库产品都支持 **SQL** 或提供了与 SQL 的接口，这有利于不同的数据库管理系统之间实现良好的互操作性，也有利于数据库操纵语言的标准化制定。

### SQL 的特点及命令动词

### SQL 语言的特点

- 一体化

SQL 语言风格统一，可以完成数据库活动中的全部工作，包括创建数据库、定义模式、更改和查询数据以及安全控制和维护数据库等，这就为数据库应用系统的开发提供了良好的环境。比如，用户在数据库投人运行后，还可根据需要随时地逐步修改模式，并不影响数据库的运行，从而使系统具有良好的可扩充性。

- 高度非过程化

在使用 SQL 语句访问数据库时，用户没有必要告诉计算机如何一步步完成任务，只需要SQL 语言描述要做什么就行了，数据库管理系统会自动完成全部工作

- 提供多种方式使用

::: tip  SQL 既是自含式语言，又是嵌人式语言

- 自含式语言是指 SQL 可以独立地联机交互，即用户可以直接以命令的方式操作数据库中的数据
- 嵌人式语言是指 SQL 可以嵌人到 Java 、 C#等高级程序设计语言中使用

:::

- 支持三级数据模式

::: danger 三级数据模式

- **全体基本表构成了数据库的 全局逻辑模式**
- **视图和部分基本表（被用户直接操作）构成了数据库的 外模式**
- **数据库的存储文件和索引文件则构成了数据库的 内模式。**

:::

### SQL 语言三大功能及命令动词

SQL 语言简洁，易学易用，主要有以下三大功能和 9 个命令动词。

|          | 数据定义 (DDL)                                               | 数据操纵 (DML)                                               | 数据控制 (DCL)                                               |
| -------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 关键字   | CREATE、 ALERT 和 DROP                                       | SELECT、INSERT 和 DELETE                                     | GRANT 和 REVOKE                                              |
| 实现方式 | Data DefinitionL(Language)                                   | Data ManipulationL                                           | Data ControlL                                                |
| 作用     | 用于定义、修改和删除数据库中各种对象，包括表、视图、索引、触发器等实现 SQL 的数据定义功能 | 用于对数据库中数据的查词、添加、删除、修改等操作实现 SQL 的数据操纵功能 | 用于对数据库中用户的授权和收权操作，以控制不同用户在各自权限范围内使用数据库资源，从而保证数据库的安全性实现 SQL 的数据控制功能 |

## 三、MySQL 数据类型

MySQL 中提供了多种数据类型主要是数值型、 字符串型、 日期时间型三类。

### 数值型

MySQL 中支持的数值型包括：

- 整型 TINYINT 、 SMALLINT 、 MEDIUMINT 、 INT 、 BIGINT 
- 浮点小数型 FLOAT 、 DOUBIÆ 
- 定点小数型 DECIMAL

关键字 INT 是 INTEGER 的同义词，关键字 DEC 是 DECIMAL 的同义词

表 4 一 1 中对不同的数值型的存储、范围及用途进行了说明

TODO

### - 整数型

五种整型占用的字节不同，能存储的数的范围也不同，可以根据所占字节数计算出每一种类型的取值范围，如果不设置无符号还是有符号，默认为有符号。MySQL 支持为整型数据设置显示宽度，比如 INT （ 5 ），指定最大的显示宽度为 5 个字符，如果数值的宽度小于指定宽度时会在左边补空格。显示宽度只用于显示，并不能限制取值范围和占用空间，比如 INT （ 5 ）能表示的数的范围仍然是一 2147483M8 到 2147483M7 ，占用的空间也是 4 个字节不变。

### - 浮点小数和定点小数型

浮点小数型包括单精度浮点类型 FLOAT 和双精度浮点类型 DOUBLE ，定点小数型只有一种 DECIMALO 这三种类型都可以用（ M ， D) 来表示，其中 M 为精度，表示整数部分加小数部分的总长度， D 为标度，表示小数部分的长度。 M 和 D 如果省略，对于 FLOAT 和 DOUBLE会根据插人数值的精度来决定精度，对于 DECIMAL 则 M 默认为 10 而 D 默认为 0浮点数相对于定点数的优点是在长度一定的情况下，浮点数能够表示更大的数据范围；它的缺点是会引起精度问题，浮点数在数据库中存放的是近似值，而定点数在数据库中存放的是精确值。关于浮点数和定点数的应用中，应注意以下几点：0 ）浮点数存在误差问题，编程中，如果用到浮点数，应尽量避免做浮点数比较或精确数值运算；（ 2 ）对货币等对精度敏感的数据，应该用定点数表示或存储；（ 3 ）要注意浮点数中一些特殊值的处理。

### 字符串型

MySQL 中的字符串型用于存储字符串数据，包括文本字符串、二进制字符串和复合类型字符串三大类。

### - 文本字符串

>  普通文本字符串 CHAR 和 VARCHAR

CHAR 指的是定长字符串，在定义时指定字符串的长度，若存人字符串实际长度小于指定长度，保存时在右侧填充空格以达到指定的长度，在访问该数据时再把空格去掉，因此CHAR 类型的数据末尾不能有空格， VARCHAR 指的是变长字符串，在定义时指定字符串的

最大长度，保存时按存人字符串的实际长度分配存储空间，在访问该数据时不会去掉尾部空格比如我们将学生表中的专业和姓名字段的类型分别定义为 CHAR （ 10 ）和 VARCHAR（ 10 ），则无论该学生的专业是“计算机"还是“信息管理"，在存储时都会在后面补齐空格后按长度为 10 的字符串处理；但如果两个学生的姓名分别叫“王华”和“张晓磊”，则存储时会分别按照长度为 2 和长度为 3 的字符串进行处理，按实际长度+ 1 来分配存储空间从数据的访问速度看， CHAR 类型的字段在内存中占的存储空间一样大，访问效率会比VARCHAR 更高；但从灵活性和节省存储空间的角度， VARCHAR 比 CHAR 更好。所以我们在给表中字符串型字段选择类型时，学号、身份证号、手机号等长度固定的字段，可以选择CHAR 类型以提高系统性能；而姓名、专业、籍贯等长度不固定的字段，选择 VARCHAR 以提高灵活性

> 长文本字符串 TEXT ： TINYTEXT 、 TEXT 、 MEDIUMTEXT 和 LONGTEXT

TEXT 型用于一长文比日文“谷、个人伊它不CHAR 或 VARCHAR 可以指定字符串长度，而且也不能有默认值。

### - 二进制字符串

> 普通二进制字符串

（ 1) 普通二进制字符串：定长二进制字符串 BINARY 和变成二进制字符串 VARBINARY这两个类型类似于 CHAR 和 VARCHAR ，不同的是它们只包含二进制字符，用于存储“ 0 ”和“ 1 ”组成的字符串，只有二进制字符集 binary ，而不是像文本字符串包含的是非二进制字符，有多种字符集和多种校对规则。二进制字符串是按字节为单位进行存储的，一个二进制位占一个字节的存储空间，比如数据类型为 binary （ 10 ）的字段，无论字段值实际包含几个二进制位，所占的存储空间都是10 个字节；而文本字符串是以字符为单位进行存储，比如数据类型为 CHAR （ 10 ）的字段，在内存中占用 10 个字符的存储空间，至于一个字符等于几个字节，要取决于选择的字符集。

> 长二进制字符串

（ 2 ）长二进制字符串： TINYBLOB 、 BLOB 、 MEDIUMBLOB 和 LONGBLOB这几个类型主要用于存储图片、音频、视频等二进制数据，但在实际的数据库中，会以文件的形式存储这些数据，而不会存储在数据库表中，因此长二进制字符串类型很少会用到。



### - 复合型字符串

> ENUM

ENUM 类型的字段只允许从一个集合中取得一个值，集合中最多包含 65535 个元素。在表创建时为某个字段定义为 ENUM 类型的语法格式是：字段名 ENUM （．值 1 '，'值 2 ，'值 n ，）。比如定义学生表时，限制姓名字段的值只能是，男，或，女'，则可以对该字段做如下定义：姓名 ENUM （；男'，'女，）。 

> SET

SET 类型的字段允许从一个集合中取得多个值，集合中最多包含 64 个元素

### 日期时间型

DATA 类型仅存储日期而不存储时间， TIME 类型仅存储时间， DATATIME 和TIMESTAMP 都是既存储日期也存储时间，如果需要表示年月日时分秒， DATETIME 和 TIME-STAMP 都是可以选择的数据类型，下面对两种类型进行说明1. 从存储空间上， DATETIME 占 8 个字节， TIMESTAMP 占 4 个字节；2 ，从取值范围上， DATETIME 是 1 用一 01 一 01 佣：佣：佣至 9999 一 12 一 31 23 ： 59 ： 59 ，TIMESTAMP 是 1970 一 01 一 01 0() ： 00 ： 01.0 ）） 0 至 2038 一 01 一 19 03 ： 14 ： 07 ；3 、从显示格式上， DATETIME 是 YYYY —MM —DD HH ： MM ： SS ，而 TIMESTAMP 是YYYYMMDD HHMMSS ；4 ．从存储形式上， TIMESTAMP 存储占用的空间和 INT 类型相同，实际上 TIMESTAMP 类型的数据就是按 INT 类型的数据来存储的，而 DATATIME 类型的数据是按字符串型的数据来存储的。因为 INT 型数据的范围有限制，所以和 INT 类型一样其存储的时间范围也是有限制的，超过了其时间范围的数据建议使用 DATETIME 类型来保存。，在表中若定义一个字段为 TIMESTAMP 类型，这个字段里的时间数据会随其他字段的修改自动刷新，所以这个数据类型的字段可以存放这条记录最后被修改的时间

### 选择数据类型的原则

在 MYQL 中为字段或变量选择合适的数据类型，可以有效地节省存储空间和提升数据的计算性能，数据类型的选择应遵循以下几个基本原则。1. 在指定数据类型的时候一般是采用从小原则，比如能用 TINYINT 的最好就不用 INT ，能用 FLOAT 类型的就不用 DOU BLE 类型，能用 CHAR 就尽量不用 TEXT ，这样会让 MYSQL在运行效率上提高很多，尤其是大数据量测试条件下；2 ． DECIMAL 最适合保存准确度要求高，而且用于计算的数据，比如价格，但是在使用DECIMAL 类型的时候，注意长度设置；3 ．不需要把数据表设计的太过复杂，功能模块上区分或许对于后期的维护更为方便，慎重出现大杂烩数据表；4 ，整型通常是最佳的数据类型，因为它速度快，并且能使用 AUTO-INCREMENT ，如果整型数据没有负数，如 ID 号，建议指定为 UNSIGNE D 无符号类型，容量可以扩大一倍；5 ．避免使用整数的显示宽度，尽量不要用 INT （ 10 ）类似的方法指定字段显示宽度，直接用 INT
