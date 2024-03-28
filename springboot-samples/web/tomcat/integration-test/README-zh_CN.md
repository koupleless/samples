<div align="center">

[English](./README.md) | 简体中文

</div>

# tomcat 多模块部署的集成测试

tomcat 在多模块部署的模式下，多个模块也会复用同一个 tomcat 实例，以节省资源。
koupleless 的运行时也会自动地给模块分配不同的 context path，以避免模块之间 http 路径冲突。 </br>
为了快速验证这一特性，我们计划通过 koupleless-test-suite 提供的集成测试框架，快速地启动基座 + biz1 + biz2
模块，并验证复用 tomcat 实例的特性。

## 集成测试框架原理

模式原理介绍详看[这里](https://www.sofastack.tech/projects/sofa-boot/sofa-ark-multi-web-component-deploy/)

## 集成测试模块说明

### 步骤一：引入必要依赖

#### koupleless-test-suite 测试框架

```xml

<dependencies>
    <dependency>
        <groupId>com.alipay.sofa.koupleless</groupId>
        <artifactId>koupleless-test-suite</artifactId>
        <version>${koupleless.runtime.version}</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

#### base 和 biz 的项目依赖

```xml

<dependencies>
    <dependency>
        <groupId>com.alipay.sofa.web</groupId>
        <artifactId>base-web-single-host-bootstrap</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>com.alipay.sofa.web</groupId>
        <artifactId>base-web-single-host-facade</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>com.alipay.sofa.web</groupId>
        <artifactId>biz1-web-single-host</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>com.alipay.sofa.web</groupId>
        <artifactId>biz2-web-single-host</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
</dependencies>
```

### 步骤二：编写集成测试用例

#### 启动基座 + 模块

```java
@SneakyThrows
@BeforeClass
public static void setUpMultiApplication() {
    multiApp = new KouplelessTestMultiSpringApplication(KouplelessMultiSpringTestConfig
            .builder()
            .baseConfig(KouplelessBaseSpringTestConfig
                    .builder()
                    .packageName("com.alipay.sofa.web.base") // 基座的包名
                    .mainClass("com.alipay.sofa.web.base.BaseApplication") // 基座的启动类
                    .artifactId("base-web-single-host") // 基座的 artifactId
                    .build())
            .bizConfigs(Lists.newArrayList(
                    KouplelessBizSpringTestConfig
                            .builder()
                            .packageName("com.alipay.sofa.web.biz1") // 模块1的包名
                            .bizName("biz1") // 模块1的名称
                            .mainClass("com.alipay.sofa.web.biz1.Biz1Application") // 模块1的启动类
                            .artifactId("biz1-web-single-host") // 模块1的 artifactId
                            .build(),
                    KouplelessBizSpringTestConfig
                            .builder()
                            .packageName("com.alipay.sofa.web.biz2") // 模块2的包名
                            .bizName("biz2") // 模块2的名称
                            .mainClass("com.alipay.sofa.web.biz2.Biz2Application") // 模块2的启动类
                            .artifactId("biz2-web-single-host") // 模块2的 artifactId
                            .build()
            ))
            .build()
    );
    multiApp.run();
    Thread.sleep(1000);
}
```
#### 编写测试用例
该测试用例测试 http 接口是否按照预期地挂载在了同一个 tomcat 实例上。
```java
 public void testContextWebhookPathPrefixIsAdded() throws Throwable {
    Response resp = client.newCall(
            new Request.Builder().url("http://localhost:8080/").get().build()).execute();
    Assert.assertEquals("hello to base deploy", resp.body().string());

    resp = client.newCall(
            new Request.Builder().url("http://localhost:8080/biz1/").get().build()).execute();
    Assert.assertEquals("hello to biz1 deploy", resp.body().string());

    resp = client.newCall(
            new Request.Builder().url("http://localhost:8080/biz2/").get().build()).execute();
    Assert.assertEquals("hello to biz2 deploy", resp.body().string());

}
```


### 步骤三：执行测试

```shell
mvn test -pl integration-test 
```

## 总结
通过上面的实验，我们验证了 tomcat 多模块模式符合我们的预期。除此之外，我们可以发现，用集成测试框架来验证多 APP 模式是非常便捷的。</br>
在传统的验证工作流中，我们需要按照如下步骤来验证多 APP 模式是否正常工作:
1. 启动基座。
2. 安装模块。
3. 掉用 HTTP / RPC 方法。
4. 观察返回。

这些步骤充满了大量的命令行与 java 代码的交织，来回切换是非常繁琐的。 </br>
通过使用集成测试框架，我们可以在一个测试用例中完成所有的工作，验证逻辑和 java 测试直接集成，用户只需要使用 java 代码，大大提高了测试效率。