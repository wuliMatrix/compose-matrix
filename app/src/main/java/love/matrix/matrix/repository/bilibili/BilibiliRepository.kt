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

package love.matrix.matrix.repository.bilibili

import love.matrix.matrix.data.Superhero

import love.matrix.matrix.repository.Result


/**
 * Interface to the Interests data layer. 与兴趣数据层的接口。
 */
interface BilibiliRepository {

    /**
     * Get relevant topics to the user.向用户获取相关主题。
     */
    fun getTopics(callback: (Result<Map<String, List<String>>>) -> Unit)

    /**
     * Get list of people.获取人员列表
     */
    fun getPeople(callback: (Result<List<String>>) -> Unit)


    fun getSuperhero(callback: (Result<List<Superhero>>) -> Unit)

    /**
     * Get list of publications.获取出版物列表
     */
    fun getPublications(callback: (Result<List<String>>) -> Unit)


}
