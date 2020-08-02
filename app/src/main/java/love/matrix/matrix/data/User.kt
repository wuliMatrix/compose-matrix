package love.matrix.matrix.data

data class User(
    val id: Int = 1,
    val imageUrl: String,
    val name: String,
    val username: String,
    val avatar: Int,
    val banner: Int,
    val bio: String,
    val following: Int,
    val followers: Int,
    val verified: Boolean
)


data class Users(
        val id: Int = 1,
        val imageUrl: String,
        val name: String
    )
