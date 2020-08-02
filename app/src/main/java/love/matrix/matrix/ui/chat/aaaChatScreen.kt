package love.matrix.matrix.ui.chat


import androidx.compose.Composable
import androidx.compose.MutableState
import androidx.compose.state
import androidx.ui.core.Modifier
import androidx.ui.foundation.*
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.layout.ColumnScope.weight
import androidx.ui.material.*
import com.kuky.demo.wan.android.ui.home.HomeArticleViewModel
import love.matrix.matrix.ui.composeTemplate.BottomBarHeightSpacer
import love.matrix.matrix.ui.composeTemplate.StateBarHeightSpacer


private enum class ChatSections(val title: String) {
    FriendsCircle("好友圈"),
    Information("消息池"),
    AddressBook("通讯录"),
}

@Composable
 fun ChatScreen(viewModel: HomeArticleViewModel) {

    val isOpen = state { true }

    Column(modifier = Modifier.fillMaxSize()) {
        StateBarHeightSpacer()
        Scaffold(
            // scaffoldState = scaffoldState,
            bodyContent = {innerPadding ->
                val modifier = Modifier.padding(innerPadding)//让内容显示在bottomBar之上
                Box(modifier = modifier.fillMaxSize()) {
                    val (currentSection, updateSection) = state { ChatSections.Information }
                    ChatBodyContent(viewModel, isOpen, currentSection, updateSection)
                }
            },
            //添加一个空白体，让内容不在bottomBar内显示，需要innerPadding配合
            bottomBar = {BottomBarHeightSpacer()}
        )
    }

}

@Composable
private fun ChatBodyContent(
    viewModel: HomeArticleViewModel,
    isOpen: MutableState<Boolean>,
    currentSection: ChatSections,
    updateSection: (ChatSections) -> Unit
) {
    val sectionTitles = ChatSections.values().map { it.title }
    Column {
        if (isOpen.value) {
            TabRow(
                items = sectionTitles,
                selectedIndex = currentSection.ordinal,// 返回此枚举常量的序数
                scrollable = false
            ) { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = currentSection.ordinal == index,
                    onSelected = {
                        updateSection(ChatSections.values()[index])//更新选项卡状态
                    })
            }
        }
        Box(modifier = Modifier.weight(1f)) {//weight 权重
            when (currentSection) {
                ChatSections.FriendsCircle -> {
                    FriendsCircleView()
                }
                ChatSections.Information -> {
                    In01Informations(viewModel)
                }
                ChatSections.AddressBook -> {
                    Chat69Contacts(
                        isOpen
                    )
                }
            }
        }

    }
}


@Composable
fun FriendsCircleView() {
    Surface(color = Color(0xFFffd7d7.toInt()), modifier = Modifier.weight(1f)) {
        Box(
            modifier = Modifier.fillMaxSize(),
            gravity = ContentGravity.Center,
            children = {
                Text(text = "好友圈")
            })
    }
}

@Composable
fun Chat69Contacts(isOpen: MutableState<Boolean>) {//联系人

    Chat69Contacts.Content(isOpen, Chat69Contacts.Routing.ContactList)
}
