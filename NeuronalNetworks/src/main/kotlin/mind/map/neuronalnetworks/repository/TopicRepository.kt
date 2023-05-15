package mind.map.neuronalnetworks.repository

import mind.map.neuronalnetworks.model.Topic
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface TopicRepository : ReactiveMongoRepository<Topic, String> {
    fun findAllBySubjectIdOrderByCreateTimestampAsc(subjectId: String): Flux<Topic>
}
