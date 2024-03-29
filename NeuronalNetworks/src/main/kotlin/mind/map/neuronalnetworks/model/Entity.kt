package mind.map.neuronalnetworks.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDateTime
import java.util.UUID

@Document("documents")
data class Document(
    @Id
    var id: String = UUID.randomUUID().toString(),

    var content: String = ""
)

@Document
data class Subject(
    @Id
    var id: String = UUID.randomUUID().toString(),

    @Field(name = "subject_name")
    var subjectName: String = "",

    @Field(name = "user_id")
    var userId: String? = "",

    var description: String? = "",

    @Field(name = "root_topic")
    var rootTopic: String = "",

    var tags: List<String> = arrayListOf(),

    var likes: Int = 0,

    var saves: Int = 0,

    @Field(name = "create_timestamp")
    var createTimestamp: LocalDateTime = LocalDateTime.now(),

    @Field(name = "update_timestamp")
    var updateTimestamp: LocalDateTime = LocalDateTime.now()
)

@Document("topics")
data class Topic(

    /*
       This Document stores Subject's topics.json, in other words this will branch out the subject into multiple topics.json
       They exist attached to a subject.
    */

    @Id
    var id: String = UUID.randomUUID().toString(),

    var type: TopicType = TopicType.TOPIC,

    var color: TopicColors = TopicColors.APP_DEFAULT,

    var weight: TopicWeights = TopicWeights.MEDIUM,

    var tags: List<String> = arrayListOf(),

    @Field(name = "subject_id")
    var subjectId: String,

    @Field(name = "user_id")
    var userId: String? = "",

    @Field(name = "topic_name")
    var topicName: String = "",

    @Field(name = "child_ids")
    var childIds: List<String> = arrayListOf(),

    @Field(name = "parents_id")
    var parentIds: List<String> = arrayListOf(),

    @Field(name = "document_id")
    var documentId: String = UUID.randomUUID().toString(),

    @Field(name = "create_timestamp")
    var createTimestamp: LocalDateTime = LocalDateTime.now(),

    @Field(name = "update_timestamp")
    var updateTimestamp: LocalDateTime = LocalDateTime.now(),

    @Field(name = "child_topics")
    var childTopics: List<Topic>? = arrayListOf()
)

enum class TopicType {
    ROOT, TOPIC, DOCUMENT
}

enum class TopicColors(val color: String) {
    APP_DEFAULT("#0077C2"),
    USER_DEFAULT("#000000"),
    TEAL("#008080"),
    MAROON("#800000"),
    PURPLE("#800080"),
    OLIVE("#808000"),
    NAVY("#000080"),
    GRAY("#808080"),
    SILVER("#C0C0C0")
}

enum class TopicWeights(val weight: Int) {
    XS(0),
    LIGHT(1),
    MEDIUM(2),
    HEAVY(3),
    XL(4),
    ROOT(5)
}
