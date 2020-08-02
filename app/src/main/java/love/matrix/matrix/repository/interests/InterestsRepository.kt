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

package love.matrix.matrix.repository.interests

import androidx.compose.ambientOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import love.matrix.matrix.fakeData.people
import love.matrix.matrix.fakeData.publications
import love.matrix.matrix.fakeData.topics
import love.matrix.matrix.repository.Result

/**
 * Interface to the Interests data layer.
 */
interface InterestsRepository {

    /**
     * Get relevant topics to the user.
     */
    fun getTopics(callback: (Result<Map<String, List<String>>>) -> Unit)

    /**
     * Get list of people.
     */
    fun getPeople(callback: (Result<List<String>>) -> Unit)

    /**
     * Get list of publications.
     */
    fun getPublications(callback: (Result<List<String>>) -> Unit)
}


//ambientOf 创建环境密钥,实现协程之间数据共享
val fakerRepository = ambientOf<FakerRepository> { error("Faker Repository not found") }

class FakerRepository {

    fun getTopics(): LiveData<Map<String, List<String>>> = MutableLiveData(topics)

    fun getPeople(): LiveData<List<String>> = MutableLiveData(people)

    fun getPublications(): LiveData<List<String>> = MutableLiveData(publications)


}