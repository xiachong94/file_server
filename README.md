# Browse
![](https://i.loli.net/2017/08/25/599fcabc4815a.png "Browse")

用Java重新实现了一套https://github.com/XhstormR/Browse
基于以下技术，实现了类似于 Nginx 的 autoindex 功能，能够 **索引本地文件**，但额外提供了 **文件上传功能** 和更加 **友好的界面**。

* Java
* Spring
* Spring MVC
* Spring Boot
* Thymeleaf
* Maven

## Run
```
java -jar file_server.jar
```

访问 http://127.0.0.1:9090/

## Configuration


### 索引目录

config.base_path=D:/Download/

### 文件上传功能

config.enable_upload=false
