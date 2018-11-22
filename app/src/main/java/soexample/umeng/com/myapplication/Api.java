package soexample.umeng.com.myapplication;

import com.bwie.jingdong.bean.AddCar;
import com.bwie.jingdong.bean.Bean;
import com.bwie.jingdong.bean.CarBean;
import com.bwie.jingdong.bean.HomeBean;
import com.bwie.jingdong.bean.LeftClazz;
import com.bwie.jingdong.bean.LoginBean;
import com.bwie.jingdong.bean.RegisterBean;
import com.bwie.jingdong.bean.RightClazz;
import com.bwie.jingdong.bean.SearchBean;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by 夏威夷丶 on 2018/11/6.
 */

public interface Api {

    // 首页
    @GET("home/getHome")
    Observable<HomeBean> getHomeData(@Query("source") String source);

    // 分类左边
    @GET("product/getCatagory")
    Observable<LeftClazz> getClazzLifeData();

    // 分类右边
    @GET("product/getProductCatagory")
    Observable<RightClazz> getClazzRightData(@Query("cid") int cid);

    // 搜索
    @GET("product/searchProducts")
    Observable<SearchBean> getSearch(@Query("keywords") String keywords, @Query("page") int page, @Query("source") String source);

    // 登录
    @GET("user/login")
    Observable<LoginBean> getLoginData(@Query("mobile") String mobile, @Query("password") String password);

    // 注册
    @GET("user/reg")
    Observable<RegisterBean> getRegisterData(@Query("mobile") String mobile, @Query("password") String password);

    // 获取用户信息
    @GET("user/getUserInfo")
    Observable<LoginBean> getUserManger(@Query("uid") int uid, @Query("token") String token);

    // 添加购物车
    @GET("product/addCart")
    Observable<AddCar> AddCart(@Query("uid") String uid, @Query("pid") String pid, @Query("token") String token);

    // 查询购物车
    @GET("product/getCarts")
    Observable<CarBean> getCarData(@Query("uid") String uid, @Query("token") String token);

    // 删除购物车
    @GET("product/deleteCart")
    Observable<AddCar> DelCart(@Query("uid") String uid, @Query("pid") String pid);

    // 上传头像
    @Multipart
    @POST("file/upload")
    Observable<Bean> upLoad(@Query("uid") String uid, @Part MultipartBody.Part part);
}
