package cn.bingoogolapple.imageloader;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/7/14 下午4:37
 * 描述:
 */
public class BGAImageLoader extends BGAAbstractImageLoader {
    private static BGAImageLoader mInstance;

    private BGAImageLoader() {
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

    }

    @Override
    public void downloadImage(Context context, String path, DownloadDelegate delegate) {

    }
}