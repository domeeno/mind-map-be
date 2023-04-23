package mind.map.neuronalnetworks.model

fun Subject.toSubjectDTO(): SubjectDTO {
    return SubjectDTO(
        id = id,
        subjectName = subjectName,
        userId = userId,
        description = description,
        rootTopic = rootTopic,
        tags = tags,
        likes = likes,
        saves = saves,
        createTimestamp = createTimestamp,
        updateTimestamp = updateTimestamp
    )
}

fun SubjectDTO.toSubject(): Subject {
    return Subject(
        subjectName = subjectName,
        userId = userId,
        description = description,
        rootTopic = rootTopic,
        tags = tags,
        likes = likes,
        saves = saves,
        createTimestamp = createTimestamp,
        updateTimestamp = updateTimestamp
    )
}

fun CreateSubjectDTO.toSubject(): Subject {
    return Subject(
        userId = userId,
        subjectName = subjectName,
        description = description,
        rootTopic = rootTopic,
        tags = tags
    )
}

fun Subject.toSubjectSearchDTO(): SubjectSearchDTO {
    return SubjectSearchDTO(
        subjectId = id,
        subjectName = subjectName,
        description = description,
        tags = tags
    )
}

fun Topic.toTopicDTO(): TopicDTO {
    return TopicDTO(
        id = id,
        type = type,
        color = color,
        weight = weight,
        tags = tags,
        parentId = parentId,
        userId = userId,
        topicName = topicName
    )
}

fun TopicDTO.toTopic(rootTopic: String): Topic {
    return Topic(
        type = type,
        color = color,
        weight = weight,
        tags = tags,
        parentId = rootTopic,
        userId = userId,
        topicName = topicName
    )
}
