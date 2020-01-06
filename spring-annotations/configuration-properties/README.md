# Spring注解@ConfigurationProperties
@ConfigurationProperties是一个外部化配置注解，比如外部的`.properties`属性文件。使用该注解，可以实现把外部的属性绑定到Bean实例，也可以实现在绑定的过程对外部属性进行合法性验证。

### 一、@ConfigurationProperties如何使用
@ConfigurationProperties注解通常放在类上使用，比如：
```java
@ConfigurationProperties(prefix = "student",ignoreInvalidFields = false,ignoreUnknownFields = false)
public class StudentProperties {}
```
也可以在放在@Configuration注解的类里面的@Bean注解的方法上，比如：
```java
@Configuration
public class TeacherConfig {
    @Bean
    @ConfigurationProperties(prefix = "teacher")
    public Teacher teacher(){//Spring会把@ConfigurationProperties注解的外部配置绑定到new Teacher()生成的实例对应属性上
        return new Teacher();
    }
}
```

**注意**：@ConfigurationProperties注解并不会去实例化Bean，也不会把实例化的Bean加入IOC容器，只是对实例后的Bean进行属性绑定以及验证属性值合法性。也
就是说@ConfigurationProperties注解不能单独使用，通常要和其它能实例化Bean并把Bean加入IOC容器的注解配合才能生效，通常有以下三种方案：

（1）. `@ConfigurationProperties+@Configuration/@Component`
```java
@Configuration
@ConfigurationProperties(prefix = "")
public class StudentProperties {}
```
（2）. `@ConfigurationProperties+@EnableConfigurationProperties(Xxx.class)`
```java
@ConfigurationProperties(prefix = "")
public class StudentProperties {}

@EnableConfigurationProperties(StudentProperties.class)
@SpringBootApplication
public class ConfigurationPropertiesApplication implements ApplicationRunner {

    @Autowired
    private StudentProperties studentProperties;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(studentProperties.toString());
    }
}
```
> @EnableConfigurationProperties源码解读：  
>Enable support for {@link ConfigurationProperties @ConfigurationProperties} annotated beans. {@code @ConfigurationProperties} beans can be registered in the standard way (for example using {@link Bean @Bean} methods) or, for convenience, can be specified directly on this annotation.  
>开启对@ConfigurationProperties注解Bean的支持，把@ConfigurationProperties注解的Bean注册进IOC容器。换句话说，Spring会扫描开启了@ConfigurationProperties注解的类进行实例化，并把@ConfigurationProperties指定前缀的属性值绑定到类实例。

（3）.`@ConfigurationProperties+@ConfigurationPropertiesScan`
```java
@ConfigurationProperties(prefix = "student",ignoreInvalidFields = false,ignoreUnknownFields = false)
public class StudentProperties {}

@ConfigurationPropertiesScan
@SpringBootApplication
public class ConfigurationPropertiesApplication implements ApplicationRunner {

    @Autowired
    private StudentProperties studentProperties;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(studentProperties.toString());
    }
}
```
> @ConfigurationPropertiesScan  
>看源码，Spring会扫描@ConfigurationPropertiesScan注解配置的包/类中@ConfigurationProperties注解的类进行实例化，然后把@ConfigurationProperties指定前缀的属性值绑定到类实例，并加入IOC容器。

### 二、@ConfigurationProperties注解类属性值绑定方式
（1）通过@ConfigurationProperties注解的类Setter方法
这种方式要求@ConfigurationProperties注解的类的属性必须要有Setter方法

（2）通过@ConfigurationProperties注解的类构造方法
通过构造方法来进行属性值绑定，必须使用`@ConstructorBinding`注解到类上或者构造方法上。比如：
```java
@ConfigurationProperties(prefix = "student",ignoreInvalidFields = false,ignoreUnknownFields = false)
public class StudentProperties {

    @ConstructorBinding
    public StudentProperties(String name, int age, Grade grade, School school, List<String> hobbyList, Duration allowLateTime) {
        this.name = name;
        this.age = age;
        this.grade = grade;
        this.school = school;
        this.hobbyList = hobbyList;
        this.allowLateTime = allowLateTime;
    }
}
```
### 三、属性值无法绑定时，重写`Converter 或 ConditionalConverter`接口来解决
如果`.properties`文件的属性值无法绑定到`@ConfigurationProperties`注解的类属性时，可以针对该属性自定义转换器来解决。自定义转换器必须实现`Converter 或ConditionalConverter`接口。
自定义转换器还必须使用`@ConfigurationPropertiesBinding`注解指名自己是一个配置属性绑定器，并加入IOC容器。在进行属性值绑定时，Spring会从IOC容器找特定绑定器进行转换绑定。比如：

```yaml
#properties配置文件
student:
  grade: 三年级
```
```java
@ConfigurationProperties(prefix = "student",ignoreInvalidFields = false,ignoreUnknownFields = false)
public class StudentProperties {

    /**
     * 年级
     */
    private Grade grade;
}

/**
 * @desc: Grade转换器
 */
@Component
@ConfigurationPropertiesBinding
public class GradeConverter implements Converter<String,Grade> {

    @Override
    public Grade convert(String source) {
        Grade grade = new Grade();
        grade.setName(source);
        return grade;
    }
}

/**
 * @desc: 年级
 */
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Grade {

    private String name;
    private String school;
    
}
```
### 四、属性值验证
（1）@ConfigurationProperties注解本身带有非法属性和未知属性验证
```java
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConfigurationProperties {

	/**
	 * Flag to indicate that when binding to this object invalid fields should be ignored.
	 * Invalid means invalid according to the binder that is used, and usually this means
	 * fields of the wrong type (or that cannot be coerced into the correct type).
	 * @return the flag value (default false)
	 */
	boolean ignoreInvalidFields() default false;

	/**
	 * Flag to indicate that when binding to this object unknown fields should be ignored.
	 * An unknown field could be a sign of a mistake in the Properties.
	 * @return the flag value (default true)
	 */
	boolean ignoreUnknownFields() default true;
}
```

（2）使用hibernate validator或 javax.validation验证框架进行验证
```java
@Validated
@Configuration
@ConfigurationProperties(prefix = "student",ignoreInvalidFields = false,ignoreUnknownFields = false)
public class StudentProperties {

    @NotEmpty(message = "学生姓名不能为空")
    private String name;
}
```
### 五、使用 Spring Boot Configuration Processor完成自动补全
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
    <optional>true</optional>
</dependency>
```
重新 build 项目之后，configuration processor会创建target/classes/META-INF/spring-configuration-metadata.json文件。
```json
{
  "groups": [
    {
      "name": "student",
      "type": "com.mapc.annotation.StudentProperties",
      "sourceType": "com.mapc.annotation.StudentProperties"
    }
  ],
  "properties": [
    {
      "name": "student.age",
      "type": "java.lang.Integer",
      "description": "年龄",
      "sourceType": "com.mapc.annotation.StudentProperties",
      "defaultValue": 0
    },
    {
      "name": "student.name",
      "type": "java.lang.String",
      "description": "姓名",
      "sourceType": "com.mapc.annotation.StudentProperties"
    }]
}
```
------
Demo源码：

#### 参考资料：

* [@ConfigurationProperties 注解使用姿势，这一篇就够了](https://www.jianshu.com/p/7f75936b573b)
* [@ConfigurationProperties加载外部配置](https://blog.csdn.net/qq_33745102/article/details/85720888)
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/reference/htmlsingle/#configuration-metadata-annotation-processor)

