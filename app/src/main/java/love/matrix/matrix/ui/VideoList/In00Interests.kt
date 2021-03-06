package love.matrix.matrix.ui.VideoList

import androidx.compose.Composable
import androidx.compose.state
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.core.clip
import androidx.ui.foundation.*
import androidx.ui.foundation.selection.toggleable
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.layout.Column
import androidx.ui.layout.Row
import androidx.ui.layout.padding
import androidx.ui.layout.preferredSize
import androidx.ui.material.*
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.EmojiNature
import androidx.ui.res.imageResource
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import love.matrix.matrix.R
import love.matrix.matrix.repository.UiState
import love.matrix.matrix.repository.interests.InterestsRepository
import love.matrix.matrix.repository.interests.impl.FakeInterestsRepository
import love.matrix.matrix.repository.previewDataFrom
import love.matrix.matrix.repository.uiStateFrom
import love.matrix.matrix.ui.common.JetnewsStatus
import love.matrix.matrix.ui.common.ThemedPreview

private enum class Sections(val title: String) {
    Topics("Topics"),
    People("People"),
    Publications("Publications")
}



@Composable
fun Interests(
    interestsRepository: InterestsRepository,
    openDrawer: () -> Unit
){

    val (currentSection, updateSection) = state { Sections.Topics  }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Interest") },
                navigationIcon = {
                    IconButton(onClick = openDrawer) {
                        Icon(asset = Icons.Filled.EmojiNature)
                    }
                }
            )
        },
        bodyContent = {
            InterestsScreenBody(currentSection, updateSection, interestsRepository)
        }
    )

}


@Composable
private fun InterestsScreenBody(
    currentSection: Sections,
    updateSection: (Sections) -> Unit,
    interestsRepository: InterestsRepository
) {
    val sectionTitles = Sections.values().map { it.title }

    Column {
        TabRow(
            items = sectionTitles, selectedIndex = currentSection.ordinal
        ) { index, title ->
            Tab(
                text = { Text(title) },
                selected = currentSection.ordinal == index,
                onSelected = {
                    updateSection(Sections.values()[index])
                }
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            when (currentSection) {
                Sections.Topics -> {
                    val topicsState = uiStateFrom(interestsRepository::getTopics)
                    if (topicsState is UiState.Success) {
                        TopicsTab(topicsState.data)
                    }
                }
                Sections.People -> {
                    val peopleState = uiStateFrom(interestsRepository::getPeople)
                    if (peopleState is UiState.Success) {
                        PeopleTab(peopleState.data)
                    }
                }
                Sections.Publications -> {
                    val publicationsState = uiStateFrom(interestsRepository::getPublications)
                    if (publicationsState is UiState.Success) {
                        PublicationsTab(publicationsState.data)
                    }
                }
            }
        }
    }
}

@Composable
private fun TopicsTab(topics: Map<String, List<String>>) {
    TabWithSections(tabName = Sections.Topics.title, sections = topics)
}

@Composable
private fun PeopleTab(people: List<String>) {
    TabWithTopics(tabName = Sections.People.title, topics = people)
}

@Composable
private fun PublicationsTab(publications: List<String>) {
    TabWithTopics(tabName = Sections.Publications.title, topics = publications)
}

@Composable
private fun TabWithTopics(tabName: String, topics: List<String>) {
    VerticalScroller(modifier = Modifier.padding(top = 16.dp)) {
        topics.forEach { topic ->
            TopicItem(
                getTopicKey(
                    tabName,
                    "- ",
                    topic
                ),
                topic
            )
            TopicDivider()
        }
    }
}

@Composable
private fun TabWithSections(
    tabName: String,
    sections: Map<String, List<String>>
) {
    VerticalScroller {
        sections.forEach { (section, topics) ->
            Text(
                text = section,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.subtitle1
            )
            topics.forEach { topic ->
                TopicItem(
                    getTopicKey(
                        tabName,
                        section,
                        topic
                    ),
                    topic
                )
                TopicDivider()
            }
        }
    }
}

@Composable
private fun TopicItem(topicKey: String, itemTitle: String) {
    val image = imageResource(R.drawable.placeholder_1_1)
    val selected = isTopicSelected(topicKey)
    val onSelected = { it: Boolean ->
        selectTopic(topicKey, it)
    }
    Row(
        modifier = Modifier
            .toggleable(
                value = selected,
                onValueChange = onSelected
            )
            .padding(horizontal = 16.dp)
    ) {
        Image(
            image,
            Modifier
                .gravity(Alignment.CenterVertically)
                .preferredSize(56.dp, 56.dp)
                .clip(RoundedCornerShape(4.dp))
        )
        Text(
            text = itemTitle,
            modifier = Modifier
                .weight(1f)
                .gravity(Alignment.CenterVertically)
                .padding(16.dp),
            style = MaterialTheme.typography.subtitle1
        )
        SelectTopicButton(
            modifier = Modifier.gravity(Alignment.CenterVertically),
            selected = selected
        )
    }
}

@Composable
private fun TopicDivider() {
    Divider(
        modifier = Modifier.padding(start = 72.dp, top = 8.dp, bottom = 8.dp),
        color = MaterialTheme.colors.surface.copy(alpha = 0.08f)
    )
}

private fun getTopicKey(tab: String, group: String, topic: String) = "$tab-$group-$topic"

private fun isTopicSelected(key: String) = JetnewsStatus.selectedTopics.contains(key)

private fun selectTopic(key: String, select: Boolean) {
    if (select) {
        JetnewsStatus.selectedTopics.add(key)
    } else {
        JetnewsStatus.selectedTopics.remove(key)
    }
}


@Preview("Interests screen people tab")
@Composable
fun PreviewPeopleTab() {
    ThemedPreview {
        PeopleTab(loadFakePeople())
    }
}

@Preview("Interests screen people tab dark theme")
@Composable
fun PreviewPeopleTabDark() {
    ThemedPreview(darkTheme = true) {
        PeopleTab(loadFakePeople())
    }
}

@Composable
private fun loadFakePeople(): List<String> {
    return previewDataFrom(FakeInterestsRepository()::getPeople)
}

@Preview("Interests screen publications tab")
@Composable
fun PreviewPublicationsTab() {
    ThemedPreview {
        PublicationsTab(loadFakePublications())
    }
}

@Preview("Interests screen publications tab dark theme")
@Composable
fun PreviewPublicationsTabDark() {
    ThemedPreview(darkTheme = true) {
        PublicationsTab(loadFakePublications())
    }
}

@Composable
private fun loadFakePublications(): List<String> {
    return previewDataFrom(FakeInterestsRepository()::getPublications)
}

@Preview("Interests screen tab with topics")
@Composable
fun PreviewTabWithTopics() {
    ThemedPreview {
        TabWithTopics(tabName = "preview", topics = listOf("Hello", "Compose"))
    }
}

@Preview("Interests screen tab with topics dark theme")
@Composable
fun PreviewTabWithTopicsDark() {
    ThemedPreview(darkTheme = true) {
        TabWithTopics(tabName = "preview", topics = listOf("Hello", "Compose"))
    }
}
