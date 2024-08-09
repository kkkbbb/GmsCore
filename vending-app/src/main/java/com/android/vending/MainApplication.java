package com.android.vending;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Application;
import android.os.Build;
import android.util.Log;

import com.google.android.phonesky.header.PhoneskyHeaderValue;
import com.google.android.phonesky.header.aynq;
import com.google.android.vending.exerimentsAndConfigs;
import com.google.android.phonesky.header.payloadsProtoStore;

import java.io.IOException;


public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        PhoneskyHeaderValue.init(getApplicationContext());

        //test
        AccountManager accountManager = AccountManager.get(this);
        Account[] accounts = accountManager.getAccountsByType("com.google");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            new Thread(()->{
                aynq payloads = payloadsProtoStore.readCache(getApplicationContext());
                if(payloads==null || payloads.a.isEmpty()) payloadsProtoStore.cachePaylaods(accounts[0],getApplicationContext());
                for (Account account : accounts){
                    String accountName = account.name;
                    exerimentsAndConfigs.postRequest(exerimentsAndConfigs.buildRequestData(this),this,accountName);
                    exerimentsAndConfigs.postRequest(exerimentsAndConfigs.buildRequestData(this,"NEW_APPLICATION_SYNC","com.google.android.finsky.regular",account),this,accountName);
                    exerimentsAndConfigs.postRequest(exerimentsAndConfigs.buildRequestData(this,"NEW_USER_SYNC_ALL_ACCOUNT",null,null),this,"");
                    exerimentsAndConfigs.buildExperimentsFlag(this,accountName,"com.google.android.finsky.regular");
                    exerimentsAndConfigs.buildExperimentsFlag(this,"","com.google.android.finsky.regular");
                    exerimentsAndConfigs.buildExperimentsFlag(this,accountName,"com.google.android.finsky.stable");
                }

                try {
                    PhoneskyHeaderValue.getphoneskyHeader(this,accounts[0]);
                } catch (IOException | IllegalAccessException | AuthenticatorException |
                         OperationCanceledException e) {
                    throw new RuntimeException(e);
                }

            }).start();
        }
    }
}
