package soexample.umeng.com.myapplication;

import com.bwie.jingdong.ICallBack.ICallBack;
import com.bwie.jingdong.base.BasePresenter;
import com.bwie.jingdong.bean.Bean;
import com.bwie.jingdong.bean.LoginBean;
import com.bwie.jingdong.bean.RegisterBean;
import com.bwie.jingdong.myfragment.model.MyModel;
import com.bwie.jingdong.myfragment.view.MView;

import java.io.File;

/**
 * Created by 夏威夷丶 on 2018/11/12.
 */

public class MyPresenter extends BasePresenter<MView> {

    private MyModel model;

    @Override
    protected void initModel() {
        model = new MyModel();
    }

    public void getLoginData(String mobile, String password) {
        model.getLoginData(mobile, password, new ICallBack() {
            @Override
            public void success(Object obj) {
                LoginBean loginBean = (LoginBean) obj;
                if (iview != null) {
                    iview.loginsuccess(loginBean);
                }
            }

            @Override
            public void error(Exception e) {
                if (iview != null) {
                    iview.error(new Exception("请求失败"));
                }
            }
        });
    }

    public void getRegisterData(String mobile, String password) {
        model.getRegisterData(mobile, password, new ICallBack() {
            @Override
            public void success(Object obj) {
                RegisterBean registerBean = (RegisterBean) obj;
                if (iview != null) {
                    iview.registersuccess(registerBean);
                    return;
                }
            }

            @Override
            public void error(Exception e) {
                if (iview != null) {
                    iview.error(e);
                }
            }
        });
    }

    public void getUserMangerData(int uid, String token) {
        model.getUserMangerData(uid, token, new ICallBack() {
            @Override
            public void success(Object obj) {
                LoginBean loginBean = (LoginBean) obj;
                if (iview != null) {
                    iview.loginsuccess(loginBean);
                }
            }

            @Override
            public void error(Exception e) {
                if (iview != null) {
                    iview.error(e);
                }
            }
        });
    }

    // 上传头像
    public void UploadModel(String uid, File file) {
        model.UploadModel(uid, file, new ICallBack() {
            @Override
            public void success(Object obj) {
                if (iview != null) {
                    Bean bean = (Bean) obj;
                    iview.beansuccess(bean);
                }
            }

            @Override
            public void error(Exception e) {
                if (iview != null) {
                    iview.error(e);
                }
            }
        });
    }
}
