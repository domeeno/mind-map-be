package mind.map.neuronalnetworks.service

import mind.map.neuronalnetworks.model.CreateTopicInput
import mind.map.neuronalnetworks.model.Topic
import mind.map.neuronalnetworks.model.TopicDTO
import mind.map.neuronalnetworks.model.toTopicDTO
import mind.map.neuronalnetworks.repository.TopicRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Service
class TopicServiceImpl(
    private val topicRepository: TopicRepository
) : TopicService {

    override fun createTopic(createTopicInput: CreateTopicInput): Flux<TopicDTO> {
        val topic = Topic(
            subjectId = createTopicInput.subjectId,
            parentIds = listOf(createTopicInput.parentId),
            topicName = createTopicInput.topicName
        )

        val newTopic = topicRepository.save(topic)

        val updateParentTopic = newTopic
            .flatMapMany {
                topicRepository.findById(createTopicInput.parentId)
                    .flatMap { parentTopic ->
                        parentTopic.childIds = parentTopic.childIds.plus(it.id)
                        topicRepository.save(parentTopic)
                    }
            }

        return newTopic.concatWith(updateParentTopic).map { it.toTopicDTO() }
    }

    override fun updateTopic(topicId: String, topicDTO: TopicDTO): Mono<TopicDTO> {
        return topicRepository.findById(topicId)
            .flatMap {
                it.topicName = topicDTO.topicName
                it.color = topicDTO.color
                it.weight = topicDTO.weight
                it.parentIds = topicDTO.parentIds
                it.childIds = topicDTO.childIds
                it.tags = topicDTO.tags
                it.documentId = topicDTO.documentId
                it.updateTimestamp = LocalDateTime.now()
                it.type = topicDTO.type
                topicRepository.save(it)
            }.map { it.toTopicDTO() }
    }

    override fun getSubjectTopics(subjectId: String): Flux<TopicDTO> {
        return topicRepository.findAllBySubjectIdOrderByCreateTimestampAsc(subjectId).map {
            it.toTopicDTO()
        }
    }

    override fun getTopics(): Flux<TopicDTO> {
        return topicRepository.findAll().map { it.toTopicDTO() }
    }
}
