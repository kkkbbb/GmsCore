package com.google.android.finsky.splitinstallservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.play.core.splitinstall.protocol.ISplitInstallService;
import com.google.android.play.core.splitinstall.protocol.SplitInstallServiceImpl;

public class SplitInstallService extends Service {
    private ISplitInstallService mservice = null;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("SplitInstallService","return service");
        if(mservice==null){
            mservice = new SplitInstallServiceImpl(this.getApplicationContext());
        }
        return (IBinder) mservice;
    }
}
