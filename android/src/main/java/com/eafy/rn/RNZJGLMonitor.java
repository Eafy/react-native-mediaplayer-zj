package com.eafy.rn;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.eafy.zjmediaplayer.Video.ZJGLMonitor;
import com.eafy.zjmediaplayer.ZJMediaPlayer;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeHolder;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.CloseableStaticBitmap;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.react.bridge.ReadableMap;

import javax.annotation.Nullable;

public class RNZJGLMonitor extends ZJGLMonitor {

    private DataSource<CloseableReference<CloseableImage>> dataSource = null;
    private static DraweeHolder<?> imageHolder = null;
    public RNMediaPlayer mediaPlayer = null;

    public RNZJGLMonitor(Context context) {
        super(context);
        RNMonitorManager.shared().addMonitor(this);
    }

    public RNZJGLMonitor(Context context, AttributeSet attrs) {
        super(context, attrs);
        RNMonitorManager.shared().addMonitor(this);
    }

    public RNZJGLMonitor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        RNMonitorManager.shared().addMonitor(this);
    }

    public RNZJGLMonitor(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        RNMonitorManager.shared().addMonitor(this);
    }

    public void onDestroy() {
        RNMonitorManager.shared().removeMonitor(this.hashCode());
        if (mediaPlayer != null && mediaPlayer.monitor == this) {
            mediaPlayer.mediaPlayer().removeMonitor();
            mediaPlayer.monitor = null;
        }
    }

    private final ControllerListener<ImageInfo> imageControllerListener = new BaseControllerListener<ImageInfo>() {
        @Override
        public void onFinalImageSet(String id, @Nullable final ImageInfo imageInfo, @Nullable Animatable animatable) {
            CloseableReference<CloseableImage> imageReference = null;
            try {
                imageReference = dataSource.getResult();
                if (imageReference != null) {
                    CloseableImage image = imageReference.get();
                    if (image != null && image instanceof CloseableStaticBitmap) {
                        CloseableStaticBitmap closeableStaticBitmap = (CloseableStaticBitmap) image;
                        Bitmap bitmap = closeableStaticBitmap.getUnderlyingBitmap();
                        if (bitmap != null) {
                            displayBitmap(bitmap);
                        }
                    }
                }
            } finally {
                dataSource.close();
                if (imageReference != null) {
                    CloseableReference.closeSafely(imageReference);
                }
            }
        }
    };

    public void setImage(ReadableMap imgMap) {
        String url = imgMap.getString("uri");
        if (!TextUtils.isEmpty(url)) {
            if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("file://") || url.startsWith("asset://")) {
                ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url)).build();
                ImagePipeline imagePipeline = Fresco.getImagePipeline();
                dataSource = imagePipeline.fetchDecodedImage(imageRequest, this);
                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setImageRequest(imageRequest)
                        .setControllerListener(imageControllerListener)
                        .setOldController(imageHolder.getController())
                        .build();
                imageHolder.setController(controller);
            } else {
                int resId = this.getResources().getIdentifier(url, "drawable", this.getContext().getPackageName());
                Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), resId);
                if (bitmap != null) {
                    displayBitmap(bitmap);
                }
            }
        }
    }
}
