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

package love.matrix.matrix.repository.interests.impl

import love.matrix.matrix.repository.Result
import love.matrix.matrix.repository.interests.InterestsRepository


/**
 * Implementation of InterestRepository that returns a hardcoded list of
 * topics, people and publications synchronously.
 * 返回一个硬编码列表的InterestRepository的实现主题，人员和出版物同步。
 */
class FakeInterestsRepository : InterestsRepository {

    private val topics by lazy {//topics 主题
        mapOf(
            "Android" to listOf("Jetpack Compose", "Kotlin", "Jetpack"),
            "Programming" to listOf("Kotlin", "Declarative UIs", "Java"),
            "Technology" to listOf("Pixel", "Google")
        )
    }

    private val people by lazy {
        listOf(
            "Kobalt Toral",
            "K'Kola Uvarek",
            "Kris Vriloc",
            "Grala Valdyr",
            "Kruel Valaxar",
            "L'Elij Venonn",
            "Kraag Solazarn",
            "Tava Targesh",
            "Kemarrin Muuda"
        )
    }

    private val publications by lazy {//publications 刊物
        listOf(
            "Kotlin Vibe",
            "Compose Mix",
            "Compose Breakdown",
            "Android Pursue",
            "Kotlin Watchman",
            "Jetpack Ark",
            "Composeshack",
            "Jetpack Point",
            "Compose Tribune"
        )
    }

    override fun getTopics(callback: (Result<Map<String, List<String>>>) -> Unit) {
        callback(Result.Success(topics))
    }

    override fun getPeople(callback: (Result<List<String>>) -> Unit) {
        callback(Result.Success(people))
    }

    override fun getPublications(callback: (Result<List<String>>) -> Unit) {
        callback(Result.Success(publications))
    }
}
