# CustomCamera

## 所需权限清单  需要访问相机权限和写入sdcard权限

- 相机权限  
```xml
<uses-permission android:name="android.permission.CAMERA" />
```
- 存储权限  
```xml
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

## 申请权限

```java
@RequiresApi(api = Build.VERSION_CODES.M)
    private void getPermission() {
        int write = ContextCompat.checkSelfPermission(this, Manifest.permission
                .WRITE_EXTERNAL_STORAGE);
        int camera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (write == PackageManager.PERMISSION_DENIED || camera == PackageManager.PERMISSION_DENIED)
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest
                    .permission.CAMERA}, REQUEST_CODE_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "已获取权限", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "您拒绝权限", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    ```

## 创建自定义摄像头

   为应用程序创建自定义摄像头界面的一般步骤如下：

   - 创建预览类 - 创建扩展SurfaceView并实现SurfaceHolder界面的相机预览类。这个类预览来自相机的实时图像。
   - 构建预览布局 - 一旦你拥有相机预览类，创建一个包含预览和你想要的用户界面控件的视图布局。
   - Capture的监听器 - 为您的接口控件连接侦听器，以响应用户操作（例如按下按钮）开始捕获图像或视频。
   - 捕获和保存文件 - 设置用于捕获图片或视频和保存输出的代码。
   - 释放相机 - 使用相机后，应用程序必须正确释放它以供其他应用程序使用。







