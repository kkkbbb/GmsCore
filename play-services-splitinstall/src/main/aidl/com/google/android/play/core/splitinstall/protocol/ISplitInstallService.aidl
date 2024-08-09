// ISplitInstallService.aidl
package com.google.android.play.core.splitinstall.protocol;
import com.google.android.play.core.splitinstall.protocol.ISplitInstallServiceCallback;

// Declare any non-default types here with import statements

interface ISplitInstallService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void startInstall(String pkg,in List<Bundle> splits,in Bundle bundle0, ISplitInstallServiceCallback callback) = 1;
    void completeInstalls(String pkg, int v,in Bundle bundle0, ISplitInstallServiceCallback callback) = 2;
    void cancelInstall(String pkg, int v, ISplitInstallServiceCallback callback) = 3;
    void getSessionState(String pkg, int v, ISplitInstallServiceCallback callback) = 4;
    void getSessionStates(String pkg, ISplitInstallServiceCallback callback) = 5;
    void splitRemoval(String pkg,in List<Bundle> splits, ISplitInstallServiceCallback callback) = 6;
    void splitDeferred(String pkg,in List<Bundle> splits,in Bundle bundle0, ISplitInstallServiceCallback callback) = 7;
    void getSessionStat2(String pkg, ISplitInstallServiceCallback callback) = 8;
    void getSessionStates2(String pkg, ISplitInstallServiceCallback callback) = 9;
    void getSplitsAppUpdate(String pkg, ISplitInstallServiceCallback callback) = 10;
    void completeInstallAppUpdate(String pkg, ISplitInstallServiceCallback callback) = 11;
    void languageSplitInstall(String pkg,in List<Bundle> splits,in Bundle bundle0, ISplitInstallServiceCallback callback) = 12;
    void languageSplitUninstall(String pkg,in List<Bundle> splits, ISplitInstallServiceCallback callback) =13;
}