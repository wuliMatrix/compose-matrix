package com.instagram.clone.ui.detail

import androidx.compose.Composable
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.Box
import androidx.ui.foundation.Icon
import androidx.ui.foundation.Text
import androidx.ui.foundation.shape.corner.CircleShape
import androidx.ui.layout.Column
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.preferredHeight
import androidx.ui.layout.preferredSize
import androidx.ui.material.IconButton
import androidx.ui.material.Scaffold
import androidx.ui.material.TopAppBar
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.ArrowBack
import com.instagram.clone.models.User222
import com.instagram.clone.utils111.Dimens
import com.instagram.clone.utils111.GlideImage
import com.instagram.clone.utils111.themeTypography

@Composable
fun DetailPage(user: User222) {
    Scaffold (
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = {
                    }) {
                        Icon(Icons.Filled.ArrowBack)
                    }
                }
            )
        },
        bodyContent = {
        DetailView(user = user)
    })
}


@Composable fun DetailView(user: User222){
    Column(modifier = Modifier.fillMaxWidth(), horizontalGravity = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.preferredHeight(Dimens.profile_image))
        Box(shape = CircleShape, modifier = Modifier.preferredSize(Dimens.profile_image)) {
            GlideImage(model = user.avatar)
        }
        Text("${user.first_name} ${user.last_name}",  style = themeTypography.h6)
        Text(user.email, style = themeTypography.body1)
    }
}