package mind.map.neuronalnetworks.service

import mind.map.neuronalnetworks.model.CreateTopicInput
import mind.map.neuronalnetworks.model.Topic
import mind.map.neuronalnetworks.model.TopicDTO
import mind.map.neuronalnetworks.model.toTopicDTO
import mind.map.neuronalnetworks.repository.TopicRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

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

    override fun getSubjectTopics(subjectId: String): Flux<TopicDTO> {
        return topicRepository.findAllBySubjectIdOrderByCreateTimestampDesc(subjectId).map {
            it.toTopicDTO()
        }
    }

    override fun getTopics(): Flux<TopicDTO> {
        return topicRepository.findAll().map { it.toTopicDTO() }
    }
}
