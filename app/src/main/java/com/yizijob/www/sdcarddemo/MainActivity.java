package com.yizijob.www.sdcarddemo;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yizijob.www.sdcarddemo.permission.PermissionHelper;
import com.yizijob.www.sdcarddemo.permission.callback.OnPermissionCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity implements OnPermissionCallback
{

    private PermissionHelper permissionHelper;
    //值唯一即可,这是为了返回做标识使用
    private final int REQUEST_SETTING = 10;
    final String[] permissionArrays = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_SMS};
    final int permissionSize = permissionArrays.length;
    final int[] permissionInfo = {R.string.open_storage_permit, R.string.open_sms_permit};
    final int infoSize = permissionInfo.length;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button btn = (Button) findViewById(R.id.btn_sd);

        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                doPermissionCheck();
            }
        });


    }

    /**
     * 检查是否拥有权限
     */
    private void doPermissionCheck()
    {
        permissionHelper = PermissionHelper.getInstance(this);
        permissionHelper
                .setForceAccepting(true) // default is false. its here so you know that it exists.
                .request(permissionArrays);
    }

    @Override
    public void onPermissionGranted(@NonNull String[] permissionName)
    {
        //权限点击允许
        String lastPermission = permissionName[permissionName.length - 1];
        if (lastPermission.equals(permissionArrays[permissionSize - 1]))
        {
            //权限点击允许
            doYourThings();
        }
        else
        {
            doPermissionCheck();
        }
    }

    @Override
    public void onPermissionDeclined(@NonNull String[] permissionName)
    {

    }

    @Override
    public void onPermissionPreGranted(@NonNull String permissionsName)
    {
        //权限已经打开了
        doYourThings();
    }

    @Override
    public void onPermissionNeedExplanation(@NonNull String permissionName)
    {
        //需要申请权限
        permissionHelper.requestAfterExplanation(permissionName);
    }

    @Override
    public void onPermissionReallyDeclined(@NonNull String permissionName)
    {
        boolean admitAppend = false;
        StringBuilder sb = new StringBuilder();
        for (int i = 0, length = permissionArrays.length; i < length; i++)
        {
            if (permissionArrays[i].equals(permissionName) || admitAppend)
            {
                if (i < infoSize)
                {
                    sb.append(getString(permissionInfo[i]));
                    admitAppend = true;
                }
            }
            if (i == length - 1)
            {
                if ("".equals(sb.toString()))
                {
                    Toast.makeText(MainActivity.this, R.string.open_permit, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this, sb.toString(), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                if (!"".equals(sb.toString()))
                {
                    sb.append("\n");
                }
            }
        }
        //禁止权限
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.parse("package:" + getPackageName());
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_SETTING);
    }

    //android6.0以下会触发
    @Override
    public void onNoPermissionNeeded()
    {
        doYourThings();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        permissionHelper.onActivityForResult(requestCode);
        //返回时重新进行检查
        if (requestCode == REQUEST_SETTING)
        {
            doPermissionCheck();
        }
    }

    /**
     * 记得手动重写这个方法
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void doYourThings()
    {
        Toast.makeText(this, "权限全部打开完毕", Toast.LENGTH_SHORT).show();

//        File myFile = this.getExternalCacheDir();
        File myFile = this.getExternalFilesDir(null);

        String myPath = myFile.getAbsolutePath();
        if (!TextUtils.isEmpty(myPath)) {
            myPath = myPath.substring(0, myPath.lastIndexOf("/"));
            File ownFile = new File(myPath + "/zhouguagya_File");
            if (!ownFile.exists()) {
                ownFile.mkdirs();
            }
            String path = ownFile.getAbsolutePath();
            File file = null;
            if (!TextUtils.isEmpty(path)) {
                file = new File(path, "wenjian.txt");
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            Log.i("-----file--->", file.toString());
            OutputStreamWriter osw = null;
            try
            {
                osw = new OutputStreamWriter(new FileOutputStream(file));
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }


            //  写文件
            try
            {
                osw.write(" android 开发就是好");
                osw.close();

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }


        }




    }
}
