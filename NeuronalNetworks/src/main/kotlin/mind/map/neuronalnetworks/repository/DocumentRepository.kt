package mind.map.neuronalnetworks.repository

import mind.map.neuronalnetworks.model.Document
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface DocumentRepository : ReactiveMongoRepository<Document, String>
