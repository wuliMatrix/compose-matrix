package com.kuky.demo.wan.android.ui.main

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kuky.demo.wan.android.base.BaseResultData
import com.kuky.demo.wan.android.data.PreferencesHelper
import com.kuky.demo.wan.android.entity.BannerData
import com.kuky.demo.wan.android.entity.WanUserData
import kotlinx.coroutines.flow.flow
import love.matrix.matrix.MainApplication
import retrofit2.Response

/**
 * @author kuky.
 * @description
 */
class MainViewModel(
    private val repository: MainRepository
) : ViewModel() {

    val hasLogin = MutableLiveData<Boolean>()
    val banners = MutableLiveData<List<BannerData>>()

    init {
        hasLogin.value = PreferencesHelper.hasLogin(MainApplication.instance)
        banners.value = repository.getCachedBanners()
    }

    fun getCoinInfo() = flow {
        emit(repository.getCoins())
    }

    fun login(username: String, password: String) = flow {
        emit(repository.login(username, password))
    }

    fun register(username: String, password: String, repass: String) = flow {
        emit(repository.register(username, password, repass))
    }

    fun loginOut() = flow {
        emit(repository.loginOut())
    }

    fun clearUserInfo() {
        PreferencesHelper.saveUserId(MainApplication.instance, 0)
        PreferencesHelper.saveUserName(MainApplication.instance, "")
        PreferencesHelper.saveCookie(MainApplication.instance, "")
    }

    // 存储用户信息
    fun saveUser(info: Response<BaseResultData<WanUserData>>) {
        if (info.body()?.errorCode == 0) {
            val cookies = StringBuilder()

            info.headers()
                .filter { TextUtils.equals(it.first, "Set-Cookie") }
                .forEach { cookies.append(it.second).append(";") }

            val strCookie =
                if (cookies.endsWith(";")) cookies.substring(0, cookies.length - 1)
                else cookies.toString()

            PreferencesHelper.saveCookie(MainApplication.instance, strCookie)
            PreferencesHelper.saveUserId(MainApplication.instance, info.body()?.data?.id ?: 0)
            PreferencesHelper.saveUserName(MainApplication.instance, info.body()?.data?.nickname ?: "")
        }
    }
}