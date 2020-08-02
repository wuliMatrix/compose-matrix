package com.instagram.clone.di

import com.google.gson.GsonBuilder
import com.instagram.clone.models.Users222
import com.kuky.demo.wan.android.base.BaseResultData
import com.kuky.demo.wan.android.data.db.HomeArticleDetail
import com.kuky.demo.wan.android.entity.ArticleData
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

var BASE_URL = "https://www.wanandroid.com"

val networkModule = module {
    factory {
        createWebService<NetworkApi>(
            createHttpClient(),
            BASE_URL
        )
    }
}

fun createHttpClient(): OkHttpClient {
    val client = OkHttpClient.Builder()
    return client.build()
}

inline fun <reified T> createWebService(
    okHttpClient: OkHttpClient,
    baseUrl: String): T {

    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .client(okHttpClient)
        .build()
    return retrofit.create(T::class.java)
}

interface NetworkApi {
    @GET("/api/users")
    suspend fun getListUser (@Query("page") page: String) : Users222


    // ===============================>
    // 首页文章
    @GET("/article/list/{page}/json")
    suspend fun homeArticles(
        @Path("page") page: Int
    ): BaseResultData<ArticleData>

    // 置顶文章
    @GET("/article/top/json")
    suspend fun topArticle(
    ): BaseResultData<MutableList<HomeArticleDetail>>


}