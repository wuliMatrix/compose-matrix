package love.matrix.matrix.ui.photo

import androidx.compose.*
import androidx.ui.core.*
import androidx.ui.foundation.*
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.input.KeyboardType
import androidx.ui.layout.*
import androidx.ui.layout.ColumnScope.weight
import androidx.ui.res.imageResource
import androidx.ui.text.TextStyle
import androidx.ui.unit.dp
import androidx.ui.unit.sp
import love.matrix.matrix.R
import androidx.ui.input.TextFieldValue
import love.matrix.matrix.ui.composeTemplate.CloseButton


@Composable
fun PhotoScreen() {
    val tweetText = state { TextFieldValue(text = "") }
    Column(modifier = Modifier.fillMaxHeight()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalGravity = Alignment.CenterVertically
        ) {
            CloseButton()
        }
        AvatarWithTextField(tweetText)
    }
}

@Composable
private fun AvatarWithTextField(tweetText: MutableState<TextFieldValue>) {
    Row(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 10.dp)
    ) {
        Image(
            imageResource(R.drawable.profile_image),
            modifier = Modifier
                .preferredSize(34.dp)
                .clip(shape = RoundedCornerShape(17.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.preferredWidth(10.dp))
        TextFieldWithHint(
            modifier = Modifier.fillMaxWidth(),
            value = tweetText.value,
            onValueChange = { textFieldValue -> tweetText.value = textFieldValue },
            hint = "What's happening?"
        )
    }
}



@Composable
private fun TextFieldWithHint(
    value: TextFieldValue,
    modifier: Modifier,
    hint: String,
    onValueChange: (TextFieldValue) -> Unit
) {
    Stack(Modifier.weight(1f)) {
        TextField(
            value = value,
            modifier = modifier,
            onValueChange = onValueChange,
            keyboardType = KeyboardType.Text
        )
        if (value.text.isEmpty()) Text(
            text = hint,
            style = TextStyle(color = Color(0xFF666666), fontSize = 18.sp)
        )
    }
}
