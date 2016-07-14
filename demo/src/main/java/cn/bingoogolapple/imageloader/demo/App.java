package cn.bingoogolapple.imageloader.demo;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/7/14 下午2:30
 * 描述:
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}