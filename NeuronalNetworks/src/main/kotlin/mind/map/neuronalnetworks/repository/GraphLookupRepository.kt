package mind.map.neuronalnetworks.repository

import mind.map.neuronalnetworks.model.Topic
import org.slf4j.LoggerFactory
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
class GraphLookupRepository(
    private val reactiveMongoTemplate: ReactiveMongoTemplate
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun getBranch(rootTopicId: String): Flux<Topic> {
        val aggregation = Aggregation.newAggregation(
            Aggregation.match(Criteria.where("_id").`is`(rootTopicId)),
            Aggregation.graphLookup("topics")
                .startWith("_id")
                .connectFrom("child_ids")
                .connectTo("_id")
                .`as`("child_topics")
        )

        return reactiveMongoTemplate.aggregate(aggregation, "topics", Topic::class.java)
            .onErrorResume {
                log.error("Error while getting branch for topicId: {}", rootTopicId, it)
                Flux.empty()
            }
    }

    fun findByArrayFieldContaining(searchString: String): Flux<Topic> {
        val query = Query(Criteria.where("child_ids").`in`(searchString))
        return reactiveMongoTemplate.find(query, Topic::class.java)
    }
}
