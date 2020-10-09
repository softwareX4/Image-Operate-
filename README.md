# Image-Operate-
CSU software CV lab. Write by opencv for java

在Intelij idea中，project structure->Modules引入opencv for java的jar包依赖，这个包一般存在于\opencv\build\java 这个路径下，对应的dll文件在x64中。
在Run/Debug Configurations中 VM options写入：-Djava.library.path=src/resource



具体实验细节在[《实验指导书》](https://github.com/softwareX4/Image-Operate-/blob/master/ImageOperate/实验指导书.docx)中。



- 加载图片——>load image

  ![image-20201009181954995](https://github.com/softwareX4/Image-Operate-/blob/master/ImageOperate/images/image-20201009181954995.png)

  

  ## 图像变换

- 灰度图、膨胀、腐蚀及灰度图的傅里叶变换

![image-20201009182054479](https://github.com/softwareX4/Image-Operate-/blob/master/ImageOperate/images/image-20201009182054479.png)

![image-20201009182150539](https://github.com/softwareX4/Image-Operate-/blob/master/ImageOperate/images/image-20201009182150539.png)

- 通用运算

  ![image-20201009182243753](https://github.com/softwareX4/Image-Operate-/blob/master/ImageOperate/images/image-20201009182243753.png)

- 旋转、平移和缩放

  ![image-20201009182551617](https://github.com/softwareX4/Image-Operate-/blob/master/ImageOperate/images/image-20201009182551617.png)

  

## 图像增强



### 对比度增强

- 对比度、亮度、log变换 、拉普拉斯算子

![image-20201009182707910](https://github.com/softwareX4/Image-Operate-/blob/master/ImageOperate/images/image-20201009182707910.png)

- 伽马变换

  ![image-20201009182803712](https://github.com/softwareX4/Image-Operate-/blob/master/ImageOperate/images/image-20201009182803712.png)

  ### 直方图修正

  

- 直方图均值化

  ![image-20201009182825535](https://github.com/softwareX4/Image-Operate-/blob/master/ImageOperate/images/image-20201009182825535.png)

  

  ### 平滑

- 单一滤波（左下为添加噪点后，右下为滤波后）

  ![image-20201009182920805](https://github.com/softwareX4/Image-Operate-/blob/master/ImageOperate/images/image-20201009182920805.png)

  

- 高斯滤波

  ![image-20201009182939911](https://github.com/softwareX4/Image-Operate-/blob/master/ImageOperate/images/image-20201009182939911.png)

  

- 双边滤波

  ![image-20201009182952738](https://github.com/softwareX4/Image-Operate-/blob/master/ImageOperate/images/image-20201009182952738.png)

- 中值滤波

  ![image-20201009183005465](https://github.com/softwareX4/Image-Operate-/blob/master/ImageOperate/images/image-20201009183005465.png)

  

  ### 锐化

- sobel算子锐化、拉普拉斯锐化（左下为灰度图，右上为拉普拉斯锐化后，右下为Sobel锐化后）

  ![image-20201009183334046](https://github.com/softwareX4/Image-Operate-/blob/master/ImageOperate/images/image-20201009183334046.png)



## 边缘检测



- Sobel算子边缘检测：左下；

  Scharr滤波器：右下；

  Laplace算子边缘检测：右上

![image-20201009183514072](https://github.com/softwareX4/Image-Operate-/blob/master/ImageOperate/images/image-20201009183514072.png)



- Canny算子边缘检测：左下；

  Hildreth算子边缘检测：右下；

  ![image-20201009183803291](https://github.com/softwareX4/Image-Operate-/blob/master/ImageOperate/images/image-20201009183803291.png)
