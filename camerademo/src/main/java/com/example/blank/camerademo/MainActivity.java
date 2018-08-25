package com.example.blank.camerademo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.blank.camerademo.camera.CameraPreview;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FrameLayout frameLayout;
    private Camera mCamera;
    private Button mCapture;

    private boolean isSafeCapture = true;

    private int cameraID = Camera.CameraInfo.CAMERA_FACING_BACK;//后置摄像头
    private static final int REQUEST_CODE_PERMISSION = 0x001;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getPermission();
        }
        mCapture.setOnClickListener(v -> {
                    if (isSafeCapture) {
                        mCamera.takePicture(null, null, mPictureCallback);
                        isSafeCapture = false;
                    }
                }
        );

        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            mCamera = Camera.open(cameraID);//打开摄像头默认是后置摄像头
            frameLayout.addView(new CameraPreview(this, mCamera));
        }
    }

    @Override
    public void onBackPressed() {
        if (imageView.getVisibility() == View.VISIBLE) {
            imageView.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    private void initView() {
        imageView = ((ImageView) findViewById(R.id.iv));
        frameLayout = findViewById(R.id.camera_preview);
        mCapture = findViewById(R.id.button_capture);
    }

    /*private String mFilePath = Environment.getExternalStorageDirectory().getPath() + "/photo.png";

    private void takePhotoBySystemCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(mFilePath);
        Uri data;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            data = FileProvider.getUriForFile(MainActivity.this, "com.blank.fileProvider", file);
            // 给目标应用一个临时授权
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            data = Uri.fromFile(file);
        }
        // 指定存储路径，这样就可以保存原图了
        intent.putExtra(MediaStore.EXTRA_OUTPUT, data);
        // 拍照返回图片
        startActivityForResult(intent, 1);
    }*/

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

    private Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            mCamera.startPreview();
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            Matrix matrix = new Matrix();
            matrix.setRotate(90, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight
                    () / 2);
            if (cameraID == Camera.CameraInfo.CAMERA_FACING_FRONT) {//前置需要镜像翻转
                matrix.postScale(1, -1);   //镜像垂直翻转
            }
            Bitmap matrixBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                    bitmap.getWidth(), bitmap.getHeight(), matrix, true);


            imageView.setImageBitmap(matrixBitmap);
            imageView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);

            String name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format
                    (new Date()
                    ) + ".jpg";
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File
                    .separator + name;
            File file = new File(path);
            try {
                FileOutputStream outputStream = new FileOutputStream(file);
                matrixBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            isSafeCapture = true;
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        mCamera.stopPreview();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCamera.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCamera.startPreview();
    }
}
