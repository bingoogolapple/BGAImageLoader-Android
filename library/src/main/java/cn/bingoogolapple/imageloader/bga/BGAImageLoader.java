package cn.bingoogolapple.imageloader.bga;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.bingoogolapple.imageloader.BGAAbstractImageLoader;
import cn.bingoogolapple.imageloader.util.BGAImageLoaderUtil;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/7/14 下午4:37
 * 描述:
 */
public class BGAImageLoader extends BGAAbstractImageLoader {
    private static BGAImageLoader mInstance;
    private BGAMemoryCache mMemoryCache;
    private BGADiskCache mDiskCache;
    private ExecutorService mExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private BGAImageLoader() {
        mMemoryCache = new BGAMemoryCache();
        mDiskCache = new BGADiskCache();
    }

    public static BGAImageLoader getInstance() {
        if (mInstance == null) {
            synchronized (BGAImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new BGAImageLoader();
                }
            }
        }
        return mInstance;
    }

    @Override
    public void displayImage(Activity activity, ImageView imageView, String path, @DrawableRes int loadingResId, @DrawableRes int failResId, int width, int height, DisplayDelegate delegate) {
        path = getPath(path);

        imageView.setImageResource(loadingResId);
        Bitmap bitmap = mMemoryCache.get(path);
        if (bitmap != null) {
            BGAImageLoaderUtil.log("从内存加载图片");
            imageView.setImageBitmap(bitmap);
            if (delegate != null) {
                delegate.onSuccess(imageView, path);
            }
            return;
        }

        mDiskCache.init(activity);
        bitmap = mDiskCache.get(path);
        if (bitmap != null) {
            BGAImageLoaderUtil.log("从本地加载图片");
            imageView.setImageBitmap(bitmap);
            mMemoryCache.put(path, bitmap);
            if (delegate != null) {
                delegate.onSuccess(imageView, path);
            }
            return;
        }

        if (BGAImageLoaderUtil.isFileScheme(path)) {
            BGAImageLoaderUtil.log("加载「file://」文件失败");
            imageView.setImageResource(failResId);
        } else {
            getBitmapFromNet(activity, imageView, path, failResId, width, height, delegate);
        }
    }

    @Override
    public void downloadImage(Context context, final String path, final DownloadDelegate delegate) {
        // TODO 待优化
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = downloadBitmap(path);
                if (delegate != null) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            if (bitmap != null) {
                                delegate.onSuccess(path, bitmap);
                            } else {
                                delegate.onFailed(path);
                            }
                        }
                    });
                }
            }
        });
    }

    private void getBitmapFromNet(Activity activity, final ImageView imageView, final String path, @DrawableRes final int failResId, int width, int height, final DisplayDelegate delegate) {
        BGAImageLoaderUtil.log("从网络加载图片");

        imageView.setTag(path);
        // TODO 待优化
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = downloadBitmap(path);
                if (path.equals(imageView.getTag())) {
                    if (bitmap != null) {
                        imageView.post(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(bitmap);
                                if (delegate != null) {
                                    delegate.onSuccess(imageView, path);
                                }
                            }
                        });
                        mMemoryCache.put(path, bitmap);
                        mDiskCache.put(path, bitmap);
                    } else {
                        imageView.post(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageResource(failResId);
                            }
                        });
                    }
                }
            }
        });
    }

    private Bitmap downloadBitmap(String url) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                return BitmapFactory.decodeStream(conn.getInputStream());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }
}