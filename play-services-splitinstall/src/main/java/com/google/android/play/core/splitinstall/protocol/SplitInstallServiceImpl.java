package com.google.android.play.core.splitinstall.protocol;

import static androidx.core.content.ContextCompat.startActivity;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.format.Time;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.android.phonesky.header.PhoneskyHeaderValue.googleApiRequest;
import com.google.android.phonesky.header.axcu;
import com.google.android.phonesky.header.ayie;
import com.google.android.phonesky.header.bbcz;
import com.google.android.phonesky.header.bbok;

import org.brotli.dec.BrotliInputStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class SplitInstallServiceImpl extends ISplitInstallService.Stub {
    static final String TAG = "SplitInstallService";
    private final Context mcontext;
    private boolean enable_split_install_starter_refactoring = true;
    public static BlockingQueue<String> installResultStat = new ArrayBlockingQueue<>(1);

    public SplitInstallServiceImpl(Context ctx){
        this.mcontext = ctx;
    }

    @Override
    public void startInstall(String pkg, List<Bundle> splits, Bundle bundle0, ISplitInstallServiceCallback callback) throws RemoteException {
        Log.i(TAG,"Start install for package: "+pkg);
        //phenotype "enable_split_install_starter_refactoring"
//        if(enable_split_install_starter_refactoring){
//
//        }

    }

    @Override
    public void completeInstalls(String pkg, int v, Bundle bundle0, ISplitInstallServiceCallback callback) throws RemoteException {
        Log.i(TAG,"Complete installs for package: "+pkg);
    }

    @Override
    public void cancelInstall(String pkg, int v, ISplitInstallServiceCallback callback) throws RemoteException {
        Log.i(TAG,"Cancel install for package: %s, session: %d");
    }

    @Override
    public void getSessionState(String pkg, int v, ISplitInstallServiceCallback callback) throws RemoteException {
        Log.i(TAG,"getSessionState for package: %s, session: %d");
    }

    @Override
    public void getSessionStates(String pkg, ISplitInstallServiceCallback callback) throws RemoteException {
        Log.i(TAG,"getSessionStates for package: %s");
    }

    @Override
    public void splitRemoval(String pkg, List<Bundle> splits, ISplitInstallServiceCallback callback) throws RemoteException {
        Log.i(TAG,"Split removal requested but app not found, package: "+pkg);
    }

    @Override
    public void splitDeferred(String pkg, List<Bundle> splits, Bundle bundle0, ISplitInstallServiceCallback callback) throws RemoteException {
        Log.i(TAG,"Split deferred install requested but app not found, package: %s"+pkg);
    }

    @Override
    public void getSessionStat2(String pkg, ISplitInstallServiceCallback callback) throws RemoteException {
        Log.i(TAG,"getSessionState2 for package: %s, session: %d"+pkg);
    }

    @Override
    public void getSessionStates2(String pkg, ISplitInstallServiceCallback callback) throws RemoteException {
        Log.i(TAG,"getSessionStates2 for package: %s, session: %d"+pkg);
    }

    @Override
    public void getSplitsAppUpdate(String pkg, ISplitInstallServiceCallback callback) throws RemoteException {
        Log.i(TAG,"Get splits for app update for package: %s"+pkg);
    }

    @Override
    public void completeInstallAppUpdate(String pkg, ISplitInstallServiceCallback callback) throws RemoteException {
        Log.i(TAG,"Complete install for app update for package: %s"+pkg);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void languageSplitInstall(String pkg, List<Bundle> splits, Bundle bundle0, ISplitInstallServiceCallback callback) throws RemoteException {
        Log.i(TAG,"Language split installation requested but app not found, package: %s"+pkg);
        NotificationManager notificationManager = (NotificationManager) mcontext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("splitInstall", "Split Install", NotificationManager.IMPORTANCE_DEFAULT));
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mcontext, "splitInstall")
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setContentTitle("Microg正在安装分包")
                .setContentText("正在安装"+splits.get(0).getString("language")+"语言包")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
//                .setAutoCancel(true);
        notificationManager.notify(1, builder.build());

        requestSplitsPackage(pkg,splits.get(0).getString("language"));
        try {
            String stat = installResultStat.poll(30, java.util.concurrent.TimeUnit.SECONDS);
            notificationManager.cancel(1);
            sendCompleteBroad(pkg,splits.get(0).getString("language"));
            Log.d("SplitInstallServiceImpl", "Install result: "+stat);
        } catch (InterruptedException e) {
            Log.e("SplitInstallServiceImpl", "Error getting install result", e);
        }

    }

    @Override
    public void languageSplitUninstall(String pkg, List<Bundle> splits, ISplitInstallServiceCallback callback) throws RemoteException {
        Log.i(TAG,"Language split uninstallation requested but app not found, package: %s"+pkg);
    }

    public void requestSplitsPackage(String packageName,String splitName) {
        if (ContextCompat.checkSelfPermission(mcontext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions((Activity) mcontext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            Log.d("SplitInstallServiceImpl", "No permission to write external storage");
        }

        PackageManager packageManager = this.mcontext.getPackageManager();
        long versionCode = 0;
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                versionCode = packageInfo.getLongVersionCode(); // For API level 28 and above
            } else {
                versionCode = packageInfo.versionCode; // For API level 27 and below
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String donwloadUrl = "";
        try {
            Account[] accounts = AccountManager.get(this.mcontext).getAccountsByType("com.google");
            googleApiRequest googleApiRequest = new googleApiRequest("https://play-fe.googleapis.com/fdfe/delivery?doc="+packageName+"&ot=1&vc="+versionCode+"&bvc="+versionCode+
                    "&pf=1&pf=2&pf=3&pf=4&pf=5&pf=7&pf=8&pf=9&pf=10&da=4&bda=4&bf=4&fdcf=1&fdcf=2&ch=&mn=config."+splitName,"GET",accounts[0],mcontext,
                    new ayie.Builder().m(new axcu.Builder().a(Collections.singletonList(splitName)).build()).build());
            bbcz response = googleApiRequest.sendRequest(null);
            for(bbok item : response.b.t.c.n){
                if(("config."+splitName).equals(item.b)){
                    donwloadUrl = item.i.d;
                    break;
                }
            }
        }catch (Exception e) {
            Log.e("SplitInstallServiceImpl", "Error getting download url", e);
        }
        try{
            URL url = new URL(donwloadUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            if(connection.getResponseCode()==200){
                File file = new File(mcontext.getFilesDir(),"phonesky-download-service/temp");
                if(!file.getParentFile().exists()){
                    file.getParentFile().mkdirs();
                }
                OutputStream tmpfile = new BufferedOutputStream(new FileOutputStream(file));

                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                byte[] buffer = new byte[4096];
                int bytesRead = -1;

                // 读取输入流并写入文件输出流
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    tmpfile.write(buffer, 0, bytesRead);
                }
                inputStream.close();
                tmpfile.close();
            }

        }catch (Exception e){
            Log.e("SplitInstallServiceImpl", "Error downloading split", e);
        }

        PackageInstaller packageInstaller;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            packageInstaller = mcontext.getPackageManager().getPackageInstaller();
            PackageInstaller.SessionParams params = new PackageInstaller.SessionParams(
                    PackageInstaller.SessionParams.MODE_INHERIT_EXISTING);
            params.setAppPackageName(packageName);
            params.setAppLabel(packageName+"的语言内容");
            params.setInstallLocation(1);
            try{
                @SuppressLint("PrivateApi") Method method = PackageInstaller.SessionParams.class.getDeclaredMethod("setDontKillApp", boolean.class);
                method.invoke(params, true);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                Log.w("SplitInstallServiceImpl", "Error setting dontKillApp", e);
            }

            int sessionId;
            try {
                sessionId = packageInstaller.createSession(params);
                PackageInstaller.Session session = packageInstaller.openSession(sessionId);
                File file = new File(mcontext.getFilesDir(),"phonesky-download-service/temp");
                try (InputStream in = new BrotliInputStream(new FileInputStream(file));
                     OutputStream out = session.openWrite(splitName, 0, -1)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                    session.fsync(out);
                }catch (Exception e){
                    Log.e("SplitInstallServiceImpl", "Error installing split", e);
                }

                Intent intent = new Intent(mcontext, InstallResultReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(mcontext,
                        sessionId, intent, 0);
                session.commit(pendingIntent.getIntentSender());
            } catch (IOException e) {
                Log.e("SplitInstallServiceImpl", "Error installing split", e);
            }
        }

    }

    public void sendCompleteBroad(String pkg,String split){
        Bundle extra = new Bundle();
        extra.putInt("status",5);
        extra.putLong("total_bytes_to_download",99999);
        extra.putString("languages",split);
        extra.putInt("error_code",0);
        extra.putInt("session_id",0);
        extra.putLong("bytes_downloaded",99999);
        Intent intent = new Intent("com.google.android.play.core.splitinstall.receiver.SplitInstallUpdateIntentService");
        intent.setPackage(pkg);
        intent.putExtra("session_state",extra);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        mcontext.sendBroadcast(intent);
    }


    public static class InstallResultReceiver extends BroadcastReceiver {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra(PackageInstaller.EXTRA_STATUS, -1);
            try {
                switch (status) {
                    case PackageInstaller.STATUS_SUCCESS:
                        Log.d("InstallResultReceiver", "安装成功");
                        installResultStat.put("Success");
                        break;
                    case PackageInstaller.STATUS_FAILURE:
                        Log.d("InstallResultReceiver", "安装失败");
                        String errormsg = intent.getStringExtra(PackageInstaller.EXTRA_STATUS_MESSAGE);
                        Log.d("InstallResultReceiver", errormsg==null?"":errormsg);
                        installResultStat.put("Failed");
                        break;
                    case PackageInstaller.STATUS_PENDING_USER_ACTION:
                        // 获取IntentSender
                        Intent intent0 =  (Intent) (intent.getExtras().get(Intent.EXTRA_INTENT));
                        intent0.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(context, intent0, null);
                        break;
                    default:
                        Log.d("InstallResultReceiver", "安装失败");
                        installResultStat.put("Failed");
                        break;
                }
            }catch (Exception e){
                Log.e("InstallResultReceiver", "Error handling install result", e);
            }

        }
    }


}
