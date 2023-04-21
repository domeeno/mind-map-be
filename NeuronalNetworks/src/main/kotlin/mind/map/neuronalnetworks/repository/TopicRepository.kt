package mind.map.neuronalnetworks.repository

import mind.map.neuronalnetworks.model.Topic
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TopicRepository : ReactiveMongoRepository<Topic, String>
