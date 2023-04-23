package mind.map.neuronalnetworks.model

import java.time.LocalDateTime

data class SubjectDTO(
    var id: String = "",
    var subjectName: String = "",
    var userId: String? = "",
    var description: String? = "",
    var rootTopic: String = "",
    var tags: List<String> = arrayListOf(),
    var likes: Int = 0,
    var saves: Int = 0,
    var createTimestamp: LocalDateTime,
    var updateTimestamp: LocalDateTime
)

data class CreateSubjectDTO(
    var userId: String? = "",
    var subjectName: String = "",
    var description: String? = "",
    var rootTopic: String = "",
    var tags: List<String> = arrayListOf()
)

data class SubjectSearchDTO(
    val subjectId: String,
    val subjectName: String,
    val description: String?,
    val tags: List<String>
)
