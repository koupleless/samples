<div align="center">

[English](./README.md) | 简体中文

</div>

# 多模块如何使用 skywalking 进行 tracing
为了让不同模块 trace 采集到不同的地方，可以通过设置不同的 service_name 来实现，但是目前 skywalking 不支持设置多个 service_name，参考 skywalking 官方回复 [apache/skywalking](https://github.com/apache/skywalking/discussions/9373)，所以更换方式将不同模块日志打印到不同目录来实现，可以通过采集不同目录日志来实现多模块的 tracing

# 实验应用

## base

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

<dependency>
<groupId>org.apache.skywalking</groupId>
<artifactId>apm-toolkit-log4j-2.x</artifactId>
<version>${skywalking.log4j.version}</version>
</dependency>

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

## biz

biz 包含两个模块，分别为 biz1 和 biz2, 都是普通 springboot，修改打包插件方式为 sofaArk biz 模块打包方式，打包为 ark biz jar
包，打包插件配置如下：

```xml
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

# 实验内容1: 动态部署

## 实验任务

1. 执行 mvn clean package -DskipTests
2. 启动基座应用 base，增加启动参数 `-javaagent:/Users/youji.zzl/Downloads/skywalking-agent/skywalking-agent.jar -Dskywalking.agent.service_name=base`，确保基座启动成功
3. 执行 arkctl deploy 命令安装 biz1 和 biz2，如果想验证热部署，可以通过多次卸载多次部署，然后验证请求是否正常
4. 发起请求验证

```shell
curl http://localhost:8080/biz1/
```
返回
```text
2024-11-13 15:44:07,355 [[SW_CTX: [base,9a554a0c999e455f86adff334f1ba084@30.177.35.65,N/A,N/A,-1]]  - /// - ] INFO  skywalking.SampleController - hello to biz1 deploy, run in com.alipay.sofa.ark.container.service.classloader.BizClassLoader@58b49c9c
```

```shell
curl http://localhost:8080/biz2/
```

返回
```text
2024-11-13 15:44:09,932 [[SW_CTX: [base,9a554a0c999e455f86adff334f1ba084@30.177.35.65,N/A,N/A,-1]]  - /// - ] INFO  skywalking.SampleController - hello to biz2 deploy, run in com.alipay.sofa.ark.container.service.classloader.BizClassLoader@87dca5e
```

5. 查看两个模块的日志分别打印在不同目录里，然后通过采集不同目录的日志来区分不同模块的 trace

## 注意事项
这里主要使用简单应用做验证，如果复杂应用，需要注意模块做好瘦身，基座有的依赖，模块尽可能设置成 provided，尽可能使用基座的依赖。
