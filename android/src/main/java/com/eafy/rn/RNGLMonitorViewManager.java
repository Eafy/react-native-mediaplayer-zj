package com.eafy.rn;

import android.graphics.Bitmap;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.eafy.zjmediaplayer.Listener.ZJDisplayRatioType;
import com.eafy.zjmediaplayer.Listener.ZJGLMonitorListener;
import com.eafy.zjmediaplayer.Video.ZJGLMonitor;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RNGLMonitorViewManager extends SimpleViewManager<RNZJGLMonitor> {

    public static final String kOnViewInfo = "onViewInfo";

    private ReactApplicationContext mReactContext = null;

    public RNGLMonitorViewManager(ReactApplicationContext reactContext){
        this.mReactContext = reactContext;
    }

    @NonNull
    @Override
    public String getName() {
        return "GLMonitor";
    }

    @NonNull
    @Override
    protected RNZJGLMonitor createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new RNZJGLMonitor(reactContext);
    }

    @Override
    public void onDropViewInstance(@NonNull RNZJGLMonitor view) {
        view.onDestroy();
        super.onDropViewInstance(view);
    }

    private void pushEvent(RNZJGLMonitor view, String name, WritableMap data) {
        mReactContext.getJSModule(RCTEventEmitter.class).receiveEvent(view.getId(), name, data);
    }

    @Nullable
    @Override
    public Map<String, Object> getConstants() {
        Map<String, Object> constants = new HashMap<>();
        constants.put("RatioType_ScaleAspectFit", ZJDisplayRatioType.ScaleAspectFit);     //ScaleAspectFit
        constants.put("RatioType_ScaleToFill", ZJDisplayRatioType.ScaleToFill);
        constants.put("RatioType_ScaleAspectFill", ZJDisplayRatioType.ScaleAspectFill);
        constants.put("RatioType_1_1", ZJDisplayRatioType.R_1_1);
        constants.put("RatioType_16_9", ZJDisplayRatioType.R_16_9);
        constants.put("RatioType_4_3", ZJDisplayRatioType.R_4_3);
        return constants;
    }

    @Override
    @Nullable
    public Map getExportedCustomDirectEventTypeConstants() {
        Map<String, Map<String, String>> map = MapBuilder.of(
                kOnViewInfo, MapBuilder.of("registrationName", kOnViewInfo)
        );

        return map;
    }

    @Override
    public void receiveCommand(@NonNull RNZJGLMonitor root, int commandId, @Nullable ReadableArray args) {
        super.receiveCommand(root, commandId, args);

        switch (commandId) {
            case 1:
                WritableMap event = Arguments.createMap();
                event.putInt("viewId", this.hashCode());
                pushEvent(root, kOnViewInfo, event);
                break;

        }
    }

    @Override
    public void receiveCommand(@NonNull RNZJGLMonitor root, String commandId, @Nullable ReadableArray args) {
        super.receiveCommand(root, commandId, args);
    }

    @ReactProp(name = "displayRatioType")
    public void displayRatioType(RNZJGLMonitor view, int displayRatioType) {
        view.displayRatioType(displayRatioType);
    }

    @ReactProp(name = "image")
    public void showMapPoi(RNZJGLMonitor view, ReadableMap imageMap) {
        view.setImage(imageMap);
    }

    @ReactMethod
    public void getImage(int viewId, Promise cb) {
        if (cb == null) return;
        RNZJGLMonitor monitor = RNMonitorManager.shared().getMonitor(viewId);
        if (monitor != null) {
            monitor.snapshot(new RNGLMonitorListener(mReactContext, cb));
        } else {
            cb.reject("-1", "Parameter error");
        }
    }

    @ReactMethod
    public void snapshot(int viewId, Promise cb) {
        if (cb == null) return;
        RNZJGLMonitor monitor = RNMonitorManager.shared().getMonitor(viewId);
        if (monitor != null) {
            monitor.snapshot(new RNGLMonitorListener(mReactContext, cb));
        } else {
            cb.reject("-1", "Parameter error");
        }
    }
}
