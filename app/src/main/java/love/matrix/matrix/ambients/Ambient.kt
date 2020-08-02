package love.matrix.matrix.ambients

import androidx.compose.ambientOf
import love.matrix.matrix.repository.information.InformationRepository

//ambientOf 创建环境密钥,实现协程之间数据共享
val informationRepository = ambientOf<InformationRepository> { error("Information Repository not found") }



