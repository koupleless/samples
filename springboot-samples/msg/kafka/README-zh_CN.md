<div align="center">

[English](./README.md) | 简体中文

</div>

# 实验内容
## 实验应用
### base
base 为普通 springboot 改造成的基座，改造内容为在 pom 里增加如下依赖
```xml


<dependency>
    <groupId>com.alipay.sofa.koupleless</groupId>
    <artifactId>koupleless-base-starter</artifactId>
    <version>${koupleless.runtime.version}</version>
</dependency>
<!-- end 动态模块相关依赖 -->

<!-- 这里添加 tomcat 单 host 模式部署多web应用的依赖 -->
<dependency>
    <groupId>com.alipay.sofa</groupId>
    <artifactId>web-ark-plugin</artifactId>
</dependency>
<!-- end 单 host 部署的依赖 -->

<!-- log4j2 相关依赖 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-log4j2</artifactId>
</dependency>

<!-- log4j2 异步队列 -->
<dependency>
    <groupId>com.lmax</groupId>
    <artifactId>disruptor</artifactId>
    <version>${disruptor.version}</version>
</dependency>
<dependency>
    <groupId>com.alipay.sofa.koupleless</groupId>
    <artifactId>koupleless-log4j2-starter</artifactId>
    <version>${koupleless.runtime.version}</version>
</dependency>
<!-- end log4j2 依赖引入 -->

<!-- 引入 kafka 依赖 -->
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
<!-- end kafka -->

<!-- 为了让三方依赖和 koupleless 模式适配，需要引入以下构建插件 -->
<plugin>
    <groupId>com.alipay.sofa.koupleless</groupId>
    <artifactId>koupleless-base-build-plugin</artifactId>
    <version>${koupleless.runtime.version}</version>
    <executions>
        <execution>
            <goals>
                <goal>add-patch</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

### biz
biz 包含两个模块，分别为 biz1 和 biz2, 都是普通 springboot，修改打包插件方式为 sofaArk biz 模块打包方式，打包为 ark biz jar 包，打包插件配置如下：
```xml
<!-- 模块需要引入专门的 log4j2 adapter -->
<dependency>
    <groupId>com.alipay.sofa.koupleless</groupId>
    <artifactId>koupleless-adapter-log4j2</artifactId>
    <version>${koupleless.runtime.version}</version>
    <scope>provided</scope>
</dependency>
<!-- 引入 kafka 依赖 -->
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
    <scope>provided</scope>
</dependency>
<!-- end kafka -->

<!-- 修改打包插件为 sofa-ark biz 打包插件，打包成 ark biz jar -->
<plugin>
    <groupId>com.alipay.sofa</groupId>
    <artifactId>sofa-ark-maven-plugin</artifactId>
    <version>${sofa.ark.version}</version>
    <executions>
        <execution>
            <id>default-cli</id>
            <goals>
                <goal>repackage</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <skipArkExecutable>true</skipArkExecutable>
        <outputDirectory>./target</outputDirectory>
        <bizName>${bizName}</bizName>
        <!-- 单host下需更换 web context path -->
        <webContextPath>${bizName}</webContextPath>
        <declaredMode>true</declaredMode>
    </configuration>
</plugin>
```
注意这里将不同 biz 的web context path 修改成不同的值，以此才能成功在一个 tomcat host 里安装多个 web 应用。


## 实验步骤

### 构建与启动 kafka 服务端

#### 运行镜像
```shell
docker run -p 9092:9092 -p 9093:9093 --name kafka-3.6.2 -e KAFKA_ENABLE_KRAFT=yes -e KAFKA_CFG_NODE_ID=1 -e KAFKA_CFG_PROCESS_ROLES=controller,broker -e KAFKA_CFG_LISTENERS=PLAINTEXT://:9092,CONTROLLER://:9093 -e KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 -e KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT -e KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=1@localhost:9093 -e KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER -e KAFKA_KRAFT_CLUSTER_ID=abcdefghijklmnopqrstuv -e ALLOW_PLAINTEXT_LISTENER=yes -e KAFKA_CFG_AUTO_CREATE_TOPICS_ENABLE=true -e KAFKA_BROKER_ID=1 bitnami/kafka:3.6.2
```

#### 执行 mvn clean package -DskipTests
可在各 bundle 的 target 目录里查看到打包生成的 ark-biz jar 包
#### 启动基座应用 base，确保基座启动成功
#### 执行 curl 命令安装 biz1 和 biz2
```shell
curl --location --request POST 'localhost:1238/installBiz' \
--header 'Content-Type: application/json' \
--data '{
    "bizName": "biz1",
    "bizVersion": "0.0.1-SNAPSHOT",
    // local path should start with file://, alse support remote url which can be downloaded
    "bizUrl": "file:///path/to/springboot-samples/samples/web/tomcat/biz1/target/biz1-kafka-0.0.1-SNAPSHOT-ark-biz.jar"
}'
```

```shell
curl --location --request POST 'localhost:1238/installBiz' \
--header 'Content-Type: application/json' \
--data '{
    "bizName": "biz2",
    "bizVersion": "0.0.1-SNAPSHOT",
    // local path should start with file://, alse support remote url which can be downloaded
    "bizUrl": "file:///path/to/springboot-samples/samples/web/tomcat/biz2/target/biz2-kafka-0.0.1-SNAPSHOT-ark-biz.jar"
}'
```

如果想验证卸载也可以执行
```shell
curl --location --request POST 'localhost:1238/uninstallBiz' \
--header 'Content-Type: application/json' \
--data '{
    "bizName": "biz1",
    "bizVersion": "0.0.1-SNAPSHOT"
}'
```

### 发起请求验证

```shell
curl http://localhost:8080/biz1/send/fadsfasdfa
```
返回 `hello to /biz1 deploy`

且日志里能看到 
```text
INFO  rest.SampleController - =================================
INFO  rest.SampleController - biz1 consumer input value: fadsfasdfa
INFO  rest.SampleController - =================================
```

```shell
curl http://localhost:8080/biz2/send/fadsfasdfa
```
返回 `hello to /biz2 deploy`

且日志里能看到
```text
INFO  rest.SampleController - =================================
INFO  rest.SampleController - biz2 consumer input value: fadsfasdfa
INFO  rest.SampleController - =================================
```

## 注意事项
这里主要使用简单应用做验证，如果复杂应用，需要注意模块做好瘦身，基座有的依赖，模块尽可能设置成 provided，尽可能使用基座的依赖。

