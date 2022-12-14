# easytodo

[具体介绍](https://blog.csdn.net/weixin_39169535/article/details/125844737)

## 🎈概述
在习惯使用springboot开发代码之后，在我们自己进行**桌面程序**编码的时候，发现很多的不方便，比如**连接数据库**，读取配置文件，**循环依赖**，日志记录，**定时任务**等等。这篇文章详细介绍了使用springboot搭建一个桌面程序easytodo，一个桌面便签和任务，使用jpa+h2作为本地存储，打包成windows应用。

## 🤖项目介绍

- [x] 支持快速添加任务
- [x] 快速完成状态更新
- [ ] 任务支持分类
- [x] 支持统计
- [x] 桌面展示
- [x] 静态展示，单一颜色，防止审美疲劳

## 🦜UI设计
>使用figma工具设计的，具体页面地址  [easytodo页面设计](https://www.figma.com/file/JBLQJxrGSJqrUCOdlX9s0z/Untitled?node-id=11:2)，设计要求是颜色**冷淡为主**，主要颜色是**淡蓝**色和**白色**，用**橙色**作为点缀😏。设计了三个logo，个人比较喜欢第一个，可惜已经别人用了，最后采用的第三个，我没有去抄袭别人的图标，确实就是按照功能直接设计出来的，圆形打个勾勒表示任务完成。
![在这里插入图片描述](https://img-blog.csdnimg.cn/fea7f0da47084a8ca99d107485975265.png)

## 效果展示
![效果展示](https://img-blog.csdnimg.cn/15a68a10630a491984de180ec8e7796b.gif)


## 打包应用
```shell
pom文件中配置jre地址
执行mvn package
解压zip后，可以直接点击运行
```

![效果展示](https://img-blog.csdnimg.cn/5e6683afb30640f19da72fdc5ceb7888.png)



