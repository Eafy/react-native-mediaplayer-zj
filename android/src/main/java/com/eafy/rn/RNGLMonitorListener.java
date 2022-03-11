package com.eafy.rn;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.eafy.zjmediaplayer.Listener.ZJGLMonitorListener;
import com.eafy.zjmediaplayer.Video.ZJGLMonitor;
import com.facebook.react.bridge.Promise;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class RNGLMonitorListener implements ZJGLMonitorListener {
    private static final String SAVE_PIC_PATH= Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory().getAbsolutePath() : "/mnt/sdcard";//保存到SD卡
    private static final String SAVE_REAL_PATH = SAVE_PIC_PATH + "/RNMediaPlayer/Pic";//保存的确切位置

    private Context context = null;
    private Promise cb = null;

    public RNGLMonitorListener(Context context, Promise cb) {
        this.context = context;
        this.cb = cb;
    }

    @Override
    public void didSnapshot(ZJGLMonitor monitor, Bitmap bitmap) {
        if (cb == null) return;
        if (bitmap != null) {
            Calendar now = new GregorianCalendar();
            String fileName = String.valueOf(now.getTime().getTime()) + ".png";
            try {
                String path = SAVE_REAL_PATH + fileName + ".jpg";
                File file = new File(path);
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
                cb.resolve(path);
            } catch (Exception e) {
                e.printStackTrace();
                cb.reject("-2", "Failed to save image");
            }
        } else {
            cb.reject("-3", "Failed to get image");
        }
    }
}
