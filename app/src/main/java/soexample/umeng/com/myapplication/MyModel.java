package soexample.umeng.com.myapplication;

import android.util.Log;

import com.bwie.jingdong.ICallBack.ICallBack;
import com.bwie.jingdong.bean.Bean;
import com.bwie.jingdong.bean.LoginBean;
import com.bwie.jingdong.bean.RegisterBean;
import com.bwie.jingdong.utils.Api;
import com.bwie.jingdong.utils.HttpUtils;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by 夏威夷丶 on 2018/11/12.
 */

public class MyModel {

    // 获取登录信息
    public void getLoginData(String mobile, String password, final ICallBack callBack) {
        Api api = HttpUtils.getInstance().create(Api.class);
        api.getLoginData(mobile, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginBean>() {
                    @Override
                    public void accept(LoginBean loginBean) throws Exception {
                        if (loginBean != null) {
                            callBack.success(loginBean);
                        }
                        return;
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i("MyModel：throwable：", throwable.getMessage());
                        callBack.error(new Exception("请检查网络"));
                    }
                });
    }

    // 获取注册信息
    public void getRegisterData(String mobile, String password, final ICallBack callBack) {
        Api api = HttpUtils.getInstance().create(Api.class);
        api.getRegisterData(mobile, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RegisterBean>() {
                    @Override
                    public void accept(RegisterBean registerBean) throws Exception {
                        if (registerBean != null) {
                            callBack.success(registerBean);
                        }
                        return;
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i("MyModel：throwable：", throwable.getMessage());
                        callBack.error(new Exception("请检查网络"));
                    }
                });
    }

    // 获取用户信息
    public void getUserMangerData(int uid, String token, final ICallBack callBack) {
        Api api = HttpUtils.getInstance().create(Api.class);
        api.getUserManger(uid, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginBean>() {
                    @Override
                    public void accept(LoginBean loginBean) throws Exception {
                        if (loginBean != null && "0".equals(loginBean.getCode())) {
                            callBack.success(loginBean);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i("MyModel：throwable：", throwable.getMessage());
                        callBack.error(new Exception("请检查网络"));
                    }
                });
    }
    // 上传头像
    public void UploadModel(String uid, File file, final ICallBack callBack) {
        Api api = HttpUtils.getInstance().create(Api.class);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        api.upLoad(uid, part)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bean>() {
                    @Override
                    public void accept(Bean bean) throws Exception {
                        if (bean != null) {
                            callBack.success(bean);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i("HomeModel：throwable：", throwable.getMessage());
                        callBack.error(new Exception("上传失败"));
                    }
                });
    }
}
