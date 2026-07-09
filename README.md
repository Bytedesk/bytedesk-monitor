# bytedesk-monitor

Spring Boot Admin Server for monitoring [Bytedesk](https://github.com/Bytedesk/bytedesk) services.

**Language:** [English](README.md) | [中文](readme.zh.md)

> **Mirror:** [GitHub](https://github.com/Bytedesk/bytedesk-monitor) | [Gitee](https://gitee.com/bytedesk/bytedesk-monitor)

## Overview

bytedesk-monitor is a dedicated monitoring dashboard built on **Spring Boot Admin Server**. It provides centralized health checks, metrics, log viewing, and environment inspection for all registered Bytedesk Spring Boot applications.

## Tech Stack

| Component          | Version |
| ------------------ | ------- |
| Spring Boot        | 3.5.16  |
| Spring Boot Admin  | 3.5.9   |
| Java               | 21      |

## Architecture

```bash
┌─────────────────┐         register / heartbeat          ┌─────────────────────┐
│  bytedesk-starter │ ──────────────────────────────────▶  │  bytedesk-monitor    │
│  (port 9003)      │  actuator endpoints exposed          │  (port 9103)         │
│  SBA Client       │                                      │  SBA Server          │
└─────────────────┘                                      └─────────────────────┘
                                                                │
                                                         Admin UI at
                                                     http://127.0.0.1:9103
```

- **Monitor Server** (`bytedesk-monitor`): Spring Boot Admin Server UI on port `9103`
- **Client** (`bytedesk-starter`): Registers itself via `spring-boot-admin-starter-client`, exposes actuator endpoints

## Quick Start

### Prerequisites

- JDK 21
- Maven 3.x (or use the bundled `mvnw`)

### 1. Start the Monitor Server

```bash
cd bytedesk-monitor
./mvnw install -Dmaven.test.skip=true
./mvnw spring-boot:run
```

### 2. Start the Bytedesk Application

The bytedesk starter already includes `spring-boot-admin-starter-client` and is pre-configured to register with the monitor server.

```bash
cd bytedesk-3x
./starter/mvnw -f starter/pom.xml spring-boot:run
```

### 3. Open the Dashboard

Visit [http://127.0.0.1:9103](http://127.0.0.1:9103) in your browser.

| Field    | Default Value |
| -------- | ------------- |
| Username | `admin`       |
| Password | `admin`       |

> ⚠️ **Production**: Override defaults via `SPRING_SECURITY_USER_NAME` / `SPRING_SECURITY_USER_PASSWORD` environment variables.

## Configuration

### Server (`application.properties`)

```properties
server.port=9103
spring.boot.admin.ui.title=Bytedesk Monitor
spring.boot.admin.monitor.status-interval=10000ms
spring.boot.admin.monitor.status-lifetime=60000ms
spring.boot.admin.monitor.default-timeout=10000ms

# Security — Admin UI login credentials
spring.security.user.name=admin
spring.security.user.password=admin
```

### Client (in bytedesk-starter)

```properties
spring.boot.admin.client.url=http://127.0.0.1:9103
spring.boot.admin.client.username=admin
spring.boot.admin.client.password=admin
spring.boot.admin.client.instance.name=bytedesk-starter
spring.boot.admin.client.instance.service-base-url=http://127.0.0.1:9003
```

### Mail Notifications (`application-open.properties`)

Use environment variables for SMTP credentials and recipients in public or production deployments.

```properties
spring.mail.host=${SPRING_MAIL_HOST:smtp.qiye.aliyun.com}
spring.mail.port=${SPRING_MAIL_PORT:465}
spring.mail.username=${SPRING_MAIL_USERNAME:support@example.com}
spring.mail.password=${SPRING_MAIL_PASSWORD:}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.ssl.enable=true
spring.mail.properties.mail.smtp.ssl.trust=${SPRING_MAIL_SMTP_SSL_TRUST:smtp.qiye.aliyun.com}

spring.boot.admin.notify.mail.enabled=true
spring.boot.admin.notify.mail.to=${SPRING_BOOT_ADMIN_NOTIFY_MAIL_TO:demo@example.com}
spring.boot.admin.notify.mail.from=${SPRING_BOOT_ADMIN_NOTIFY_MAIL_FROM:Bytedesk Monitor <support@example.com>}
```

## Features

- **Health Dashboard** — live status of all registered services
- **Metrics** — JVM memory, threads, GC, HTTP request metrics
- **Log Viewer** — browse and search application logs in real time
- **Environment** — inspect Spring environment properties and system details
- **Thread Dump** — capture and analyze JVM thread dumps
- **Notifications** — alerts on status changes (offline/online)

## Screenshots

### Applications Overview

![Applications Overview](images/bytedesk-monitor-apps.png)

### Application Detail

![Application Detail](images/bytedesk-monitor-detail.png)

### Event Journal

![Event Journal](images/bytedesk-monitor-journal.png)

![Event logs](/img/monitor/bytedesk-monitor-logs.png)

## Quartz Task

![Quartz Task](/img/monitor/bytedesk-monitor-quartz.png)

## Thread Dump

![Thread Dump](/img/monitor/bytedesk-monitor-threaddump.png)

## Related Links

- [Spring Boot Admin Docs](https://docs.spring-boot-admin.com/current/getting-started.html)
- [Bytedesk](https://github.com/Bytedesk/bytedesk)
