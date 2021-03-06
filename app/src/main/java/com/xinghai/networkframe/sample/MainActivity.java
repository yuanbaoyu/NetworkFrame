package com.xinghai.networkframe.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xinghai.networkframe.sample.entity.Ip;
import com.xinghai.networkframe.sample.net.A;
import com.xinghai.networkframe.sample.net.HttpBinService;
import com.xinghai.networkframelib.BaseObserver;
import com.xinghai.networkframelib.RetrofitClient;
import com.xinghai.networkframelib.common.CommonRetryWhenFunction;
import com.xinghai.networkframelib.common.transformer.RxSchedulersHelper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button rxBtn, retrofitBtn;
    private TextView contentTv;

    private CompositeDisposable mDisposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rxBtn = (Button) findViewById(R.id.rxBtn);
        rxBtn.setOnClickListener(this);

        retrofitBtn = (Button) findViewById(R.id.retrofitBtn);
        retrofitBtn.setOnClickListener(this);

        contentTv = (TextView) findViewById(R.id.contentTv);
        contentTv.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposables.clear();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rxBtn:
                testRx();
                break;
            case R.id.retrofitBtn:
                testRetrofit();
                break;
            default:
                break;
        }
    }


    public void testRetrofit() {

        RetrofitClient.getInstance().create(HttpBinService.class).getIp().enqueue(new Callback<Ip>() {
            @Override
            public void onResponse(Call<Ip> call, Response<Ip> response) {
                Log.e("===>", "retrofit onResponse");
                if(response.isSuccessful())
                    contentTv.setText(response.body().origin);
            }

            @Override
            public void onFailure(Call<Ip> call, Throwable t) {
                Log.e("===>", "retrofit onFailure");

            }
        });

    }

    public void testRx() {
        RetrofitClient.getInstance().create(A.class).get()
                .debounce(10, TimeUnit.SECONDS)
                .retryWhen(new CommonRetryWhenFunction())
                .compose(RxSchedulersHelper.<ResponseBody>io_main())
                .subscribe(new BaseObserver<ResponseBody>(this) {

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        mDisposables.add(d);
                    }

                    @Override
                    public void onNext(ResponseBody value) {
                        super.onNext(value);

                        try {
                            contentTv.setText(value.string());
                        } catch (IOException e) {
                            onError(e);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }
                });
    }

}
