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

package love.matrix.matrix.repository

import androidx.compose.Composable
import androidx.compose.getValue
import androidx.compose.onActive
import androidx.compose.setValue
import androidx.compose.state
/**
 * Model for UiStates that can refresh. The Success state contains whether there's data loading
 * apart from the current data to display on the screen. The error state also returns previously
 * loaded data apart from the error.
 * 可以刷新的UiState的模型。成功状态包含除当前数据外，是否还有其他数据要加载到屏幕上。
 * 错误状态先前也已返回，除了错误之外，还加载了数据。
 *
  */
sealed class RefreshableUiState<out T> {
    data class Success<out T>(val data: T?, val loading: Boolean) : RefreshableUiState<T>()
    data class Error<out T>(val exception: Exception, val previousData: T?) :
        RefreshableUiState<T>()
}

/**
 * Handler that allows getting the current RefreshableUiState and refresh its content.
 * 允许获取当前RefreshableUiState并刷新其内容的处理程序。
 */
data class RefreshableUiStateHandler<out T>(
    val state: RefreshableUiState<T>,
    val refreshAction: () -> Unit
)

/**
 * Refreshable UiState factory that updates its internal state with the
 * [com.example.jetnews.data.Result] of a callback passed as a parameter.
 * 可刷新的UiState工厂，该工厂使用作为参数传递的回调的[com.example.jetnews.data.Result]更新其内部状态。
 *
 * To load asynchronous data, effects are better pattern than using @Model classes since
 * effects are Compose lifecycle aware.
 * 要加载异步数据，效果比使用@Model类更好，因为效果是Compose生命周期感知的。
 */
@Composable
fun <T> refreshableUiStateFrom(
    repositoryCall: RepositoryCall<T>
): RefreshableUiStateHandler<T> {

    var state: RefreshableUiState<T> by state<RefreshableUiState<T>> {
        RefreshableUiState.Success(data = null, loading = true)
    }

    val refresh = {
        state = RefreshableUiState.Success(data = state.currentData, loading = true)
        repositoryCall { result ->
            state = when (result) {
                is Result.Success -> RefreshableUiState.Success(
                    data = result.data, loading = false
                )
                is Result.Error -> RefreshableUiState.Error(
                     //previousData 以前的数据
                    exception = result.exception, previousData = state.currentData
                )
            }
        }
    }

    onActive {
        refresh()
    }

    return RefreshableUiStateHandler(state, refresh)
}

val <T> RefreshableUiState<T>.loading: Boolean
    get() = this is RefreshableUiState.Success && this.loading && this.data == null

val <T> RefreshableUiState<T>.refreshing: Boolean
    get() = this is RefreshableUiState.Success && this.loading && this.data != null

val <T> RefreshableUiState<T>.currentData: T?
    get() = when (this) {
        is RefreshableUiState.Success -> this.data
        is RefreshableUiState.Error -> this.previousData
    }
