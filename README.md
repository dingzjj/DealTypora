# DealTypora是一个专门处理makedown的软件

## 解决问题：
在本机上的md文件发给别的主机，只有上面有图片，大家都知道肯定是看不到图片的
于是本程序解决了该问题
### 步骤：
1.将要打包的md文件放到DealTypora\PackAndParse\src\main\resources中
2.在本机上运行pack
3.将整个程序发给别的主机
4.在别的主机上运行parse

## 目前有以下功能
### 1.能打包markdown文件
pack能将md文件所用的图片打包到一个文件夹里
可以将打包后的md文件发给其他主机，然后启动parse能使md文件在其他主机上正常可见

## pack使用方法：
![image](https://user-images.githubusercontent.com/101155146/203324402-7e909b30-3019-4409-bc3c-516dead5b0ad.png)

## parse使用方法
![image](https://user-images.githubusercontent.com/101155146/203324878-7a135707-b68e-4ff8-8820-f891521e1e47.png)
> choose the dir 能选择保持的地方
## 运行环境
jdk1.8


