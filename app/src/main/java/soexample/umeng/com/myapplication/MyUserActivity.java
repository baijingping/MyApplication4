package soexample.umeng.com.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bwie.jingdong.R;
import com.bwie.jingdong.base.BaseActivity;
import com.bwie.jingdong.bean.Bean;
import com.bwie.jingdong.bean.LoginBean;
import com.bwie.jingdong.bean.RegisterBean;
import com.bwie.jingdong.myfragment.presenter.MyPresenter;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;

public class MyUserActivity extends BaseActivity<MyPresenter> implements MView {

    @BindView(R.id.img_left)
    ImageView imgLeft;
    @BindView(R.id.simple_myuser)
    SimpleDraweeView simpleMyuser;
    @BindView(R.id.re_myuser)
    RelativeLayout reMyuser;
    @BindView(R.id.txt_umyser)
    TextView txtUmyser;

    private SharedPreferences sp;
    private File tempFile;
    private int uid;
    private String token;

    @Override
    protected MyPresenter getProcenter() {
        return new MyPresenter();
    }

    @Override
    protected int productLayoutId() {
        return R.layout.activity_my_user;
    }

    @Override
    protected void onResume() {
        super.onResume();
        uid = sp.getInt("uid", 0);
        token = sp.getString("token", "");
        if (uid != 0 && !TextUtils.isEmpty(token) && presenter != null) {
            presenter.getUserMangerData(uid, token);
        }
    }

    @Override
    protected void initData() {
        sp = getSharedPreferences("Nickname", MODE_PRIVATE);
        String qq = sp.getString("qq", "");
        if (!TextUtils.isEmpty(qq)) {
            simpleMyuser.setImageURI(Uri.parse(qq));
        }

    }

    @Override
    protected void initListener() {
        super.initListener();
        final String[] items = new String[]{"拍照", "从相册选取"};
        simpleMyuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MyUserActivity.this)
                        .setTitle("上传头像")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        // 相机
                                        getPicFromCamera();
                                        break;
                                    case 1:
                                        // 相册
                                        getPicFromAlbm();
                                        break;
                                }
                            }
                        })
                        .create()
                        .show();
            }
        });

        imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // 相册
    private void getPicFromAlbm() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 2);
    }

    // 相机
    private void getPicFromCamera() {
        //用于保存调用相机拍照后所生成的文件
        tempFile = new File(Environment.getExternalStorageDirectory().getPath(), System.currentTimeMillis() + ".jpg");
        //跳转到调用系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {   //如果在Android7.0以上,使用FileProvider获取Uri
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(MyUserActivity.this, "com.bwie.jingdong", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
            Log.e("dasd", contentUri.toString());
        } else {    //否则使用Uri.fromFile(file)方法获取Uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        }
        startActivityForResult(intent, 1);
    }

    // 裁剪
    private void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    //用相机返回的照片去调用剪裁也需要对Uri进行处理
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Uri contentUri = FileProvider.getUriForFile(MyUserActivity.this, "com.bwie.jingdong", tempFile);
                        cropPhoto(contentUri);
                    } else {
                        cropPhoto(Uri.fromFile(tempFile));
                    }
                }
                break;
            case 0:
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    //在这里获得了剪裁后的Bitmap对象，可以用于上传
                    Bitmap image = bundle.getParcelable("data");

                    //也可以进行一些保存、压缩等操作后上传
                    String path = saveImage("crop", image);
                    if (presenter != null) {
                        presenter.UploadModel(String.valueOf(uid), new File(path));
                    }
                }
                break;
            case 2:
                Uri uri = data.getData();
                cropPhoto(uri);
                break;
        }
    }

    // 压缩
    public String saveImage(String name, Bitmap bmp) {
        File appDir = new File(Environment.getExternalStorageDirectory().getPath());
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = name + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void loginsuccess(LoginBean loginBean) {
        if (loginBean != null) {
            String icon = (String) loginBean.getData().getIcon();
            if (icon != null) {
                Uri uri = Uri.parse(icon.replace("https", "http"));
                Log.i("uri", uri + "");
                simpleMyuser.setImageURI(uri);
            } else {
                simpleMyuser.setImageResource(R.drawable.user);
            }
            txtUmyser.setText(loginBean.getData().getMobile());
        }
    }

    @Override
    public void registersuccess(RegisterBean register) {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void error(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void beansuccess(Bean bean) {
        if (bean != null) {
            if (uid != 0 && !TextUtils.isEmpty(token) && presenter != null) {
                presenter.getUserMangerData(uid, token);
            }
        }
        Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show();
    }
}
