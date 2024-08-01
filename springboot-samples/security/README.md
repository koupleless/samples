```html
<div align="center">
    <a href="./README.md">English</a> | 简体中文
</div>

# Experiment Content: Module and Base Integration

Make sure to have Maven installed locally with a version greater than 3.9.0.

## Experiment Application

### Base

The base is a standard Spring Boot application transformed into a foundational layer (base). The modifications include adding the following dependencies in the `pom.xml`:

```xml
<dependency>
    <groupId>com.alipay.sofa.koupleless</groupId>
    <artifactId>koupleless-base-starter</artifactId>
    <version>${koupleless.runtime.version}</version>
</dependency>
<!-- end dynamic module related dependencies -->
<!-- added dependency for deploying multiple web applications in single Tomcat host mode -->
<dependency>
    <groupId>com.alipay.sofa</groupId>
    <artifactId>web-ark-plugin</artifactId>
</dependency>
<!-- end single host deployment dependency -->
<!-- to ensure third-party dependencies are compatible with koupleless mode, add the following build plugin -->
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

### Biz

1. The biz is a standard Spring Boot application. After introducing security dependencies as `provided`, the module cannot reuse the base's security configuration and HTML files, which means that the module needs to add its own `SecurityConfig`, etc.
2. Modify the packaging plugin to use the `sofa-ark` packaging method for the biz module, packaging it as an ark biz jar file. The packaging plugin configuration is as follows:

```xml
<dependency>
    <groupId>com.alipay.sofa.koupleless</groupId>
    <artifactId>koupleless-app-starter</artifactId>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>org.thymeleaf.extras</groupId>
    <artifactId>thymeleaf-extras-springsecurity6</artifactId>
    <version>3.1.2.RELEASE</version>
    <scope>provided</scope>
</dependency>
<!-- modify the packaging plugin to use the sofa-ark biz packaging plugin, packaging into an ark biz jar -->
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
        <!-- need to change web context path under single host -->
        <webContextPath>${bizName}</webContextPath>
        <declaredMode>true</declaredMode>
    </configuration>
</plugin>
```

Note that you need to assign different values to the web context path for different biz modules to successfully install multiple web applications in a single Tomcat host.

## Experiment Steps

### Start the Base Application

You can use IDEA run to start the base application.

### Package the Biz Application (biz1)

Execute `mvn clean package -Dmaven.test.skip=true` in the `samples/springboot-samples/security/security-biz1` directory to package the module. After packaging, you can find the packaged `ark-biz` jar files in the target directory of each bundle.

### Install the Biz Application (biz1)

#### Use curl or arkctl command to install biz1

```shell
curl --location --request POST 'localhost:1238/installBiz' \
--header 'Content-Type: application/json' \
--data '{
    "bizName": "biz",
    "bizVersion": "0.0.1-SNAPSHOT",
    // local path should start with file://, also support remote url which can be downloaded
    "bizUrl": "file:///Users/xxxx/xxxx/Code/koupleless/samples/springboot-samples/service/sample-service-biz/biz-bootstrap/target/biz-bootstrap-0.0.1-SNAPSHOT-ark-biz.jar"
}'
```

or

```shell
arkctl deploy /path/to/security-biz1-0.0.1-SNAPSHOT-ark-biz.jar
```

### Send Request for Verification

#### Verify Base Module Calls

1. Access the web service of the base:

```shell
curl http://localhost:8080
```

It should redirect to the login page. After entering the username and password, it returns `hello to base deploy`.

2. Access the web service of biz1:

```shell
curl http://localhost:8080/biz1/
```

It should redirect to the login page. After entering the username and password, it returns `hello to biz1 deploy`.

## Notes

1. This demonstration mainly verifies simple applications. For more complex applications, make sure to slim down the modules. Dependencies available in the base should be set as `provided` in the module whenever possible to reuse the base's dependencies.
2. When verifying the module functionality, append a slash to the web endpoint, for example, `curl http://localhost:8080/biz1/` instead of `http://localhost:8080/biz1`.
```
