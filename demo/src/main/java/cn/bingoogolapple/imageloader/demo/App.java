package cn.bingoogolapple.imageloader.demo;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/7/14 下午2:30
 * 描述:
 */
public class App extends Application {
    private static App sInstance;
    private Retrofit mRetrofit;

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        sInstance = this;

        mRetrofit = new Retrofit.Builder()
                .baseUrl("http://7xk9dj.com1.z0.glb.clouddn.com/adapter/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static App getInstance() {
        return sInstance;
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }
}