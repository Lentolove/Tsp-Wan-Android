package com.android.blogandroid.model.api

import com.android.blogandroid.model.bean.*
import retrofit2.http.*

/**
 *     author : shengping.tian
 *     time   : 2020/12/12
 *     desc   : retrofit 接口
 *     version: 1.0
 */
interface ApiService {

    companion object {
        const val BASE_URL = "https://www.wanandroid.com"
    }

    @GET("/article/top/json")
    suspend fun getTopArticleList(): ApiResult<List<Article>>

    @GET("/article/list/{page}/json")
    suspend fun getArticleList(@Path("page") page: Int): ApiResult<Pagination<Article>>

    //获取projectList数据
    @GET("/article/listproject/{page}/json")
    suspend fun getProjectList(@Path("page") page: Int): ApiResult<Pagination<Article>>

    //广场界面数据
    @GET("/user_article/list/{page}/json")
    suspend fun getUserArticleList(@Path("page") page: Int): ApiResult<Pagination<Article>>

    //项目界面数据 两个接口
    @GET("project/tree/json")
    suspend fun getProjectCategories(): ApiResult<MutableList<Category>>

    //根据Category id 获取更过数据
    @GET("project/list/{page}/json")
    suspend fun getProjectListById(@Path("page") page: Int, @Query("cid") cid: Int): ApiResult<Pagination<Article>>

    //公众号界面数据
    @GET("wxarticle/chapters/json")
    suspend fun getWeChatCategory(): ApiResult<MutableList<Category>>

    @GET("wxarticle/list/{id}/{page}/json")
    suspend fun getWeChatArticleList(
            @Path("page") page: Int,
            @Path("id") id: Int
    ): ApiResult<Pagination<Article>>

    //体系页面数据
    @GET("tree/json")
    suspend fun getArticleCategories(): ApiResult<MutableList<Category>>

    @GET("article/list/{page}/json")
    suspend fun getArticleListByCid(
            @Path("page") page: Int,
            @Query("cid") cid: Int
    ): ApiResult<Pagination<Article>>

    //发现界面数据
    @GET("banner/json")
    suspend fun getBanners(): ApiResult<List<Banner>>

    @GET("hotkey/json")
    suspend fun getHotWords(): ApiResult<List<HotWord>>

    @GET("friend/json")
    suspend fun getFrequentlyWebsites(): ApiResult<List<Frequently>>

    //导航界面数据
    @GET("navi/json")
    suspend fun getNavigation(): ApiResult<List<Navigation>>

    //搜索接口
    @FormUrlEncoded
    @POST("article/query/{page}/json")
    suspend fun search(
            @Field("k") keywords: String,
            @Path("page") page: Int
    ): ApiResult<Pagination<Article>>

    /**
     * 注册接口 post
     */
    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(
            @Field("username") username: String,
            @Field("password") password: String,
            @Field("repassword") rePassword: String
    ): ApiResult<UserInfo>

    /**
     * 登录接口 post
     */
    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
            @Field("username") username: String,
            @Field("password") password: String
    ): ApiResult<UserInfo>

    /**
     * 收藏和取消收藏
     */
    @POST("lg/collect/{id}/json")
    suspend fun collect(@Path("id") id: Long): ApiResult<Any>

    @POST("lg/uncollect_originId/{id}/json")
    suspend fun uncollect(@Path("id") id: Long): ApiResult<Any>

    /**
     * 登录后获取收藏的列表
     */
    @GET("lg/collect/list/{page}/json")
    suspend fun getCollectionList(@Path("page") page: Int): ApiResult<Pagination<Article>>

}