package com.eafy.rn;

import androidx.annotation.Nullable;

import com.eafy.zjmediaplayer.Listener.ZJDisplayRatioType;
import com.eafy.zjmediaplayer.Listener.ZJMediaPlayerListener;
import com.eafy.zjmediaplayer.ZJAuthManager;
import com.eafy.zjmediaplayer.ZJMediaPlayer;
import com.eafy.zjmediaplayer.ZJMediaStatisticalInfo;
import com.eafy.zjmediaplayer.ZJMediaStreamPlayer;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import java.util.HashMap;
import java.util.Map;

public class RNMediaPlayer extends ReactContextBaseJavaModule implements ZJMediaPlayerListener {

    public static final String kOnMediaPlayerPlayStatus = "onMediaPlayerPlayStatus";
    public static final String kOnMediaPlayerStatisticalInfo = "onMediaPlayerStatisticalInfo";
    public static final String kOnMediaPlayerRecordStatus = "onMediaPlayerRecordStatus";

    private ReactApplicationContext reactContext = null;
    private ZJMediaPlayer mediaPlayer = null;
    public RNZJGLMonitor monitor = null;

    public RNMediaPlayer(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "MediaPlayer";
    }

    @Nullable
    @Override
    public Map<String, Object> getConstants() {
        Map<String, Object> constants = new HashMap<>();
        constants.put(kOnMediaPlayerPlayStatus, kOnMediaPlayerPlayStatus);
        constants.put(kOnMediaPlayerStatisticalInfo, kOnMediaPlayerStatisticalInfo);
        constants.put(kOnMediaPlayerRecordStatus, kOnMediaPlayerRecordStatus);
        return constants;
    }

    private void pushEvent(String name, WritableMap data) {
        if (reactContext == null) return;
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(name, data);
    }

    public ZJMediaPlayer mediaPlayer() {
        if (mediaPlayer != null) return mediaPlayer;

        mediaPlayer = new ZJMediaPlayer(reactContext.getApplicationContext());
        return mediaPlayer;
    }

    @ReactMethod
    public void openDebugMode() {
        ZJAuthManager.shared().openDebugMode();
    }

    @ReactMethod
    public void configWithKey(String key, String secret, Promise cb) {
        boolean ret = ZJAuthManager.shared().config(reactContext.getApplicationContext(), key, secret);
        if (cb != null) {
            cb.resolve(ret);
        }
    }

    @ReactMethod
    public void isAuthOK(Promise cb) {
        if (cb == null) return;
        cb.resolve(ZJAuthManager.shared().isDebugMode());
    }

    @ReactMethod
    public void playerId(Promise cb) {
        if (cb == null) return;
        cb.resolve(mediaPlayer().hashCode());
    }

    @ReactMethod
    public void addMonitor(int viewId) {
        RNZJGLMonitor monitor = RNMonitorManager.shared().getMonitor(viewId);
        if (monitor == null) return;
        mediaPlayer().addMonitor(monitor);
        if (this.monitor != null) {
            this.monitor.mediaPlayer = null;
        }
        this.monitor = monitor;
        monitor.mediaPlayer = this;
    }

    @ReactMethod
    public void start(String url) {
        mediaPlayer().setListener(this);
        this.mediaPlayer.startURL(url);
    }

    @ReactMethod
    public void restart() {
        mediaPlayer().restart();
    }

    @ReactMethod
    public void seek(int msTime) {
        mediaPlayer().seek(msTime);
    }

    @ReactMethod
    public void stop() {
        mediaPlayer().setListener(null);
        mediaPlayer().stop();
    }

    @ReactMethod
    public void openVideo() {
        mediaPlayer().openVideo();
    }

    @ReactMethod
    public void closeVideo() {
        mediaPlayer().closeVideo();
    }

    @ReactMethod
    public void mute(boolean mute) {
        mediaPlayer().setMute(mute);
    }

    @ReactMethod
    public void setMediaCacheTime(int cacheTime, int pursueTime) {
        mediaPlayer().setMediaCacheTime(cacheTime, pursueTime);
    }

    @ReactMethod
    public void setDenoiseLevel(int nLevel, float gainVolume) {
        mediaPlayer().setDenoiseLevel(nLevel, gainVolume);
    }

    @ReactMethod
    public void setMediaSyncMode(boolean sync) {
        mediaPlayer().setMediaSyncMode(sync);
    }

    @ReactMethod
    public void startRecord(String path) {
        mediaPlayer().startRecord(path);
    }

    @ReactMethod
    public void stopRecord() {
        mediaPlayer().stopRecord();
    }

    @ReactMethod
    public void isRecording(Promise cb) {
        if (cb == null) return;
        cb.resolve(mediaPlayer().isRecording());
    }

    @ReactMethod
    public void getRecordDuration(Promise cb) {
        if (cb == null) return;
        cb.resolve(mediaPlayer().getRecordDuration());
    }

    /*************************   ZJMediaPlayerListener   *****************************/

    @Override
    public void didMediaPlayerPlay(ZJMediaStreamPlayer player, int status, int errCode) {
        WritableMap map = Arguments.createMap();
        map.putInt("playerId", player.hashCode());
        map.putInt("status", status);
        map.putInt("errCode", errCode);
        pushEvent(kOnMediaPlayerPlayStatus, map);
    }

    @Override
    public void didMediaPlayerStatisticalInfo(ZJMediaStreamPlayer player, ZJMediaStatisticalInfo statInfo) {
        WritableMap map = Arguments.createMap();
        map.putInt("playerId", player.hashCode());
        map.putInt("videoWidth", statInfo.videoWidth);
        map.putInt("videoHeight", statInfo.videoHeight);
        map.putInt("videoFPS", statInfo.videoFPS);
        map.putInt("videoBps", statInfo.videoBps);
        map.putInt("audioBps", statInfo.audioBps);
        map.putInt("timestamp", (int)statInfo.timestamp);
        map.putInt("videoTotalFrame", statInfo.videoTotalFrame);
        map.putInt("videoDropFrame", statInfo.videoDropFrame);
        map.putInt("onlineCount", statInfo.onlineCount);
        pushEvent(kOnMediaPlayerStatisticalInfo, map);
    }

    @Override
    public void didMediaPlayerRecord(ZJMediaStreamPlayer player, int status, String filePath, int errCode) {
        WritableMap map = Arguments.createMap();
        map.putInt("playerId", player.hashCode());
        map.putInt("status", status);
        map.putInt("errCode", errCode);
        map.putString("filePath", filePath);
        pushEvent(kOnMediaPlayerRecordStatus, map);
    }
}
