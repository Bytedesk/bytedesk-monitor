# bytedesk-monitor

基于 **Spring Boot Admin Server** 的 [Bytedesk](https://github.com/Bytedesk/bytedesk) 服务监控面板。

## 概述

bytedesk-monitor 是一个独立的监控服务，提供统一的健康检查、指标采集、日志查看和环境信息浏览功能，可集中管理所有已注册的 Bytedesk Spring Boot 应用。

## 技术栈

| 组件                | 版本    |
| ------------------- | ------- |
| Spring Boot         | 3.5.16  |
| Spring Boot Admin   | 3.5.9   |
| Java                | 21      |

## 架构

```bash
┌─────────────────┐         注册 / 心跳                    ┌─────────────────────┐
│  bytedesk-starter │ ──────────────────────────────────▶  │  bytedesk-monitor    │
│  (端口 9003)      │  暴露 actuator 端点                   │  (端口 9103)         │
│  SBA Client       │                                      │  SBA Server          │
└─────────────────┘                                      └─────────────────────┘
                                                                │
                                                         管理界面访问
                                                     http://127.0.0.1:9103
```

- **Monitor Server**（`bytedesk-monitor`）：Spring Boot Admin Server，默认端口 `9103`
- **Client**（`bytedesk-starter`）：通过 `spring-boot-admin-starter-client` 自动注册到监控服务端

## 快速开始

### 环境要求

- JDK 21
- Maven 3.x（或使用项目自带的 `mvnw`）

### 1. 启动监控服务

```bash
cd bytedesk-monitor
./mvnw spring-boot:run
```

### 2. 启动 Bytedesk 应用

bytedesk starter 已内置 `spring-boot-admin-starter-client` 并配置好自动注册。

```bash
cd bytedesk-3x
JASYPT_ENCRYPTOR_PASSWORD=<your-password> ./starter/mvnw -f starter/pom.xml spring-boot:run
```

### 3. 打开监控面板

浏览器访问 [http://127.0.0.1:9103](http://127.0.0.1:9103)。

## 配置说明

### 服务端（`application.properties`）

```properties
server.port=9103
spring.boot.admin.ui.title=Bytedesk Monitor
spring.boot.admin.monitor.status-interval=10000ms       # 健康检查间隔
spring.boot.admin.monitor.status-lifetime=60000ms       # 状态缓存有效期
spring.boot.admin.monitor.default-timeout=10000ms       # 请求超时
```

### 客户端（bytedesk-starter 中）

```properties
spring.boot.admin.client.url=http://127.0.0.1:9103              # Monitor Server 地址
spring.boot.admin.client.instance.name=bytedesk-starter          # 实例名称
spring.boot.admin.client.instance.service-base-url=http://127.0.0.1:9003  # 服务地址
```

## 功能特性

- **健康面板** — 实时查看所有注册服务的上下线状态
- **指标监控** — JVM 内存、线程、GC、HTTP 请求等核心指标
- **日志查看** — 在线浏览和搜索应用日志
- **环境信息** — 查看 Spring 环境变量、系统属性
- **线程转储** — 在线捕获和分析 JVM 线程 Dump
- **通知告警** — 服务状态变更时触发通知（离线/恢复）

## 相关链接

- [Spring Boot Admin 官方文档](https://docs.spring-boot-admin.com/current/getting-started.html)
- [Bytedesk 主项目](https://github.com/Bytedesk/bytedesk)
