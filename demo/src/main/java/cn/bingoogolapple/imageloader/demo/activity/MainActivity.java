package cn.bingoogolapple.imageloader.demo.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.bingoogolapple.imageloader.BGAImage;
import cn.bingoogolapple.imageloader.demo.App;
import cn.bingoogolapple.imageloader.demo.R;
import cn.bingoogolapple.imageloader.demo.engine.ApiEngine;
import cn.bingoogolapple.imageloader.demo.model.NormalModel;
import cn.bingoogolapple.imageloader.demo.util.UIUtil;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private static final int REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE = 1;

    private RecyclerView mContentRv;
    private ContentAdapter mContentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContentRv = (RecyclerView) findViewById(R.id.rv_main_content);
        mContentRv.setLayoutManager(new LinearLayoutManager(this));
        mContentAdapter = new ContentAdapter(mContentRv);
        mContentRv.setAdapter(mContentAdapter);

        loadDataWrapper();
    }

    private void loadData() {
        App.getInstance().getRetrofit().create(ApiEngine.class).getNormalModels().enqueue(new Callback<List<NormalModel>>() {
            @Override
            public void onResponse(Call<List<NormalModel>> call, Response<List<NormalModel>> response) {
                List<NormalModel> models = response.body();
                models.get(0).avatorPath = "/storage/emulated/0/Meizhi/6.12 珍爱生命，请远离损友！！.jpg";
                models.get(1).avatorPath = "/storage/emulated/0/Meizhi/10.23 给仍在命运中的你：励志短片《命运》.jpg";
                models.get(2).avatorPath = "/storage/emulated/0/Meizhi/10.22 街头极限健身Greg Plitt 格雷格普利特励志演讲, Greg Plitt是一传奇人物 .jpg";
                models.get(3).avatorPath = "/storage/emulated/0/Meizhi/10.12 8分钟将10部好莱坞经典机器人电影连成一个故事.jpg";
                mContentAdapter.setData(models);
            }

            @Override
            public void onFailure(Call<List<NormalModel>> call, Throwable t) {
                Toast.makeText(App.getInstance(), "数据加载失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE)
    private void loadDataWrapper() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            loadData();
        } else {
            EasyPermissions.requestPermissions(this, "加载本地图片需要以下权限:\n\n1.访问您设备上的照片", REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE) {
            Toast.makeText(this, "您拒绝了「加载本地图片」所需要的相关权限!", Toast.LENGTH_SHORT).show();
            loadData();
        }
    }

    public class ContentAdapter extends BGARecyclerViewAdapter<NormalModel> {

        public ContentAdapter(RecyclerView recyclerView) {
            super(recyclerView, R.layout.item_normal);
        }

        @Override
        public void fillData(BGAViewHolderHelper viewHolderHelper, int position, NormalModel model) {
            BGAImage.displayImage(MainActivity.this, viewHolderHelper.getImageView(R.id.iv_item_main_avatar), model.avatorPath, R.mipmap.holder, R.mipmap.holder, UIUtil.dp2px(50), UIUtil.dp2px(50), null);
            viewHolderHelper.setText(R.id.tv_item_main_title, model.title).setText(R.id.tv_item_main_detail, model.detail);
        }
    }
}