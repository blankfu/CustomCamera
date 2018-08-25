# CustomCamera

## 权限  需要打开相机的权限和写入sdcard权限

-相机权限  <uses-permission android:name="android.permission.CAMERA" />
-存储权限  <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

## 创建自定义摄像头

-检测和访问摄像机 - 创建代码以检查摄像机是否存在并请求访问。  
-创建预览类 - 创建扩展SurfaceView并实现SurfaceHolder界面的相机预览类。这个类预览来自相机的实时图像。  
-构建预览布局 - 一旦你拥有相机预览类，创建一个包含预览和你想要的用户界面控件的视图布局。  
-Capture的监听器 - 为您的接口控件连接侦听器，以响应用户操作（例如按下按钮）开始捕获图像或视频。  
-捕获和保存文件 - 设置用于捕获图片或视频和保存输出的代码。  
-释放相机 - 使用相机后，应用程序必须正确释放它以供其他应用程序使用。  



