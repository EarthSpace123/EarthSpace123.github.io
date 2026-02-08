# 图书商城管理系统

这是一个基于Spring Boot实现的图书商城管理系统，展示了多种设计模式的应用。

## 技术栈
- Java 17
- Spring Boot 3.5.9
- Spring Data JPA
- Spring Security
- Thymeleaf
- MySQL

## 实现的设计模式
- MVC模式：整体架构
- 策略模式：订单状态处理
- 单例模式：系统配置管理
- 观察者模式：订单状态通知
- 状态模式：购物车管理
- 责任链模式：进货审批流程

## 功能特性
- 用户登录和权限管理
- 商品浏览和搜索
- 购物车管理
- 订单处理
- 进货审批流程

## 系统架构
系统采用B/S体系架构，Spring Boot + Thymeleaf + MySQL 8.0 + IDEA + Tomcat。系统采用分层架构设计，包括表现层、控制层、业务层和数据访问层，实现了MVC设计模式。

## 运行项目
1. 配置MySQL数据库
2. 复制`application.properties.example`为`application.properties`
3. 修改`application.properties`中的数据库连接信息
4. 运行`mvn spring-boot:run`
5. 访问 http://localhost:8080

### 安全配置说明
项目使用示例配置文件来避免敏感信息泄露：
- `application.properties.example`：配置模板（已提交到Git）
- `application.properties`：实际配置（已添加到.gitignore）

首次运行时，请执行：
```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

然后修改`application.properties`中的数据库凭据。

## 项目结构
```
src/main/java/com/php/design_patten_demo/
├── config/          # 配置类
├── controller/       # 控制器层
├── dto/            # 数据传输对象
├── entity/          # 实体类
├── enums/          # 枚举类
├── exception/       # 异常处理
├── pattern/         # 设计模式实现
│   ├── chain/       # 责任链模式
│   ├── observer/    # 观察者模式
│   ├── singleton/    # 单例模式
│   ├── state/       # 状态模式
│   └── strategy/    # 策略模式
├── repository/      # 数据访问层
├── service/         # 业务逻辑层
└── DesignPattenDemoApplication.java
```

## 数据库设计
系统使用MySQL数据库，包含以下主要表：
- user：用户信息
- product：商品信息
- cart：购物车信息
- sys_order：订单信息
- order_item：订单项信息
- approval_request：进货申请信息
- notification：通知信息

## 开发环境
- IDE：IntelliJ IDEA
- 构建工具：Maven
- 版本控制：Git
- 数据库：MySQL 8.0

## 作者信息
- 作者：潘宏培
- 学号：39
- 班级：软工232班
- 学校：广州航海学院