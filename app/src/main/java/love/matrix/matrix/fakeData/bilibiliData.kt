package love.matrix.matrix.fakeData

import love.matrix.matrix.data.Superhero

//主题
val topics by lazy {
    mapOf(
        "Home" to listOf(
            "record",//头部轮播视频
            "douga",//动画
            "anime",//番剧动态
            "guochuang",//国产原创相关
            "music",//音乐
            "dance",//舞蹈
            "game",//游戏
            "technology",//知识
            "digital",//数码
            "life",//生活
            "kichiku",//鬼畜
            "fashion",//时尚
            "information",//资讯
            "entertainment",//娱乐
            "movie",//电影
            "tv",//电视剧
            "cinephile",//影视
            "documentary"//记录片
        ),
        "Programming" to listOf("Kotlin", "Declarative UIs", "Java"),
        "Technology" to listOf("Pixel", "Google")
    )
}


//用户
val people by lazy {
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

//刊物
val publications by lazy {
    mutableListOf(
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


val superhero by lazy {
    mutableListOf<Superhero>().apply {
        add(
            Superhero(
                "Iron Man",
                43,
                "https://i.annihil.us/u/prod/marvel/i/mg/9/c0/527bb7b37ff55.jpg"
            )
        )
        add(
            Superhero(
                "Hulk",
                38,
                "https://i.annihil.us/u/prod/marvel/i/mg/5/a0/538615ca33ab0.jpg"
            )
        )
        add(
            Superhero(
                "Deadpool",
                25,
                "https://i.annihil.us/u/prod/marvel/i/mg/9/90/5261a86cacb99.jpg"
            )
        )
        add(
            Superhero(
                "Wolverine",
                48,
                "https://i.annihil.us/u/prod/marvel/i/mg/2/60/537bcaef0f6cf.jpg"
            )
        )
        add(
            Superhero(
                "Black Widow",
                40,
                "https://i.annihil.us/u/prod/marvel/i/mg/f/30/50fecad1f395b.jpg"
            )
        )
        add(
            Superhero(
                "Hulk",
                38,
                "https://i.annihil.us/u/prod/marvel/i/mg/d/d0/5269657a74350.jpg"
            )
        )
        add(
            Superhero(
                "Rogue",
                25,
                "https://i.annihil.us/u/prod/marvel/i/mg/9/90/5261a86cacb99.jpg"
            )
        )
        add(
            Superhero(
                "Groot",
                4,
                "https://i.annihil.us/u/prod/marvel/i/mg/3/10/526033c8b474a.jpg"
            )
        )
        add(
            Superhero(
                "Professor X",
                55,
                "https://i.annihil.us/u/prod/marvel/i/mg/3/e0/528d3378de525.jpg"
            )
        )
    }
}