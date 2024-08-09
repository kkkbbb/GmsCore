package com.google.android.vending;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.zip.GZIPOutputStream;

import okio.ByteString;

public class exerimentsAndConfigs {
    static final String TAG = "experimentAndConfig";
    static private long version = 84122130L;
    static private long baselineCL = 636944598L;
    static private String token="";

    private static auts.Builder buildbaseauts(String pkgname,long fixed64){
        autw autw_ = new autw.Builder().b(fixed64).build();
        auue auue_ = new auue.Builder()
                .b(pkgname)
                .c(version) //暂未知来源version
                .d(autw_)
                .e(baselineCL) //cli
                .f("com.android.vending").build();

        unknowMsg msg = new unknowMsg.Builder().field1(1).build();
        return new auts.Builder()
                .b(auue_)
                .c(msg.encodeByteString());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static auty buildRequestData(Context context) {
        return buildRequestData(context,"NEW_USER_SYNC",null,null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static auty buildRequestData(Context context,String actions,String pkgname,Account account){
        @SuppressLint("HardwareIds") bbqy bbqy_ = new bbqy.Builder()
                .b(Long.valueOf(GsfGServices.getString(context.getContentResolver(),"android_id","")))
//                .b(4458620517879527908L)
                .c(Build.VERSION.SDK_INT)
                .g(Build.ID)
                .f(Build.DEVICE)
                .k(Build.MANUFACTURER)
                .d(Build.MODEL)
                .e(Build.PRODUCT)
                .f("redroid_arm64")
                .h("")
                .m(Build.FINGERPRINT)
                .j(Locale.getDefault().getCountry())
                .i(Locale.getDefault().toString())
                .o(Arrays.asList(Build.SUPPORTED_ABIS)).build();
        bbqz bbqz_ = new bbqz.Builder()
                .b(4)
                .c(bbqy_)
                .build();
        autu autu_ = new autu.Builder()
                .b((long) (account==null?0:1))  //账号是否为空
                .c(bbqz_)
                .f(false)
                .e(ByteString.of())
                .g(new auuf.Builder().build())
                .build();
        auts.Builder auts_ = null,auts_2 = null;
        if(actions.equals("NEW_USER_SYNC_ALL_ACCOUNT")){
            auts_ = buildbaseauts("com.google.android.finsky.regular",-1);
            auts_2 = buildbaseauts("com.google.android.finsky.stable",-1);
        }else {
            auts_ = buildbaseauts("com.google.android.finsky.regular",0);
        }

        if(actions.contains("NEW_USER_SYNC")){
            auty.Builder result = new auty.Builder()
                    .b(autu_)
                    .d(ByteString.of())
                    .e(action.NEW_USER_SYNC)
                    .g(128)
                    .f("com.google.android.finsky.regular");
            result.c.add(auts_.build());
            if(auts_2!=null) result.c.add(auts_2.build());
            return result.build();
        }

        if(actions.equals("NEW_APPLICATION_SYNC")){
            try{
                phenotypeDatabase datahelper = new phenotypeDatabase(context);
                SQLiteDatabase db = datahelper.getWritableDatabase();
                Cursor cursor0 = db.rawQuery("SELECT partitionId, tag FROM ApplicationTags WHERE packageName = ? AND user = ? AND version = ?",
                        new String[]{pkgname,account.name, String.valueOf(exerimentsAndConfigs.version)});
                List<auud> auuds = new ArrayList<>();
                while (cursor0.moveToNext()){
                    auuds.add(new auud.Builder()
                            .b(cursor0.getLong(0))
                            .c(ByteString.of(cursor0.getBlob(1)))
                            .build());
                }
                cursor0.close();
                auts_.e(auuds);
                cursor0 = db.rawQuery("SELECT tokensTag FROM ExperimentTokens WHERE packageName = ? AND user = ? AND version = ? AND isCommitted = 0",
                        new String[]{pkgname,account.name, String.valueOf(exerimentsAndConfigs.version)});
                if(cursor0.moveToNext()){
                    auts_.f(ByteString.of(cursor0.getBlob(0)));
                }
                cursor0.close();
                auts auts2 = buildbaseauts("com.google.android.finsky.stable",0).build();

                cursor0 = db.rawQuery("SELECT bytesTag FROM RequestTags WHERE user = ?",
                        new String[]{account.name});
                byte[] autybytes = null;
                if(cursor0.moveToNext()){
                    autybytes = cursor0.getBlob(0);
                }
                cursor0.close();

                assert autybytes != null;
                return new auty.Builder()
                        .b(autu_)
                        .c(List.of(auts_.build(),auts2))
                        .d(ByteString.of(autybytes))
                        .e(action.NEW_APPLICATION_SYNC)
                        .g(128)
                        .f("com.google.android.finsky.stable")
                        .build();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        throw new RuntimeException("request experimentsandconfigs has Unknow action");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void postRequest(auty auty_, Context context, String accoutname){
        String tokenType = "oauth2:https://www.googleapis.com/auth/experimentsandconfigs";
//        String tokenType2 = "oauth2:https://www.googleapis.com/auth/googleplay";
        try{
            URL uRL0 = new URL("https://www.googleapis.com/experimentsandconfigs/v1/getExperimentsAndConfigs" + "?r=" + auty_.e.getValue() + "&c=" + auty_.g);
//            if(token.isEmpty()){
//
//            }

            if(!TextUtils.isEmpty(accoutname)){
                AccountManager accountManager = AccountManager.get(context);
//            accountManager.getAuthToken(account,tokenType2,null,false,null,null).getResult().getString(AccountManager.KEY_AUTHTOKEN);
                AccountManagerFuture<Bundle> future = accountManager.getAuthToken(new Account(accoutname,"com.google"), tokenType, null, false, null, null);
                Bundle bundle = future.getResult();
                token = bundle.getString(AccountManager.KEY_AUTHTOKEN);
            }


            HttpURLConnection httpURLConnection0 = (HttpURLConnection)uRL0.openConnection();
            httpURLConnection0.setConnectTimeout(30000);
            httpURLConnection0.setReadTimeout(30000);
            httpURLConnection0.setDoOutput(true);
            httpURLConnection0.setInstanceFollowRedirects(false);
            httpURLConnection0.setRequestProperty("Accept-Encoding",null);
            httpURLConnection0.setRequestProperty("Content-Type", "application/x-protobuf");
            httpURLConnection0.setRequestProperty("Content-Encoding", "gzip");
            httpURLConnection0.setRequestProperty("Authorization", "Bearer " + token);
            httpURLConnection0.setRequestProperty("User-Agent","Android-Finsky/41.2.21-31 [0] [PR] 636997666 (api=3,versionCode=84122130,sdk=31,device=redroid_arm64,hardware=redroid,product=redroid_arm64,platformVersionRelease=12,model=redroid12_arm64,buildId=SQ1D.220205.004,isWideScreen=0,supportedAbis=arm64-v8a;armeabi-v7a;armeabi) (redroid_arm64 SQ1D.220205.004); gzip");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {
                gzipOutputStream.write(auty_.encode());
                gzipOutputStream.finish();
            }
            byte[] compressedData = byteArrayOutputStream.toByteArray();
            try(OutputStream os = httpURLConnection0.getOutputStream()){
                os.write(compressedData);
                os.flush();
            }
            int v = httpURLConnection0.getResponseCode();
            if(v >= 200 && v < 300) {
                byte[] array_b = toByteArray(httpURLConnection0.getInputStream());
                autz autz_ = autz.ADAPTER.decode(array_b);

                phenotypeDatabase datahelper = new phenotypeDatabase(context);
                SQLiteDatabase db = datahelper.getWritableDatabase();
//                db.execSQL("INSERT OR REPLACE INTO last_fetch (type, serving_version) VALUES (?, ?);",new Object[]{1,autz_.d});
//                db.execSQL("INSERT OR REPLACE INTO OmittedConfigPackages (config_package_id)\nSELECT config_package_id FROM config_packages WHERE name = ?1;",);
                if(autz_.b != null){
                    Cursor cursor = db.rawQuery("SELECT user FROM RequestTags WHERE user = ?1",new String[]{accoutname});
                    if(cursor.getCount() > 0){
                        db.execSQL("UPDATE RequestTags SET user = ?1, bytesTag = ?2 WHERE user = ?1",new Object[]{accoutname,autz_.b.toByteArray()});
                    }else{
                        db.execSQL("INSERT INTO RequestTags (user, bytesTag) VALUES (?, ?)",new Object[]{accoutname,autz_.b.toByteArray()});
                    }
                    cursor.close();
                }


                for(autt autt0 : autz_.a){
                    auue auue0 = autt0.b;
                    String pkgname = auue0.b;
                    long version = auue0.c;
                    for(auuc auuc0: autt0.c){
                        Long partitionId = auuc0.a.b;

                        for (auub auub0 : auuc0.b){
                            Long long4=null,long5=null;
                            Double double0=null;
                            String s5=null;
                            ByteString arr_b2= ByteString.encodeUtf8("");
                            if(auub0.h == null) continue;
                            switch (auub0.h){
                                case 1:
                                    long4 = auub0.c;
                                    break;
                                case 2:
                                    long5 = auub0.d ? 1L : 0L;
                                    break;
                                case 3:
                                    double0 = auub0.e;
                                    break;
                                case 4:
                                    s5 = auub0.f;
                                    break;
                                case 5:
                                    arr_b2 = auub0.g == null?null : auub0.g.a;
                                default:
                                    continue;
                            }
                            int flagType = 0;
                            db.execSQL("INSERT OR REPLACE INTO Flags(packageName, version, flagType, partitionId, user, name, committed, intVal, boolVal, floatVal, stringVal, extensionVal) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                                    new Object[]{pkgname,version,flagType,partitionId,accoutname,auub0.b,flagType,long4,long5,double0,s5,arr_b2.toByteArray()});
                        }
                        db.execSQL("DELETE FROM ExperimentTokens WHERE packageName = ? AND version = ? AND user = ? AND isCommitted = 0",new Object[]{pkgname,version,accoutname});
                        db.execSQL("INSERT INTO ExperimentTokens (packageName, version, user, isCommitted, experimentToken, serverToken, configHash, servingVersion, tokensTag, flagsHash) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                                new Object[]{pkgname,version,accoutname,0,autt0.d.toByteArray(),autt0.e,Integer.toString(caculateHash(autt0)),autz_.d,autt0.g.toByteArray(),0});
                        db.execSQL("DELETE FROM ApplicationTags WHERE packageName = ? AND version = ? AND user = ? AND partitionId = ?",new Object[]{pkgname,version,accoutname,partitionId});
                        db.execSQL("INSERT OR REPLACE INTO ApplicationTags (packageName, version, partitionId, user, tag) VALUES (?, ?, ?, ?, ?)",
                                new Object[]{pkgname, version, partitionId, accoutname, auuc0.a.c.toByteArray()});

                    }
                }
            }

        } catch (OperationCanceledException | AuthenticatorException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void buildExperimentsFlag(Context context,String accountName,String pkgName){
        SQLiteDatabase database = new phenotypeDatabase(context).getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT EXISTS(SELECT NULL FROM FlagOverrides)",new String[]{});
        cursor.moveToNext();
        experimentsValues expValue;
        ArrayList<FlagsValue> overflags = new ArrayList<>();
        if(cursor.getInt(0)>0){
           cursor.close();
           cursor = database.rawQuery("SELECT flagType, name, intVal, boolVal, floatVal, stringVal, extensionVal FROM FlagOverrides WHERE packageName = ? AND user = \'*\' AND committed = 0",new String[]{pkgName});
           while (cursor.moveToNext()){
               overflags.add(getFlagsValue(cursor));
           }
           for(FlagsValue flag : overflags){
               if (flag.name.equals("__phenotype_server_token") && flag.valueType == 3){
                   expValue = new experimentsValues(null,flag.stringVal,0);
               }
           }
        }
        cursor.close();
        cursor = database.rawQuery("SELECT experimentToken,serverToken,servingVersion FROM ExperimentTokens WHERE packageName = ? AND version = ? AND user = ? AND isCommitted = 0",
                new String[]{pkgName, String.valueOf(version),accountName});
        cursor.moveToNext();
        expValue = new experimentsValues(cursor.getBlob(0),cursor.getString(1),cursor.getLong(2));
        cursor.close();

        TreeSet<FlagsValue> flags = new TreeSet<>();
        cursor = database.rawQuery("SELECT flagType, name, intVal, boolVal, floatVal, stringVal, extensionVal FROM Flags WHERE packageName = ? AND version = ? AND user = ? AND committed = 0 ORDER BY name",
                new String[]{pkgName,String.valueOf(version),accountName});
        while (cursor.moveToNext()){
            flags.add(getFlagsValue(cursor));
        }
        cursor.close();


        for (FlagsValue overflag : overflags){
            flags.remove(overflag);
            flags.add(overflag);
        }
        HashMap<Integer,ArrayList<FlagsValue>> Flags2Type = new HashMap<>();
        for (FlagsValue flag : flags){
            if(Flags2Type.get(flag.flagType) == null) Flags2Type.put(flag.flagType,new ArrayList<>());
            Objects.requireNonNull(Flags2Type.get(flag.flagType)).add(flag);
        }
        ArrayList<FlagTypeValue> flagtypeList = new ArrayList<>();
        for (Integer flagtype : Flags2Type.keySet()){
            flagtypeList.add(new FlagTypeValue(flagtype, Objects.requireNonNull(Flags2Type.get(flagtype)).toArray(new FlagsValue[0])));
        }

        commit(database,pkgName,accountName);
        //experients name？
        cursor = database.rawQuery("SELECT configHash FROM ExperimentTokens WHERE packageName = ? AND version = ? AND user = ? AND isCommitted = ?",
                new String[]{pkgName,String.valueOf(version),accountName,"1"});
        cursor.moveToNext();
        String confighash = cursor.getString(0);
        cursor.close();
        String expeIntroduce = pkgName + " " + accountName + " " + confighash;

        ExperimentsFlagsConfiguration configuration = new ExperimentsFlagsConfiguration(expeIntroduce,expValue.serverToken,flagtypeList.toArray(new FlagTypeValue[0]),false,expValue.experimentToken,expValue.servingVersion);
        aqis aqis = buildProtoaqis(configuration);


        writeExperimentsFlag(aqis,context,pkgName,accountName);
    }

    private static void commit(SQLiteDatabase database,String pkgName,String accountName){
        //将写入expflag的数据标记commit
        database.execSQL("INSERT OR REPLACE INTO ExperimentTokens SELECT packageName, version, user, 1 AS isCommitted, experimentToken, serverToken, configHash, servingVersion, tokensTag, flagsHash FROM ExperimentTokens WHERE packageName = ? AND version = ? AND user = ? AND isCommitted = 0",
                new Object[]{pkgName,String.valueOf(version),accountName});
        database.execSQL("DELETE FROM Flags WHERE packageName = ? AND committed = 1",new String[]{pkgName});
        database.execSQL("INSERT INTO Flags SELECT packageName, version, flagType, partitionId, user, name, intVal, boolVal, floatVal, stringVal, extensionVal, 1 AS committed FROM Flags WHERE packageName = ? AND version = ? AND user = ? AND committed = 0",
                new String[]{pkgName,String.valueOf(version),accountName});
    }

    public static experimentsDatasRead readExperimentsFlag(Context context,String pkgname,String username){
        File file = new File(context.getFilesDir(), "com.google.android.finsky.regular".equals(pkgname)?(TextUtils.isEmpty(username)?"experiment-flags-regular-null-account":"experiment-flags-regular-"+ Uri.encode(username)):"experiment-flags-process-stable");
        if(!file.exists()){
            Log.d(TAG,"File "+file.getName()+" not exists");
            return null;
        }
        try {
            DataInputStream inputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
            if(inputStream.readByte() != 1){
                throw new IOException("Unrecognized file version.");
            }
            experimentsDatasRead result = new experimentsDatasRead();
            result.setBaseToken(inputStream.readUTF(),inputStream.readUTF(),autq.ADAPTER.decode(Base64.decode(inputStream.readUTF(),3)));
            int endOfFlag = 0;
            while (endOfFlag==0){
                switch (inputStream.readByte()){
                    case 0:
                        endOfFlag = 1;
                        break;
                    case 1:
                        result.putFlag(inputStream.readUTF(), inputStream.readByte());
                        break;
                    case 2:
                        result.putFlag(inputStream.readUTF(), inputStream.readShort());
                        break;
                    case 3:
                        result.putFlag(inputStream.readUTF(), inputStream.readInt());
                        break;
                    case 4:
                        result.putFlag(inputStream.readUTF(),inputStream.readLong());
                        break;
                    case 5:
                        result.putFlag(inputStream.readUTF(),inputStream.readUTF());
                        break;
                    case 6:
                        String key = inputStream.readUTF();
                        int length = inputStream.readInt();
                        if(length>=0){
                            byte[] value = new byte[length];
                            inputStream.readFully(value);
                            result.putFlag(key,value);
                            break;
                        }
                        throw new RuntimeException("Bytes flag has negative length.");
                    case 7:
                        result.putFlag(inputStream.readUTF(),inputStream.readDouble());
                        break;
                    case 8:
                        result.putFlag(inputStream.readUTF(),inputStream.readBoolean());
                        break;
                    default:
                        throw new RuntimeException("Unknown flag type");
                }
            }
            inputStream.close();
            return result;
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public static class experimentsDatasRead {
        public String serverToken;
        public String expeIntroduce;
        public autq experimentToken;
        public final Map<String,Object> d = new HashMap<>();

        public void setBaseToken(String serverToken, String expeIntroduce, autq experimentToken) {
            this.serverToken = serverToken;
            this.expeIntroduce = expeIntroduce;
            this.experimentToken = experimentToken;
        }

        public void putFlag(String name, boolean value) {
            d.put(name,value);
        }

        public void putFlag(String name, long value) {
            d.put(name,value);
        }

        public void putFlag(String name, double value) {
            d.put(name,value);
        }

        public void putFlag(String name, String value) {
            d.put(name,value);
        }

        public void putFlag(String name, byte[] value) {
            d.put(name,value);
        }
    }

    private static void writeExperimentsFlag(aqis aqis,Context context,String pkgname,String username) {
        try {
            File file = new File(context.getFilesDir(), "com.google.android.finsky.regular".equals(pkgname)?(TextUtils.isEmpty(username)?"experiment-flags-regular-null-account":"experiment-flags-regular-"+ Uri.encode(username)):"experiment-flags-process-stable");
            DataOutputStream dataOutputStream0 = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            dataOutputStream0.writeByte(1);
            dataOutputStream0.writeUTF(aqis.d);
            dataOutputStream0.writeUTF(aqis.b);
            dataOutputStream0.writeUTF(Base64.encodeToString(buildExperimentsTokenProto(context,username,pkgname).encode(),3));
            for(aqiu flag : aqis.e ){
                if(flag.intVal!=null){
                    long value = flag.intVal;
                    if(value >= 0xFFFFFFFFFFFFFF80L && value <= 0x7FL) {
                        dataOutputStream0.writeByte(1);
                        dataOutputStream0.writeUTF(flag.d);
                        dataOutputStream0.writeByte(((int)value));
                    }
                    else if(value >= 0xFFFFFFFFFFFF8000L && value <= 0x7FFFL) {
                        dataOutputStream0.writeByte(2);
                        dataOutputStream0.writeUTF(flag.d);
                        dataOutputStream0.writeShort(((int)value));
                    }
                    else if(value >= 0xFFFFFFFF80000000L && value <= 0x7FFFFFFFL) {
                        dataOutputStream0.writeByte(3);
                        dataOutputStream0.writeUTF(flag.d);
                        dataOutputStream0.writeInt(((int)value));
                    }
                    else {
                        dataOutputStream0.writeByte(4);
                        dataOutputStream0.writeUTF(flag.d);
                        dataOutputStream0.writeLong(value);
                    }
                } else if (flag.boolVal != null) {
                    dataOutputStream0.writeByte(8);
                    dataOutputStream0.writeUTF(flag.d);
                    dataOutputStream0.writeBoolean(flag.boolVal);
                } else if (flag.floatVal != null){
                    dataOutputStream0.writeByte(7);
                    dataOutputStream0.writeUTF(flag.d);
                    dataOutputStream0.writeDouble(flag.floatVal);
                } else if (flag.stringVal != null){
                    dataOutputStream0.writeByte(5);
                    dataOutputStream0.writeUTF(flag.d);
                    dataOutputStream0.writeUTF(flag.stringVal);
                } else if (flag.extensionVal != null){
                    dataOutputStream0.writeByte(6);
                    dataOutputStream0.writeUTF(flag.d);
                    dataOutputStream0.writeInt(flag.extensionVal.size());
                    dataOutputStream0.write(flag.extensionVal.toByteArray(),0,flag.extensionVal.size());
                }
            }
            Log.d(TAG,"Finished writing experiment flags into file "+file.getName());
            dataOutputStream0.writeByte(0);
            dataOutputStream0.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static autq buildExperimentsTokenProto(Context context,String user,String pkgname){
        SQLiteDatabase db = new phenotypeDatabase(context).getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT experimentToken FROM ExperimentTokens WHERE user = ? AND packageName = ? AND version = ? AND isCommitted = 1",
                new String[]{user,pkgname, String.valueOf(exerimentsAndConfigs.version)});
        cursor.moveToNext();
        autq.Builder autq_ = new autq.Builder();
        autq_.c.add(ByteString.of(cursor.getBlob(0)));
        return autq_.build();
    }

    private static aqis buildProtoaqis(ExperimentsFlagsConfiguration configuration) {
        aqis.Builder aqis_ = new aqis.Builder()
                .b(configuration.expeIntroduce)
                .d(configuration.serverToken)
                .g(configuration.b)
                .h(configuration.servingVersion)
                .c(ByteString.of(configuration.experimentToken));

        for(FlagTypeValue typeValue : configuration.array){
            for (FlagsValue flagsValue : typeValue.values){
                aqis_.e.add(flagvalue2aqiu(flagsValue));
            }
        }

        return aqis_.build();
    }

    private static aqiu flagvalue2aqiu(FlagsValue value){
        switch (value.valueType){
            case 0:
                return new aqiu.Builder()
                        .d(value.name)
                        .intVal((long) value.intVal).build();
            case 1:
                return new aqiu.Builder()
                        .d(value.name)
                        .boolVal(value.boolVal).build();
            case 2:
                return new aqiu.Builder()
                        .d(value.name)
                        .floatVal((double) value.floatVal).build();
            case 3:
                return new aqiu.Builder()
                        .d(value.name)
                        .stringVal(value.stringVal).build();
            case 4:
                return new aqiu.Builder()
                        .d(value.name)
                        .extensionVal(ByteString.of(value.extensionVal)).build();
        }
        return null;
    }
    static class ExperimentsFlagsConfiguration{
        private String expeIntroduce;
        private String serverToken;
        private FlagTypeValue[] array;
        private boolean b;
        private byte[] experimentToken;
        private long servingVersion;

        public ExperimentsFlagsConfiguration(String expeIntroduce, String serverToken, FlagTypeValue[] array, boolean b, byte[] experimentToken, long servingVersion) {
            this.expeIntroduce = expeIntroduce;
            this.serverToken = serverToken;
            this.array = array;
            this.b = b;
            this.experimentToken = experimentToken;
            this.servingVersion = servingVersion;
        }
    }

    public static FlagsValue getFlagsValue(Cursor cursor){
        int flagtype = cursor.getInt(0);
        String name = cursor.getString(1);
        if (!cursor.isNull(2)){
            return new FlagsValue(flagtype, name, cursor.getInt(2));
        }else if (!cursor.isNull(3)){
            return new FlagsValue(flagtype,name, cursor.getInt(3) != 0);
        }else if (!cursor.isNull(4)){
            return new FlagsValue(flagtype,name,cursor.getFloat(4));
        }else if (!cursor.isNull(5)){
            return new FlagsValue(flagtype,name,cursor.getString(5));
        }else if (!cursor.isNull(6)){
            return new FlagsValue(flagtype,name,cursor.getString(6));
        }
        return null;
    }

    //用于保存同一flagtype类型的flags list
    static class FlagTypeValue {
        private int flagtype;
        private FlagsValue[] values;

        public FlagTypeValue(int type,FlagsValue[] values){
            this.flagtype = type;
            this.values = values;
        }
    }

    static class experimentsValues {
        byte[] experimentToken;
        String serverToken;
        long servingVersion;

        public experimentsValues(byte[] experimentToken,String serverToken,long servingVersion){
            this.experimentToken = experimentToken;
            this.serverToken = serverToken;
            this.servingVersion = servingVersion;
        }
    }

    static class FlagsValue implements Comparable<FlagsValue> {
        private int flagType;
        private String name;
        private int intVal;
        private boolean boolVal;
        private float floatVal;
        private String stringVal;
        private byte[] extensionVal;
        public int valueType;

        public FlagsValue(int flagType, String name, int intVal){
            this.valueType = 0;
            this.flagType = flagType;
            this.name = name;
            this.intVal = intVal;
        }

        public FlagsValue(int flagType, String name, boolean boolVal){
            this.valueType = 1;
            this.flagType = flagType;
            this.name = name;
            this.boolVal = boolVal;
        }

        public FlagsValue(int flagType, String name, float floatVal){
            this.valueType = 2;
            this.flagType = flagType;
            this.name = name;
            this.floatVal = floatVal;
        }

        public FlagsValue(int flagType, String name, String stringVal){
            this.valueType = 3;
            this.flagType = flagType;
            this.name = name;
            this.stringVal = stringVal;
        }

        public FlagsValue(int flagType, String name, byte[] extensionVal){
            this.valueType = 4;
            this.flagType = flagType;
            this.name = name;
            this.extensionVal = extensionVal;
        }

        @Override
        public int compareTo(FlagsValue flagvalue) {
            return this.name.compareTo(flagvalue.name);
        }

        public Object getValue(){
            switch (this.valueType){
                case 0:
                    return intVal;
                case 1:
                    return boolVal;
                case 2:
                    return floatVal;
                case 3:
                    return stringVal;
                case 4:
                    return extensionVal;
            }
            return null;
        }

        @Override
        public boolean equals(Object obj) {
            if(this.valueType == ((FlagsValue)obj).valueType){
                switch (this.valueType){
                    case 0:
                        return this.intVal == ((FlagsValue) obj).intVal;
                    case 1:
                        return this.boolVal == ((FlagsValue) obj).boolVal;
                    case 2:
                        return this.floatVal == ((FlagsValue) obj).floatVal;
                    case 3:
                        return this.stringVal.equals(((FlagsValue) obj).stringVal);
                    case 4:
                        return this.extensionVal.equals(((FlagsValue) obj).extensionVal);
                }
            }
            return false;
        }
    }
    static int caculateHash(autt autt0){
        int v=0;
        for(auuc auuc0 : autt0.c){
            auud auud0 = auuc0.a;
            if(auud0==null) auud0 = new auud.Builder().build();
            long v1 = auud0.b;
            long v2 = v1 ^ auud0.b >>> 0x20;
            int v3 = (int)v2;
            byte[] arr_b = auud0.c.toByteArray();
            for(int v4 = 0; v4 < arr_b.length; ++v4) {
                v3 = v3 * 0x1F + arr_b[v4];
            }
            v = v * 17 ^ v3;
        }
        return v;
    }

    public static byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];

        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        return buffer.toByteArray();
    }


}
