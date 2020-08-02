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

package love.matrix.matrix.repository.information


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import love.matrix.matrix.data.Information
import love.matrix.matrix.data.Superhero
import love.matrix.matrix.fakeData.infoList
import love.matrix.matrix.fakeData.superhero
import love.matrix.matrix.repository.Result

/**
 * Interface to the Posts data layer.与Posts数据层的接口。
 */
class InformationRepository {

    fun getInformation(): LiveData<List<Information>> = MutableLiveData(infoList)

}
