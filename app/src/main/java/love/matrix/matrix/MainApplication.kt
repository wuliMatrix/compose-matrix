/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package love.matrix.matrix

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.instagram.clone.di.networkModule
import com.kuky.demo.wan.android.di.dataSourceModule
import com.kuky.demo.wan.android.di.girlModule
import com.kuky.demo.wan.android.di.repositoryModule
import com.kuky.demo.wan.android.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level


class MainApplication : Application() {

    // AppContainer instance used by the rest of classes to obtain dependencies
    //其余类使用AppContainer实例获取依赖项
    lateinit var container: AppContainer



    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl(this)

        instance = applicationContext

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MainApplication)
            androidFileProperties()
            modules(
                dataSourceModule, repositoryModule, viewModelModule,networkModule,girlModule
            )
        }

       /* startKoin {
            androidContext(applicationContext)
            modules(
                networkModule,
                com.instagram.clone.di.viewModelModule,
                com.instagram.clone.di.repositoryModule
            )
        }*/


    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: Context
    }
}
