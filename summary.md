# Desktop功能需求与设计实现文档
内容：

- 项目简介
- 功能需求
- 设计实现

# 项目简介
本项目属于openthos项目的一部分，提供电脑版的Launcher。

### 当前开发人员 (20161001-…)
王之旭 陈鹏 卢宁 罗俊欢

# 功能需求
### 桌面效果图
![2016-08-22效果图](image/Launcher-2016-08-22.png)
<br />  
![2016-08-10效果图](image/Launcher-2016-08-10.png)
<br />

### 桌面基本元素
我的电脑，回收站，用户个人文件，文件夹<br />*注：APP快捷方式第一版暂不考虑*

### 桌面基本操作
左键单击，左键双击，右键单击，左键按住拖动

### 桌面基本操作详细说明
|元素|操作|描述|
|---|---|---|
|普通文档|双击|打开文档关联的程序
|普通文档|右键单击|打开菜单
|普通文档|单击|选中
|普通文档|拖动|图标自由拖动，位置与网格对齐
|普通文档|Ctrl+D|快速删除到回收站
|普通文档|Delete|快速删除到回收站
|普通文档|Shift+Delete|快速永久删除文件
|普通文档|F2|快速重命名被选中的文件
|普通文档|Ctrl+C|快速复制
|普通文档|Ctrl+X|快速剪贴
|普通文档|Enter|快速打开
|空白处|右键单击|打开桌面菜单
|空白处|左键拖动框选|选择文件
|空白处|Shift+左键|选择文件
|空白处|Ctrl+左键|选择文件
|空白处|左键拖动|多选文件
|任意状态|F5|排序
|普通文档|Ctrl+V|快速粘贴

## 右键菜单
### 右键桌面空白处打开桌面菜单
|元素|操作|描述|
|---|---|---|
|粘贴|单击|粘贴|
|排序|放置鼠标在上面|出现排序方式子菜单并选择相应的排序方式|
|新建文件夹|单击|在桌面新建文件夹|
|新建文件|单击|在桌面新建文件|
|显示设置|单击|打开显示设置窗口|
|更改壁纸|单击|打开壁纸设置窗口|

### 右键我的电脑
|元素|操作|描述|
|---|---|---|
|打开|单击|打开文件管理器
|关于本机|单击|进入关于本机设置

### 右键普通文件图标
|元素|操作|描述|
|---|---|---|
|打开|单击|打开应用程序
|打开方式|单击|出现打开方式选择对话框
|压缩|单击|打开压缩对话框
|解压|单击|打开解压对话框
|剪切|单击|剪切
|复制|单击|复制
|删除|单击|打开删除对话框询问是否删除，确定后放入回收站
|重命名|单击|重命名
|属性|单击|出现属性对话框

### 右键回收站
|元素|操作|描述|
|---|---|---|
|打开|单击|回收站
|清空回收站|单击|清空回收站中的所有文件

### 压缩 解压缩

   - 支持将普通文件压缩成tar，zip
   - 支持将tar文件进一步压缩成gz，bz2
   - 支持解压 tar，zip，rar等常见的压缩文件格式
  
### 属性  
|元素|操作|描述|
|---|---|---|
|文件类型|无|显示文件类型
|打开方式|无|修改默认打开方式
|位置|无|显示文件所在位置
|大小|无|显示文件的大小
|占用空间|无|显示文件所占用的空间
|创建时间|无|显示文件的创建时间
|修改时间|无|显示文件的修改时间
|访问时间|无|显示文件的访问时间
|用户|左键点击可修改|显示文件的用户权限
|群组|左键点击可修改|显示文件的群组权限
|其他|左键点击可修改|显示文件的其他权限
|确定|左键点击|确定修改
|取消|左键点击|取消
|应用|左键点击|应用修改
 
# 设计实现
## 代码结构及说明
代码的主要结构如图所示：[Desktop代码结构说明.md](https://github.com/openthos/desktop-analysis/blob/master/doc/Desktop代码结构说明.md) <br />

## 功能设计与实现
功能设计与实现：[Desktop设计与实现.md](https://github.com/openthos/desktop-analysis/blob/master/doc/Desktop设计与实现.md) <br />

## 工程文件地址
请查看：[OpenthosLauncher](https://github.com/openthos/desktop-analysis/tree/master/OpenthosLauncher) <br />
如果在Linux环境下编译，可能会出现一些找不到的错误，这时候，只需要将 /main/java/Android 目录整体删除 <br />
并在build.gradle增加   compile 'com.android.support:recyclerview-v7:22.+'  <br />

## 与Launcher3合并后工程文件地址（壁纸切换）<br />
需要将OpenthosLauncher工程所有的R文件改成Launcher3的R文件，并且手动融合两个版本的res文件 <br />
并且修改原来Launcher3的Android.mk文件中LOCAL_MODULE字段，改成其他任意字段都可以。 <br />
请查看：[OtoLauncher](https://github.com/openthos/desktop-analysis/tree/master/OtoLauncher) <br />


