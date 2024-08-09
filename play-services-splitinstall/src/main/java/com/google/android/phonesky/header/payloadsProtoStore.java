package com.google.android.phonesky.header;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Point;
import android.opengl.GLES10;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.google.android.vending.GsfGServices;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

public class payloadsProtoStore {
    static final String TAG = "payloadProtoStore";

    static String accountSha256(Account account, Context context){
        try {
            String androidId = GsfGServices.getString(context.getContentResolver(),"android_id","");
            byte[] androidIdAcc = (androidId+"-"+account.name).getBytes();
            MessageDigest messageDigest0 = MessageDigest.getInstance("SHA256");
            messageDigest0.update(androidIdAcc, 0, androidIdAcc.length);
            return Base64.encodeToString(messageDigest0.digest(), 11);
        }catch (Exception ignored){
            return null;
        }
    }

    public static aynq readCache(Context context){
        File cacheFile = new File(context.getFilesDir(),"finsky/shared/payload_valuestore.pb");
        if(!cacheFile.exists()){
            return null;
        }
        try (FileInputStream inputStream = new FileInputStream(cacheFile)) {
            return aynq.ADAPTER.decode(inputStream);
        } catch (IOException e) {
            Log.e(TAG, "Error reading person from file", e);
            return null;
        }
    }

    public static void cachePaylaods(Account account,Context context){
        aynq.Builder datas = new aynq.Builder();
        aynw[] payloads = buildPayloads(account, context);
        for(aynw payload : payloads){
            if(payload!=null) datas.a.add(payload);
        }
        File cacheFile = new File(context.getFilesDir(),"finsky/shared/payload_valuestore.pb");
        try {
            if(!cacheFile.exists()){
                if(!cacheFile.getParentFile().exists()) cacheFile.getParentFile().mkdirs();
                cacheFile.createNewFile();
            }
        }catch (Exception e){
            Log.e(TAG,"Create payload_valuestore.pb failed !");
            return;
        }
        try (FileOutputStream outputStream = new FileOutputStream(cacheFile)) {
            outputStream.write(datas.build().encode());
            Log.d(TAG, "Person written to file: " + cacheFile.getAbsolutePath());
        } catch (IOException e) {
            Log.e(TAG, "Error writing person to file", e);
        }

    }

    public static String generateRandomIMEI() {
        Random random = new Random();

        // 生成前 14 位随机数
        StringBuilder imeiBuilder = new StringBuilder();
        for (int i = 0; i < 14; i++) {
            int digit = random.nextInt(10);
            imeiBuilder.append(digit);
        }

        // 计算校验位
        String imei14 = imeiBuilder.toString();
        int checkDigit = calculateLuhnCheckDigit(imei14);

        // 拼接成完整的 IMEI
        imeiBuilder.append(checkDigit);
        return imeiBuilder.toString();
    }

    private static int calculateLuhnCheckDigit(String imei14) {
        int sum = 0;
        for (int i = 0; i < imei14.length(); i++) {
            int digit = Character.getNumericValue(imei14.charAt(i));
            if (i % 2 == 1) {
                digit *= 2;
            }
            if (digit > 9) {
                digit -= 9;
            }
            sum += digit;
        }
        return (10 - (sum % 10)) % 10;
    }

    public static aynw[] buildPayloads(Account account, Context context) {
        List<FetchedGlStrings> gpuInfos = fetchGLInfo();
        assert gpuInfos!=null;
        //---------------------------------------GPU info--------------------------------------------------------------------

        String result = "";
        result = accountSha256(account, context);
        aypl aypl_ = new aypl.Builder().b(result).build();
        awms awms_ = new awms.Builder().b(aypl_).build();
        aynw ACCOUNT_ASSOCIATION_PAYLOAD = new aynw.Builder().oneofField7(awms_).build();
        //--------------------------------------------------------------------------------------------------------------------
        aynw CARRIER_PROPERTIES_PAYLOAD = null;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            @SuppressLint("HardwareIds") String subscriberId = telephonyManager.getSubscriberId();
            String subscriberId1 = Long.parseLong(subscriberId) / 100000L + "00000";
            String groupidLevel = telephonyManager.getGroupIdLevel1();
            String simoperator = telephonyManager.getSimOperator();
            String operatorName = telephonyManager.getSimOperatorName();
            int simcardId = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                simcardId = telephonyManager.getSimCarrierId();
            }
            int CarrierIdFromSimMccMnc = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                CarrierIdFromSimMccMnc = telephonyManager.getCarrierIdFromSimMccMnc();
            }

            bcfo bcfo_ = new bcfo.Builder().b(Long.valueOf(subscriberId1)).c(operatorName).d(groupidLevel).g(simcardId).h(CarrierIdFromSimMccMnc).build();
            bcfl bcfl_ = new bcfl.Builder().b(bcfo_).build();
            axbm axbm_ = new axbm.Builder().b(bcfo_.b).c(bcfo_.c).d(bcfo_.d).e(bcfo_.e).g(bcfo_.g).h(bcfo_.h).f(bcfo_.f).build();

            axbl axbl_ = new axbl.Builder().b(axbm_).build();
            awyq awyq_ = new awyq.Builder().c(axbl_).b(simoperator).build();
            CARRIER_PROPERTIES_PAYLOAD = new aynw.Builder().oneofField9(awyq_).build();
        } catch (SecurityException unused_ex) {
            Log.e(TAG, "SecurityException when reading IMSI.");
        } catch (IllegalStateException unused_ex) {
            Log.e(TAG, "IllegalStateException when reading IMSI. This is a known SDK 31 Samsung bug.");
        }

        //-------------------------------------------------------------------------------------------------------------
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        Account accounts[] = accountManager.getAccounts();

//        boolean hasAdmin = false;
//        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
//        List<ComponentName> activeAdmins = devicePolicyManager.getActiveAdmins();
//        if (activeAdmins != null) {
//            for (ComponentName admin : activeAdmins) {
//                String s2 = admin.getPackageName();
//                if(devicePolicyManager.isDeviceOwnerApp(s2)){
//                    hasAdmin = true;
//                }
//            }
//        }
//        ComponentName profileowner = devicePolicyManager.getProfileOwner()
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            Bundle bundle = devicePolicyManager.getApplicationRestrictions();
//        }
        //非系统应用无法获取,忽略
        for (Account account1 : accounts) {
            if (account1 != null && account1.type.equals("com.google.work")) {
                int idx = 0;
                for (Account account2 : accounts) {
                    idx++;
                    if (account1.name.equalsIgnoreCase(account2.name)) {
                        accounts[idx] = null;
                    }
                }
            }
        }
        axch.Builder axch_ = new axch.Builder();
        for (Account account1 : accounts) {
            axch_.a.add(new aypl.Builder().b(accountSha256(account1, context)).build());
        }
        aynw DEVICE_ACCOUNTS_PAYLOAD = new aynw.Builder().oneofField8(axch_.build()).build();

        //--------------------------------------------------------------------------------------------------
        bcfm.Builder bcfm_ = new bcfm.Builder();
        bcfm_.b(0).c(0).d(0).e(0).g(false).h(false).i(0).l(0);

        ActivityManager activityManager0 = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo0 = activityManager0.getDeviceConfigurationInfo();
        if (configurationInfo0 != null) {
            switch (configurationInfo0.reqTouchScreen) {
                case 1, 2, 3:
                    bcfm_.b(configurationInfo0.reqTouchScreen);
            }
            switch (configurationInfo0.reqKeyboardType) {
                case 1, 2, 3:
                    bcfm_.c(configurationInfo0.reqKeyboardType);
            }
            switch (configurationInfo0.reqNavigation) {
                case 1, 2, 3, 4:
                    bcfm_.d(configurationInfo0.reqNavigation);
            }
            bcfm_.l(configurationInfo0.reqGlEsVersion);
            bcfm_.g((configurationInfo0.reqInputFeatures & 1) == 1).h((configurationInfo0.reqInputFeatures & 2) > 0);
        }

//        new Point();
//        Point point0 = ((DisplayManager)context.getSystemService(Context.DISPLAY_SERVICE)).getStableDisplaySize();
//        Point point1 = new Point(Math.min(point0.x, point0.y), Math.max(point0.x, point0.y));
        //需要系统应用才能使用
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        if (windowManager != null) {
            Display display = windowManager.getDefaultDisplay();
            display.getSize(size);

            bcfm_.j(size.x).k(size.y);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            bcfm_.i(DisplayMetrics.DENSITY_DEVICE_STABLE).e(caculatePoint(size, DisplayMetrics.DENSITY_DEVICE_STABLE));
        }

        Configuration configuration0 = context.getResources().getConfiguration();
        bcfm_.f(configuration0.screenLayout).s(configuration0.smallestScreenWidthDp)
                .m(Arrays.asList(Objects.requireNonNull(context.getPackageManager().getSystemSharedLibraryNames())))
                .q(Arrays.asList(context.getAssets().getLocales()));
        //"enable_always_fetch_gle"
        //"enable_fetch_gle_if_crashed"
        //两个experimentsFlags暂时不做判断,假设都为0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            bcfm_.r(gpuInfos.stream().flatMap(fetchedGlStrings -> Arrays.stream(fetchedGlStrings.glExtensions)).collect(Collectors.toList()))
                    .t(activityManager0.isLowRamDevice());
        }

        ActivityManager.MemoryInfo meminfo = new ActivityManager.MemoryInfo();
        activityManager0.getMemoryInfo(meminfo);
        bcfm_.u(meminfo.totalMem).v(Runtime.getRuntime().availableProcessors());

        FeatureInfo[] arr_featureInfo = context.getPackageManager().getSystemAvailableFeatures();
        for (FeatureInfo featureInfo0 : arr_featureInfo) {
            if (!TextUtils.isEmpty(featureInfo0.name)) {
                bcfn bcfn_ = new bcfn.Builder().build();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    bcfn_ = new bcfn.Builder().b(featureInfo0.name).c(featureInfo0.version).build();
                }
                bcfm_.o.add(bcfn_);
                bcfm_.n.add(bcfn_.b);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bcfm_.p(List.of(Build.SUPPORTED_ABIS));
        }

        String prop = getSystemProperty("ro.oem.key1","");
        if(TextUtils.isEmpty(prop)){
            bcfm_.w(prop);
        }
        bcfm_.x(Build.VERSION.CODENAME);
        prop = getSystemProperty("ro.build.version.preview_sdk_fingerprint","");
        if(TextUtils.isEmpty(prop)){
            bcfm_.y(prop);
        }
        bcfm bcfmResult = bcfm_.build();
        //转换
        axcj.Builder axcj_ = new axcj.Builder();
        axcj_.e(bcfmResult.r);
        for (bcfn bcfn_ : bcfmResult.o){
            axcj_.b.add(new ayny.Builder().b(bcfn_.b).c(bcfn_.c).build());
        }
        axcj_.c(bcfmResult.m).d(bcfmResult.q).f(false);
        aynw DEVICE_CAPABILITIES_PAYLOAD = new aynw.Builder().oneofField10(axcj_.build()).build();

        //-------------------------------------------------------------------------------------------------------------------
        axcn.Builder axcn_ = new axcn.Builder();
        axcn_.c(bcfmResult.g).b(bcfmResult.c).d(bcfmResult.d);
        aynw DEVICE_INPUT_PROPERTIES_PAYLOAD = new aynw.Builder().oneofField11(axcn_.build()).build();
        //-------------------------------------------------------------------------------------------------------------------
        axcs.Builder axcs_ = new axcs.Builder();
        axcs_.b(Build.MANUFACTURER).c(Build.MODEL).d(Build.DEVICE).e(Build.PRODUCT).f(Build.BRAND);
        aynw DEVICE_MODEL_PAYLOAD = new aynw.Builder().oneofField12(axcs_.build()).build();
        //-------------------------------------------------------------------------------------------------------------------
        axdz.Builder axdz_ = new axdz.Builder();
        DevicePolicyManager devicePolicyManager0 = (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        List<ComponentName> list1 = devicePolicyManager0.getActiveAdmins();
        if(list1!=null){
            for (ComponentName componentName : list1){
                String packageName = componentName.getPackageName();
                PackageInfo packageInfo0 = null;
                try {
                    packageInfo0 = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
                }catch (Exception ignored){}

                boolean isDeviceOwner = devicePolicyManager0.isDeviceOwnerApp(packageName);
                boolean isProfileOwner = false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    isProfileOwner = devicePolicyManager0.isProfileOwnerApp(packageName);
                }

                baun baun_ = new baun.Builder().b(componentName.getPackageName())
                        .e(isDeviceOwner?policyType.MANAGED_DEVICE:isProfileOwner?policyType.MANAGED_PROFILE:policyType.LEGACY_DEVICE_ADMIN).c(caculateSHA(packageInfo0.signatures[0].toByteArray(),"SHA1")).d(caculateSHA(packageInfo0.signatures[0].toByteArray(),"SHA256")).build();
                axzv axzv_ = new axzv.Builder().b(baun_.b).c(baun_.c).d(baun_.d).e(mangedScope.fromValue(baun_.e.getValue())).build();
                if(isProfileOwner && isProfileOwner){
                    axdz_.b(axzv_);
                }
                axdz_.c.add(axzv_);
            }
        }
        aynw ENTERPRISE_PROPERTIES_PAYLOAD = new aynw.Builder().oneofField13(axdz_.build()).build();
        //---------------------------------------------------------------------------------------------------------------------
        awkg.Builder awkg_ = new awkg.Builder();
        TelephonyManager telephonyManager0 = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        long imeid = 0;
        if(telephonyManager0 != null) {
            String s7 = "";
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    if(PermissionRequestActivity.requestPermissionsSync(context, new String[]{Manifest.permission.READ_PHONE_STATE})){
//                        s7 = telephonyManager0.getImei();
//                    }
//                }else{
//                    s7 = telephonyManager0.getImei();
//                }
//            }
            //非系统应用，无法获取imei，采用随机生成imei
            //random imei
            s7 = generateRandomIMEI();
            imeid = TextUtils.isEmpty(s7) || !Pattern.compile("^[0-9]{15}$").matcher(s7).matches() ? 0L : Long.parseLong(s7, 10) | 0x1000000000000000L;
            if(imeid==0){
                String s8="";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    s8 = telephonyManager0.getMeid();
                }
                if(!TextUtils.isEmpty(s8) && Pattern.compile("^[0-9a-fA-F]{14}$").matcher(s8).matches()) {
                    imeid = Long.parseLong(s8, 16) | 0x1100000000000000L;
                    if(imeid==0){
                        if(context.getPackageManager().checkPermission("android.permission.READ_PRIVILEGED_PHONE_STATE", "com.android.vending") == PackageManager.PERMISSION_GRANTED){
                            String s = "";
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                s = Build.getSerial();
                            }
                            if(TextUtils.isEmpty(s) && !s.equals("unknown")) {
                                try {
                                    byte[] arr_b = MessageDigest.getInstance("SHA1").digest(s.getBytes());
                                    imeid = (((long)arr_b[0]) & 0xFFL) << 0x30 | 0x1400000000000000L | (((long)arr_b[1]) & 0xFFL) << 40 | (((long)arr_b[2]) & 0xFFL) << 0x20 | (((long)arr_b[3]) & 0xFFL) << 24 | (((long)arr_b[4]) & 0xFFL) << 16 | (((long)arr_b[5]) & 0xFFL) << 8 | ((long)arr_b[6]) & 0xFFL;
                                }catch(NoSuchAlgorithmException noSuchAlgorithmException0) {
                                    Log.e(TAG,"No support for sha1?");
                                }
                            }
                        }
                    }
                }
            }
            awkg_.b(imeid);
        }
        aynw HARDWARE_IDENTIFIER_PAYLOAD = new aynw.Builder().oneofField14(awkg_.build()).build();
        //--------------------------------------------------------------------------------------------------------------------------------
        axlx.Builder axlx_ = new axlx.Builder();
        axlx_.b(bcfmResult.t).c(bcfmResult.u).d(bcfmResult.v).e(bcfmResult.p).build();
        aynw HARDWARE_PROPERTIES_PAYLOAD = new aynw.Builder().oneofField15(axlx_.build()).build();
        //--------------------------------------------------------------------------------------------------------------------------------
        axyk.Builder axyk_ = new axyk.Builder().b("GMT+08:00");
        aynw LOCALE_PROPERTIES_PAYLOAD = new aynw.Builder().oneofField16(axyk_.build()).build();
        //--------------------------------------------------------------------------------------------------------------------------------
        //        NOTIFICATION_ROUTING_INFO_PAYLOAD忽略
        //--------------------------------------------------------------------------------------------------------------------------------
        ayfl.Builder ayfl_ = new ayfl.Builder();
        ayfl_.b("am-google").c("play-ms-android-google").d("play-ad-ms-android-google");
        aynw PLAY_PARTNER_PROPERTIES_PAYLOAD = new aynw.Builder().oneofField18(ayfl_.build()).build();
        //--------------------------------------------------------------------------------------------------------------------------------
        int version = 0;
        try{
            version = context.getPackageManager().getPackageInfo("com.android.vending", 0).versionCode;
        }catch (PackageManager.NameNotFoundException packageManager$NameNotFoundException0){
            Log.e(TAG,"[DAS] Could not find our package",packageManager$NameNotFoundException0);
        }
        ayft ayft_ = new ayft.Builder().b(version).build();
        aynw PLAY_PROPERTIES_PAYLOAD = new aynw.Builder().oneofField19(ayft_).build();
        //--------------------------------------------------------------------------------------------------------------------------------
        ayje.Builder ayje_ = new ayje.Builder();
        ayje_.b(bcfmResult.b).c(bcfmResult.j).d(bcfmResult.k).e(bcfmResult.e).f(bcfmResult.i);
        aynw SCREEN_PROPERTIES_PAYLOAD = new aynw.Builder().oneofField20(ayje_.build()).build();
        //--------------------------------------------------------------------------------------------------------------------------------
        aynz.Builder aynz_ = new aynz.Builder();
        aynz_.b("google/sunfish/sunfish:13/TQ2A.230405.003/9719927:user/release-keys").c((long) Build.VERSION.SDK_INT).d(bcfmResult.y).e(bcfmResult.x).f(bcfmResult.w).g(bcfmResult.l);
        aynw SYSTEM_PROPERTIES_PAYLOAD = new aynw.Builder().oneofField21(aynz_.build()).build();
        //--------------------------------------------------------------------------------------------------------------------------------
        List<axlq> axlqs = Collections.emptyList();
        assert gpuInfos != null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            gpuInfos = gpuInfos.stream().filter(fetchedGlStrings -> !fetchedGlStrings.glRenderer.isEmpty() || !fetchedGlStrings.glVendor.isEmpty() || !fetchedGlStrings.glVersion.isEmpty()).collect(Collectors.toList());
            Optional<Integer> maxversion = gpuInfos.stream().max(Comparator.comparingInt(fetchedGlStrings -> fetchedGlStrings.eglContextClientVersion)).map(FetchedGlStrings::getContextClientVersion);
            if(maxversion.isPresent()){
                gpuInfos = gpuInfos.stream().filter(fetchedGlStrings -> fetchedGlStrings.eglContextClientVersion == maxversion.get()).collect(Collectors.toList());
            }
            axlqs = gpuInfos.stream().map(fetchedGlStrings -> {
                axlp.Builder axlp_ = new axlp.Builder();
                if(!TextUtils.isEmpty(fetchedGlStrings.glRenderer)) axlp_.b(fetchedGlStrings.glRenderer);
                if(!TextUtils.isEmpty(fetchedGlStrings.glVendor)) axlp_.c(fetchedGlStrings.glVendor);
                if(!TextUtils.isEmpty(fetchedGlStrings.glVersion)) axlp_.d(fetchedGlStrings.glVersion);
                return new axlq.Builder().b(axlp_.build()).build();
            }).distinct().collect(Collectors.toList());
        }

        aynw GPU_PAYLOAD = new aynw.Builder().oneofField24(axlqs.isEmpty()?new axlq.Builder().build():axlqs.get(0)).build();

        return  new aynw[]{ACCOUNT_ASSOCIATION_PAYLOAD,
                CARRIER_PROPERTIES_PAYLOAD,
                DEVICE_ACCOUNTS_PAYLOAD,
                DEVICE_CAPABILITIES_PAYLOAD,
                DEVICE_INPUT_PROPERTIES_PAYLOAD,
                DEVICE_MODEL_PAYLOAD,
                ENTERPRISE_PROPERTIES_PAYLOAD,
                HARDWARE_IDENTIFIER_PAYLOAD,
                HARDWARE_PROPERTIES_PAYLOAD,
                LOCALE_PROPERTIES_PAYLOAD,
//                NOTIFICATION_ROUTING_INFO_PAYLOAD,
                PLAY_PARTNER_PROPERTIES_PAYLOAD,
                PLAY_PROPERTIES_PAYLOAD,
                SCREEN_PROPERTIES_PAYLOAD,
                SYSTEM_PROPERTIES_PAYLOAD,
                GPU_PAYLOAD};
    }

    public static ArrayList<FetchedGlStrings> fetchGLInfo(){
        EGL10 eGL100 = (EGL10) EGLContext.getEGL();
        ArrayList<FetchedGlStrings> result = new ArrayList<>();
        EGL10Wrapper egl10Instance =eGL100 == null ? null : new EGL10Wrapper(eGL100);
        if(eGL100 == null){
            Log.e(TAG, "Couldn't get EGL");
            return null;
        }
        EGLDisplay eGLDisplay0 = eGL100.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        eGL100.eglInitialize(eGLDisplay0, new int[2]);
        int[] arr_v = new int[1];
        int configCount = eGL100.eglGetConfigs(eGLDisplay0, null, 0, arr_v) ? arr_v[0] : 0;
        if(configCount <= 0) {
            Log.e(TAG, "Couldn't get EGL config count");
            return null;
        }
        EGLConfig[] arr_eGLConfig = new EGLConfig[configCount];
        EGLConfig[] arr_eGLConfig1 = eGL100.eglGetConfigs(eGLDisplay0, arr_eGLConfig, configCount, new int[1]) ? arr_eGLConfig : null;
        if(arr_eGLConfig1 == null) {
            Log.e(TAG, "Couldn't get EGL configs");
            return null;
        }
        int[] arr_v1 = {0x3057, 1, 0x3056, 1, 0x3038};
        int v3;
        for(int v1 = 0; v1 < configCount; v1 = v3 + 1) {
            if(egl10Instance.eglGetConfigAttrib(eGLDisplay0, arr_eGLConfig1[v1], 0x3027) != 0x3050 && (egl10Instance.eglGetConfigAttrib(eGLDisplay0, arr_eGLConfig1[v1], 0x3033) & 1) != 0) {
                int v2 = egl10Instance.eglGetConfigAttrib(eGLDisplay0, arr_eGLConfig1[v1], 0x3040);
                if((v2 & 1) == 0) {
                    v3 = v1;
                }else {
                    v3 = v1;
                    result.add(buildGLStrings(egl10Instance, eGLDisplay0, arr_eGLConfig1[v1], arr_v1, null, 1, false));
                }

                if((v2 & 4) != 0) {
                    result.add(buildGLStrings(egl10Instance, eGLDisplay0, arr_eGLConfig1[v3], arr_v1, new int[]{0x3098, 2, 0x3038}, 2, false));
                }

            }else {
                v3 = v1;
            }
        }
        egl10Instance.eglinstance.eglTerminate(eGLDisplay0);
        return result;
    }

    public static FetchedGlStrings buildGLStrings(EGL10Wrapper egl10Tools, EGLDisplay eGLDisplay0, EGLConfig eGLConfig0, int[] arr_v, int[] arr_v1, int v, boolean z){
        EGLContext eGLContext0 = egl10Tools.eglinstance.eglCreateContext(eGLDisplay0, eGLConfig0, EGL10.EGL_NO_CONTEXT, arr_v1);
        if(eGLContext0 != EGL10.EGL_NO_CONTEXT) {
            EGLSurface eGLSurface0 = egl10Tools.eglinstance.eglCreatePbufferSurface(eGLDisplay0, eGLConfig0, arr_v);
            if(eGLSurface0 == EGL10.EGL_NO_SURFACE) {
                egl10Tools.eglDestroyContext(eGLDisplay0, eGLContext0);
                return null;
            }
            egl10Tools.eglMakeCurrent(eGLDisplay0, eGLSurface0, eGLSurface0, eGLContext0);
            FetchedGlStrings result = new FetchedGlStrings(0,null,null,null,null);
            String s = GLES10.glGetString(0x1F03);
            if(!TextUtils.isEmpty(s)) {
                result.glExtensions = s.split(" ");
            }
            if(!z) {
                result.glRenderer = GLES10.glGetString(0x1F01);
                result.glVendor = GLES10.glGetString(0x1F00);
                result.glVersion = GLES10.glGetString(0x1F02);
            }
            if(result.glExtensions != null) {
                egl10Tools.eglMakeCurrent(eGLDisplay0, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
                egl10Tools.eglinstance.eglDestroySurface(eGLDisplay0, eGLSurface0);
                egl10Tools.eglDestroyContext(eGLDisplay0, eGLContext0);
                return result;
            }

            StringBuilder stringBuilder0 = new StringBuilder();

            if(result.glExtensions == null) {
                stringBuilder0.append(" glExtensions");
            }
            throw new IllegalStateException("Missing required properties:" + stringBuilder0);

        }
        return null;
    }

    public static class EGL10Wrapper {
        public final EGL10 eglinstance;

        EGL10Wrapper(EGL10 eglinstance) {
            this.eglinstance = eglinstance;
        }

        public final int eglGetConfigAttrib(EGLDisplay eGLDisplay0, EGLConfig eGLConfig0, int v) {
            int[] arr_v = new int[1];
            eglinstance.eglGetConfigAttrib(eGLDisplay0, eGLConfig0, v, arr_v);
            return arr_v[0];
        }

        public final void eglDestroyContext(EGLDisplay eGLDisplay0, EGLContext eGLContext0) {
            eglinstance.eglDestroyContext(eGLDisplay0, eGLContext0);
        }

        public final void eglMakeCurrent(EGLDisplay eGLDisplay0, EGLSurface eGLSurface0, EGLSurface eGLSurface1, EGLContext eGLContext0) {
            eglinstance.eglMakeCurrent(eGLDisplay0, eGLSurface0, eGLSurface1, eGLContext0);
        }
    }



    public static class FetchedGlStrings {
        public int eglContextClientVersion;
        public String[] glExtensions;
        public String glRenderer;
        public String glVendor;
        public String glVersion;

        public FetchedGlStrings(int eglContextClientVersion, String[] glExtensions, String glRenderer, String glVendor, String glVersion) {
            this.eglContextClientVersion = eglContextClientVersion;
            this.glExtensions = glExtensions;
            this.glRenderer = glRenderer;
            this.glVendor = glVendor;
            this.glVersion = glVersion;
        }

        public int getContextClientVersion(){
            return eglContextClientVersion;
        }
    }

    public static String caculateSHA(byte[] data, String algorith){
        MessageDigest messageDigest0;
        try {
            messageDigest0 = MessageDigest.getInstance(algorith);
        }
        catch(NoSuchAlgorithmException noSuchAlgorithmException0) {
            Log.e(TAG, "[DC] No support for %s?",noSuchAlgorithmException0);
            return null;
        }

        messageDigest0.update(data, 0, data.length);
        return Base64.encodeToString(messageDigest0.digest(), 11);
    }

    public static String getSystemProperty(String key, String defaultValue) {
        String value = defaultValue;
        try {
            @SuppressLint("PrivateApi") Class<?> systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method getMethod = systemPropertiesClass.getMethod("get", String.class, String.class);
            value = (String) getMethod.invoke(null, key, defaultValue);
        } catch (Exception e) {
            Log.e(TAG, "Unable to retrieve system property", e);
        }
        return value;
    }

    public static int caculatePoint(Point point0, int v) {
        float f = (float)point0.x;
        int v1 = (int)(((float)point0.y) * (160.0f / ((float)v)));
        if(v1 < 470) {
            return 17;
        }

        int v2 = (int)(f * (160.0f / ((float)v)));
        if(v1 >= 960 && v2 >= 720) {
            return v1 * 3 / 5 < v2 - 1 ? 20 : 4;
        }

        int v3 = v1 < 640 || v2 < 480 ? 2 : 3;
        return v1 * 3 / 5 < v2 - 1 ? v3 | 16 : v3;
    }

}
