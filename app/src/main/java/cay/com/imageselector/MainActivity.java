package cay.com.imageselector;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import okhttp3.Call;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private static final String TAG = "chen";
    private TextView mTextView;
    private Button upLoad;
    private List<String> path;
    private int num;
    private int i;
    private int aaa;
    private TextView total;
    private TextView progess1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.select_image);
        mTextView = (TextView) findViewById(R.id.text1);
        upLoad = (Button) findViewById(R.id.upload);
        total = (TextView) findViewById(R.id.total);
        progess1 = (TextView) findViewById(R.id.progress1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MultiImageSelector.create(MainActivity.this).showCamera(true).count(20).multi().start(MainActivity.this,2);
                total.setVisibility(View.GONE);
                progess1.setVisibility(View.GONE);

            }
        });
        upLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aaa = 0;
                total.setVisibility(View.VISIBLE);
                total.setText("总共上传图片数量："+num+"张");
                progess1.setVisibility(View.VISIBLE);
                progess1.setText("正在上传图片中，请勿退出");
                for (  i=0;i<num;i++) {
                    String[] names = path.get(i).split("\\/");
                    String imageName = names[(names.length) - 1];
                    File file = new File(path.get(i));
                    Log.i(TAG, "onClick: "+path.get(i));
                    Log.i(TAG, "大小: "+file.length());
                    OkHttpUtils.post().addFile("nihhh",imageName,file).addParams("AAAAA","hahhaha").url("http://118.192.157.178:8080/XiaoWei/servlet/UploadFile").build().execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Log.i(TAG, "onError: ");
                            aaa++;
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.i(TAG, "onResponse: "+i);
                            aaa++;
                            if (aaa < (num - 1)) {
                                progess1.setText("已上传成功图片" + aaa + "张");

                            } else {
                                progess1.setText("成功上传所有图片");

                            }
                            Log.i(TAG, "aaa: "+aaa);
                        }
                    });

                }


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2){
            if(resultCode == RESULT_OK){
                // 获取返回的图片列表
                path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                num =path.size();
                mTextView.setText("你已选择"+num+"图片");
                for (int i =0;i<num;i++) {
                    Log.i(TAG, "第"+i+"张图片路径: "+path.get(i));
                }

                // 处理你自己的逻辑 ....
            }
        }

    }
}
