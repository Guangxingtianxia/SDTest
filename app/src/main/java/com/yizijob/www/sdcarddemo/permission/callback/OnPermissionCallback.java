package com.yizijob.www.sdcarddemo.permission.callback;

import android.support.annotation.NonNull;

/**
 * @author :
 * @version :
 * @Date : 2017/8/28  17:59
 * @Desc :
 */

public interface OnPermissionCallback
{
    void onPermissionGranted(@NonNull String[] permissionName);

    void onPermissionDeclined(@NonNull String[] permissionName);

    void onPermissionPreGranted(@NonNull String permissionsName);

    void onPermissionNeedExplanation(@NonNull String permissionName);

    void onPermissionReallyDeclined(@NonNull String permissionName);

    void onNoPermissionNeeded();
}
