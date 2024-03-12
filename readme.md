# 简介
#### android apt的通用配置框架，包含debug的自定义AbstractProcessor部分

# 环境配置
## Project Structure 配置
#### 1、Android Gradle Plugin Version:7.1.3
#### 2、Gradle Version 7.2
#### 3、Java Version 11.0.XX


## build.gradle 配置
```groovy
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_11
        targetCompatibility JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = '11'
    }
```

## gradle.properties配置
```properties
    org.gradle.daemon=true
    org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8 -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005
    #android.suppressUnsupportedCompileSdk=33
    org.gradle.java.home=对应java的本地目录
```

# debug的操作步骤
#### 1、创建 remote JVM debug（TestApt）， Use module classpath 是当前的 AbstractProcessor的module
#### 2、./gradlew --stop 关闭进程锁定
#### 3、./gradlew --daemon 进程锁定
#### 4、打断点，debug remote JVM （TestApt）
#### 5、clean build
#### 6、debug app module

