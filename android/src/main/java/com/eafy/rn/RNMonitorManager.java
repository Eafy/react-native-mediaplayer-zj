package com.eafy.rn;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class RNMonitorManager {
    private static RNMonitorManager mInstance = null;
    private Context mContext = null;
    public Map<Integer, RNZJGLMonitor> monitorDic = new HashMap<>();

    public RNMonitorManager() {
    }

    public static synchronized RNMonitorManager shared(){
        if (mInstance == null) {
            mInstance = new RNMonitorManager();
        }
        return mInstance;
    }

    public void addMonitor(RNZJGLMonitor monitor) {
        if (monitor == null) return;
        monitorDic.put(monitor.hashCode(), monitor);
    }

    public void removeMonitor(int viewId) {
        monitorDic.remove(viewId);
    }

    public RNZJGLMonitor getMonitor(int viewId) {
        return monitorDic.get(viewId);
    }
}
