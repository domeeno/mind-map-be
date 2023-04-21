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

fun Subject.toSubjectSearchDTO(): SubjectSearchDTO {
    return SubjectSearchDTO(
        subjectId = id,
        subjectName = subjectName,
        description = description,
        tags = tags
    )
}
