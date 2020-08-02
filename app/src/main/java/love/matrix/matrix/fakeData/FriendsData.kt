package love.matrix.matrix.fakeData

import love.matrix.matrix.R
import love.matrix.matrix.data.Friends
import love.matrix.matrix.data.Information
import love.matrix.matrix.data.User


// Users
val sudorizwan = User(
        name = "Ahmed Rizwan",
        username = "sudo_rizwan",
        avatar = R.drawable.a_profile_image,
        banner = R.drawable.a_profile_banner,
        bio = "Android Engineer @Snappymob",
        following = 393,
        followers = 339,
        verified = false,
        imageUrl = ""
)

val androiddevs = User(
        name = "Android Developers",
        username = "AndroidDev",
        avatar = R.drawable.a_dev_profile_image,
        banner = R.drawable.a_dev_profile_banner,
        bio = "News and announcements for developers from the Android team",
        following = 284,
        followers = 1000,
        verified = true,
        imageUrl = ""
)


val friends = mutableListOf(
        Friends(
                sudorizwan,
                "This is a test friend!",
                null,
                495,
                false,
                193,
                false,
                2,
                1587345183868
        ),
        Friends(
                androiddevs,
                "Kickstart your Kotlin training!\n" +
                        "\n" +
                        "If you're a #Kotlin newbie, start with our Kotlin Bootcamp for programmers and Android Kotlin Fundamentals courses to learn the basics.",
                R.drawable.a_dev_frient_image,
                495,
                true,
                193,
                true,
                2,
                1585852320000
        ),
        Friends(
                sudorizwan,
                "Another test friend, but with an image!",
                R.drawable.a_frient_image,
                495,
                false,
                193,
                false,
                2,
                1587345183868
        ),
        Friends(
                sudorizwan,
                "Test friend 2",
                null,
                495,
                false,
                193,
                false,
                2,
                1585427520000
        ),
        Friends(
                sudorizwan,
                "Test friend 3",
                null,
                495,
                false,
                193,
                false,
                2,
                1585427520000
        ),
        Friends(
                androiddevs,
                "Android Devs test friend 1",
                null,
                495,
                false,
                193,
                true,
                2,
                1585852320000
        ),
        Friends(
                androiddevs,
                "Android Devs test friend 2",
                null,
                495,
                false,
                193,
                true,
                2,
                1585852320000
        )
)




