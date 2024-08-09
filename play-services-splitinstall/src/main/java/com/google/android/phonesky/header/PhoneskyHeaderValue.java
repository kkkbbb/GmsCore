package com.google.android.phonesky.header;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.vending.GsfGServices;
import com.google.android.vending.exerimentsAndConfigs;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import android.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.zip.GZIPOutputStream;

import okio.ByteString;

public class PhoneskyHeaderValue {
    static String TAG = "PhoneskyHeaderValue";

    public static String getConsistencyToken(Context context){
        try {
            Class<?> checkclient = Class.forName("org.microg.vending.billing.CheckinServiceClient");
            Object instance = checkclient.getField("INSTANCE").get(null);
            Method method =  checkclient.getDeclaredMethod("getConsistencyToken", Context.class);
            return (String) method.invoke(instance,context);
        }catch (Exception ignored){
            Log.e(TAG,"getConsistencyToken failed!");
            return "";
        }

    }

    @SuppressLint("HardwareIds")
    public static aknj init(Context applicationContext) {
        try{
            ayie.Builder initiated = new ayie.Builder();

            initiated.n(new awzv.Builder()
                    .b(getConsistencyToken(applicationContext))
                    .f("").build());

            initiated.r(new axcm.Builder()
                    .b(Build.DEVICE)
                    .c(Build.HARDWARE)
                    .d(Build.MODEL)
                    .f(Build.PRODUCT)
                    .g(Long.valueOf(GsfGServices.getString(applicationContext.getContentResolver(),"android_id","")))
                    .e(Uri.encode(applicationContext.getPackageManager().getApplicationInfo(applicationContext.getPackageName(), PackageManager.GET_META_DATA).metaData.getString("GpVersion")).replace("(", "%28").replace(")", "%29"))
                    .h(Build.FINGERPRINT).build());

            initiated.q(new awyv.Builder()
                    .b(new awyw.Builder()
                            .b(Build.VERSION.SDK_INT)
                            .c(Build.ID)
                            .d(Build.VERSION.RELEASE)
                            .e(84122130).build())
                    .c("am-google") //"market_client_id"
                    .d(true) //getResources(xxx)^1
                    .build());

            aknj.Builder result = new aknj.Builder();
            result.a.put("<device>",initiated.build());
            return result.build();
        }catch (Exception e){
            Log.e(TAG,"PhoneskyHeaderValue.Init",e);
        }
        return null;
    }

    public static void getphoneskyHeader(Context context, Account account) throws IOException, IllegalAccessException, OperationCanceledException, AuthenticatorException {
        googleApiRequest request = new googleApiRequest("https://play-fe.googleapis.com/fdfe/toc?nocache_isui=true","GET",account, context,buildataToc(context));
        bbcz result = request.sendRequest(null);
        axau axau_ = new axau.Builder().b(ByteString.encodeUtf8(result.b.k.t)).build();
        writePhonesky(context, "<device>", data -> data.j(axau_).build());

        aynq firtsyncdata = new aynq.Builder().a(Collections.singletonList(new aynw.Builder().oneofField30(new ayfd.Builder().build()).build())).build();
        request = new googleApiRequest("https://play-fe.googleapis.com/fdfe/sync","POST",account,context,buildataToc(context));
        request.content = firtsyncdata.encode();
        bbcz resultSyncfirst = request.sendRequest(null);
        writePhonesky(context,"<device>",data -> data.d(resultSyncfirst.b.br.b).build());

        aynq requestdata = payloadsProtoStore.readCache(context);
        request = new googleApiRequest("https://play-fe.googleapis.com/fdfe/sync?nocache_qos=lt","POST",account,context,buildataToc(context));
        request.content = requestdata.encode();
        bbcz resultSync = request.sendRequest(null);
        writePhonesky(context,"<device>",data -> data.d(resultSync.b.br.b).build());

        writePhonesky(context,account.name,data -> data.i(new ayez.Builder().b(getExperimentTokenFor(context, account)).build()).build());
        writePhonesky(context,"",data -> data.i(new ayez.Builder().b(getExperimentTokenFor(context, null)).build()).build());
    }

    private static baxr getExperimentTokenFor(Context context,Account account){
        exerimentsAndConfigs.experimentsDatasRead dataRegular = exerimentsAndConfigs.readExperimentsFlag(context,"com.google.android.finsky.regular",account == null ? "" : account.name);
        exerimentsAndConfigs.experimentsDatasRead dataStable = exerimentsAndConfigs.readExperimentsFlag(context,"com.google.android.finsky.stable","");
        baxr.Builder result = new baxr.Builder();
        if(dataRegular!=null && !TextUtils.isEmpty(dataRegular.serverToken)){
            result.b(dataRegular.serverToken);
        }
        if (dataStable!=null && !TextUtils.isEmpty(dataStable.serverToken)){
            result.c(dataStable.serverToken);
        }
        return result.build();
    }

    //build X-PS-RH for first /fdfe/toc?nocache_isui=true
    public static ayie buildataToc(Context context){
        return new ayie.Builder().k(new aydy.Builder().b(5).build())
                .r(new axcm.Builder().g(Long.valueOf(GsfGServices.getString(context.getContentResolver(),"android_id",""))).build())
                .w(new awnc.Builder().b("00000000-0000-0000-0000-000000000000").c(1).build()).build();
    }



    public static void writePhonesky(Context context, String key,writePhoneskyCallback callback) throws IOException {
        File file = new File(context.getFilesDir(),"finsky/shared/phonesky_header_valuestore.pb");
        aknj.Builder existData=null;
        if(file.exists()){
            FileInputStream input =  new FileInputStream(file);
            existData = aknj.ADAPTER.decode(input).newBuilder();
            input.close();
        }else {
            if(Objects.requireNonNull(file.getParentFile()).exists() || Objects.requireNonNull(file.getParentFile()).mkdirs()){
                if(file.createNewFile()){
                    existData = Objects.requireNonNull(init(context)).newBuilder();
                }else {
                    throw new RuntimeException("create file failed");
                }
            }
        }
        if(existData !=null){
            if(existData.a.containsKey(key)){
                ayie modifed = callback.modify(Objects.requireNonNull(existData.a.get(key)).newBuilder());
                existData.a.put(key,modifed);
            }else {
                ayie modifed = callback.modify(new ayie.Builder());
                existData.a.put(key,modifed);
            }

            FileOutputStream outputStream =  new FileOutputStream(file);
            outputStream.write(existData.build().encode());
            outputStream.close();
        }

//        return null;
    }

    public interface writePhoneskyCallback{
        ayie modify(ayie.Builder data);
    }

    public static class googleApiRequest{
        private final ayie externalxpsrh;
        Context context;
        public String url;
        public String method;
        public byte[] content;
        public int timeout = 3000;
        private Account user;
        public Map<String,String> headers = new HashMap<>();
        private final String tokentype = "oauth2:https://www.googleapis.com/auth/googleplay";
        public boolean gzip = false;

        public googleApiRequest(String url,String method,Account user, Context context,ayie xpsrh){
            this.url = url;
            this.method = method;
            this.context = context;
            this.user = user;
            this.externalxpsrh = xpsrh;
            headers.put("User-Agent",buildUserAgent());
        }

        @SuppressLint("DefaultLocale")
        private String buildUserAgent() {
            String versionName = "41.2.21-31"; // 应用的版本名称，通常从 APK 元数据中获取
            String versionCode = "84122130"; // 应用的版本代码，通常从 APK 元数据中获取
            int apiLevel = Build.VERSION.SDK_INT; // Android API 级别
            String device = Build.DEVICE; // 设备名称
            String hardware = Build.HARDWARE; // 硬件名称
            String product = Build.PRODUCT; // 产品名称
            String release = Build.VERSION.RELEASE; // Android 版本号
            String model = Build.MODEL; // 设备型号
            String buildId = Build.ID; // Build ID
//            boolean isWideScreen = getResources().getBoolean(R.bool.is_wide_screen); // 是否宽屏，需要在资源中定义
            String supportedAbis = null; // 支持的 ABI 类型
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                supportedAbis = String.join(";", Build.SUPPORTED_ABIS);
            }else {
                supportedAbis = Build.CPU_ABI + ";" + Build.CPU_ABI2;
            }

            return String.format("Android-Finsky/%s [0] [PR] 636997666 (api=%d,versionCode=%s,sdk=%d,device=%s,hardware=%s,product=%s,platformVersionRelease=%s,model=%s,buildId=%s,isWideScreen=%d,supportedAbis=%s)",
                    versionName, apiLevel, versionCode, apiLevel, device, hardware, product, release, model, buildId, 0, supportedAbis);
        }

        public void addHeader(String key,String value){
            headers.put(key,value);
        }

        public Map<String,String> getHeaders() throws IOException, IllegalAccessException, OperationCanceledException, AuthenticatorException {
            File phoneksy_header_file = new File(context.getFilesDir(),"finsky/shared/phonesky_header_valuestore.pb");
            aknj existData= new aknj.Builder().build();
            if(phoneksy_header_file.exists()){
                FileInputStream input =  new FileInputStream(phoneksy_header_file);
                existData = aknj.ADAPTER.decode(input);
                input.close();
            }
            ayie xpsrh = new ayie.Builder().build();
            if(existData.a.containsKey("<device>")){
                xpsrh = existData.a.get("<device>");
            }
            if (existData.a.containsKey(user.name)){
                mergeProto(xpsrh,existData.a.get(user.name));
            }
            if(externalxpsrh!=null){
                mergeProto(xpsrh,externalxpsrh);
            }
            headers.put("X-PS-RH", Base64.encodeToString(gzip(xpsrh.encode()),Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP));
            headers.put("Authorization","Bearer " + AccountManager.get(context).getAuthToken(user,tokentype,null,false,null,null).getResult().getString(AccountManager.KEY_AUTHTOKEN));
            return this.headers;
        }

        public static byte[] gzip(byte[] arr_b) {
            try(ByteArrayOutputStream byteArrayOutputStream0 = new ByteArrayOutputStream(); GZIPOutputStream gZIPOutputStream0 = new GZIPOutputStream(byteArrayOutputStream0)) {
                gZIPOutputStream0.write(arr_b);
                gZIPOutputStream0.finish();
                byte[] arr_b1 = byteArrayOutputStream0.toByteArray();
                arr_b1[9] = 0;
                return arr_b1;
            }
            catch(IOException iOException0) {
                Log.e("Unexpected %s", Arrays.toString(new Object[]{iOException0}));
                return new byte[0];
            }
        }

        public void mergeProto(ayie data1,ayie data2) throws IllegalAccessException {
            for(Field data : ayie.class.getDeclaredFields()){
                data.setAccessible(true);
                if(data.get(data2) != null && data.get(data1) == null){
                    data.set(data1,data.get(data2));
                }
            }
        }

        public bbcz sendRequest(Map<String,String> externalHeader) throws IOException, IllegalAccessException, OperationCanceledException, AuthenticatorException {
            URL uRL0 = new URL(this.url);
            HttpURLConnection httpURLConnection0 = (HttpURLConnection)uRL0.openConnection();
            httpURLConnection0.setInstanceFollowRedirects(HttpURLConnection.getFollowRedirects());
            httpURLConnection0.setConnectTimeout(this.timeout);
            httpURLConnection0.setReadTimeout(this.timeout);
            httpURLConnection0.setUseCaches(false);
            httpURLConnection0.setDoInput(true);

            Map<String, String> headers = new HashMap<>(this.getHeaders());
            if(externalHeader!=null) headers.putAll(externalHeader);
            for(String key : headers.keySet()){
                httpURLConnection0.setRequestProperty(key,headers.get(key));
            }
            httpURLConnection0.setRequestMethod(this.method);
            if(this.method.equals("POST")){
                byte[] content = this.content;
                if(content!=null){
                    httpURLConnection0.setDoInput(true);
                    if(!httpURLConnection0.getRequestProperties().containsKey("Content-Type")) {
                        httpURLConnection0.setRequestProperty("Content-Type", "application/x-protobuf");
                    }
                    OutputStream dataOutputStream0;
                    if(this.gzip){
                        dataOutputStream0 = new GZIPOutputStream(new DataOutputStream(httpURLConnection0.getOutputStream()));
                    }else {
                        dataOutputStream0 = new DataOutputStream(httpURLConnection0.getOutputStream());
                    }

                    dataOutputStream0.write(content);
                    dataOutputStream0.close();
                }
            }
            int responseCode = httpURLConnection0.getResponseCode();
            if(responseCode==200){
//            String xdfe = httpURLConnection0.getHeaderField("x-dfe-signature-response");
                byte[] data = exerimentsAndConfigs.toByteArray(httpURLConnection0.getInputStream());
                return bbcz.ADAPTER.decode(data);
            }

            return null;
        }
    }
}
