package com.test.runtimepermissions;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    //需要权限才能运行的方法
    @NeedsPermission({Manifest.permission.CALL_PHONE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void test() {
        File file = Environment.getExternalStorageDirectory();
        File file1 = new File(file, "111.txt");
        if (file1.exists()) {
            file1.delete();
        }
        try {
            file1.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //需要权限才能运行的方法
    @NeedsPermission(Manifest.permission.CAMERA)
    public void capture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    //权限拒绝后 选择了不在提示
    @OnNeverAskAgain(Manifest.permission.CAMERA)
    public void paizhaox(){
        Toast.makeText(this,"永久拒绝拍照权限",Toast.LENGTH_SHORT).show();
    }

    //拒绝权限时回掉
    @OnPermissionDenied(Manifest.permission.CAMERA)
    public void paizhao(){
        Toast.makeText(this,"没有拍照权限",Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.tv,R.id.tv2})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv:
                MainActivityPermissionsDispatcher.testWithCheck(this);
                break;
            case R.id.tv2:
                MainActivityPermissionsDispatcher.captureWithCheck(this);
                break;
        }
    }

    //权限拒绝提示 必须在回掉时调用
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
