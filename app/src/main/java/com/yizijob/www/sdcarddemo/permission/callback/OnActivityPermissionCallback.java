package com.yizijob.www.sdcarddemo.permission.callback;

import android.support.annotation.NonNull;

/**
 * @author :周广亚
 * @version :
 * @Date : 2017/8/28  17:58
 * @Desc :
 */

public interface OnActivityPermissionCallback
{
    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
}
