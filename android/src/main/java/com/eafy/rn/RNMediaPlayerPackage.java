
package com.eafy.rn;

import android.os.Looper;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

public class RNMediaPlayerPackage implements ReactPackage {

    @NonNull
    @Override
    public List<NativeModule> createNativeModules(@NonNull ReactApplicationContext reactContext) {
        List<NativeModule> modules = new ArrayList<>();
        modules.add(new RNMediaPlayer(reactContext));
        return modules;
    }

    @NonNull
    @Override
    public List<ViewManager> createViewManagers(@NonNull ReactApplicationContext reactContext) {
        init(reactContext);
        List<ViewManager> views  = new ArrayList<>();
        views.add(new RNGLMonitorViewManager(reactContext));
        return views;
    }

    @MainThread
    protected void init(ReactApplicationContext reactContext) {
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
    }
}