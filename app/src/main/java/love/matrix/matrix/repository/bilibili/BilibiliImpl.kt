package love.matrix.matrix.repository.bilibili

import love.matrix.matrix.data.Superhero
import love.matrix.matrix.fakeData.people
import love.matrix.matrix.fakeData.publications
import love.matrix.matrix.fakeData.superhero
import love.matrix.matrix.fakeData.topics
import love.matrix.matrix.repository.Result

class BilibiliImpl : BilibiliRepository{

    override fun getTopics(callback: (Result<Map<String, List<String>>>) -> Unit) {
        callback(Result.Success(topics))
    }

    override fun getSuperhero(callback: (Result<List<Superhero>>) -> Unit) {
        callback(Result.Success(superhero))
    }

    override fun getPeople(callback: (Result<List<String>>) -> Unit) {
        callback(Result.Success(people))
    }

    override fun getPublications(callback: (Result<List<String>>) -> Unit) {
        callback(Result.Success(publications))
    }


}