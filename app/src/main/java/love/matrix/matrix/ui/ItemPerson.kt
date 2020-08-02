package com.instagram.clone.ui.home

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.Box
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.Text
import androidx.ui.foundation.shape.corner.CircleShape
import androidx.ui.layout.*
import androidx.ui.material.Card
import androidx.ui.unit.dp
import com.instagram.clone.models.User222
import com.instagram.clone.navigator.Navigator
import com.instagram.clone.navigator.navigateTo
import com.instagram.clone.utils111.Dimens
import com.instagram.clone.utils111.GlideImage
import com.instagram.clone.utils111.themeTypography

@Composable
fun ItemPerson(user: User222) {
    Clickable(onClick = {
        navigateTo(Navigator.DetailPage(user = user))
    }) {
        Card {
            Box(
                padding = Dimens.medium_content,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row {
                    Box(shape = CircleShape, modifier = Modifier.preferredSize(Dimens.circle_image)) {
                        GlideImage(model = user.avatar)
                    }
                    Spacer(modifier = Modifier.preferredSize(width = Dimens.medium_content, height = 0.dp))
                    Column {
                        Text(text = user.first_name, style = themeTypography.h6)
                        Text(text = user.email, style = themeTypography.body1)
                    }
                }
            }
        }
    }
}