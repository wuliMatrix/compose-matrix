package love.matrix.matrix.ui.template

import LocalResourceImageComponent
import androidx.compose.Composable
import androidx.compose.state
import androidx.ui.core.DensityAmbient
import androidx.ui.core.Modifier
import androidx.ui.core.WithConstraints
import androidx.ui.foundation.Box
import androidx.ui.foundation.Text
import androidx.ui.foundation.clickable
import androidx.ui.foundation.lazy.LazyColumnItems
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.material.Card
import androidx.ui.material.TextButton
import androidx.ui.text.TextStyle
import androidx.ui.text.style.TextAlign
import androidx.ui.unit.dp
import androidx.ui.unit.sp
import love.matrix.matrix.R
import love.matrix.matrix.ui.chat.Person
import love.matrix.matrix.ui.chat.colors
import love.matrix.matrix.ui.compose.ViewPagerVerticalScroller


/**
 * 2020/7/3
 *
 * 这个组件实现了数据的多列显示，
 * 更换不同的数据源和内部组件组合，能实现各种各样的列表显示
 * 这是很重要的功能，目前官方还没有这样的现成的组件
 * （之前有Grid组件可以实现这样的功能，后来在dev13之后却删除了）
 */

@Composable
fun LazyColumnItemsScrollableComponent(
    personList: List<Person>
) {
    val cols = 1//在这里控制数据显示的列数
    val personRows = personList.chunked(cols)//chunked 拆分

        ViewPagerVerticalScroller(Modifier.height(1000.dp)) {
            Row {
                TextButton(
                    onClick = { },
                    modifier = Modifier.padding(8.dp),
                    backgroundColor = Color.Red
                ) {
                    Text("Previous", color = Color.White)
                }

                TextButton(
                    onClick = { },
                    modifier = Modifier.padding(8.dp),
                    backgroundColor = Color.Red
                ) {
                    Text("Index: $index", Modifier.padding(8.dp), color = Color.White)
                }
            }

            LazyColumnItems(personRows,modifier = Modifier.height(800.dp)) { row ->

               WithConstraints {

                    Row() {
                        val w =300.dp
                            //with(DensityAmbient.current) { (constraints.maxWidth.toDp().value / cols).dp }
                        row.forEach { person ->
                            val index = personList.indexOf(person)
                            Card(
                                shape = RoundedCornerShape(4.dp),
                                color = colors[index % colors.size],
                                modifier = Modifier
                                    .width(w)
                                    .padding(4.dp)
                                    .clickable(onClick = {
                                    })
                            ) {
                                Text(
                                    person.name, style = TextStyle(
                                        color = Color.Black,
                                        fontSize = 20.sp,
                                        textAlign = TextAlign.Center
                                    ), modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }

                   val aaa = constraints.maxWidth
                   val bbb = constraints.maxHeight
                   val ccc = constraints.minHeight
                   val ddd = constraints.minWidth
                   val fff = constraints.minWidth
               }
           }



    }
}

