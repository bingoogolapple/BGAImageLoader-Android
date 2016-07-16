package cn.bingoogolapple.imageloader;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import cn.bingoogolapple.imageloader.bga.BGAImageLoader;
import cn.bingoogolapple.imageloader.third.BGAGlideImageLoader;
import cn.bingoogolapple.imageloader.third.BGAPicassoImageLoader;
import cn.bingoogolapple.imageloader.third.BGAUILImageLoader;
import cn.bingoogolapple.imageloader.third.BGAXUtilsImageLoader;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/6/25 下午5:03
 * 描述:
 */
public class BGAImage {
    private static BGAAbstractImageLoader sImageLoader;

    private BGAImage() {
    }

    private static final BGAAbstractImageLoader getImageLoader() {
        if (sImageLoader == null) {
            synchronized (BGAImage.class) {
                if (sImageLoader == null) {
                    if (isClassExists("com.bumptech.glide.Glide")) {
                        sImageLoader = new BGAGlideImageLoader();
                    } else if (isClassExists("com.squareup.picasso.Picasso")) {
                        sImageLoader = new BGAPicassoImageLoader();
                    } else if (isClassExists("com.nostra13.universalimageloader.core.ImageLoader")) {
                        sImageLoader = new BGAUILImageLoader();
                    } else if (isClassExists("org.xutils.x")) {
                        sImageLoader = new BGAXUtilsImageLoader();
                    } else {
//                        throw new RuntimeException("必须在你的build.gradle文件中配置「Glide、Picasso、universal-image-loader、XUtils3」中的某一个图片加载库的依赖");
                        sImageLoader = BGAImageLoader.getInstance();
                    }
                }
            }
        }
        return sImageLoader;
    }

    private static final boolean isClassExists(String classFullName) {
        try {
            Class.forName(classFullName);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static void displayImage(Activity activity, ImageView imageView, String path, @DrawableRes int loadingResId, @DrawableRes int failResId, int width, int height, final BGAAbstractImageLoader.DisplayDelegate delegate) {
        getImageLoader().displayImage(activity, imageView, path, loadingResId, failResId, width, height, delegate);
    }

    public static void downloadImage(Context context, String path, final BGAAbstractImageLoader.DownloadDelegate delegate) {
        getImageLoader().downloadImage(context, path, delegate);
    }
}
