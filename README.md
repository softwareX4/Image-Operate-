# Image-Operate-
CSU software CV lab. Write by opencv for java

在Intelij idea中，project structure->Modules引入opencv for java的jar包依赖，这个包一般存在于\opencv\build\java 这个路径下，对应的dll文件在x64中。
在Run/Debug Configurations中 VM options写入：-Djava.library.path=src/resource



具体实验细节在《实验指导书》中。