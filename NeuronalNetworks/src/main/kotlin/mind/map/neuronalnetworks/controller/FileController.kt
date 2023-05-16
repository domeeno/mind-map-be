package mind.map.neuronalnetworks.controller

import mind.map.neuronalnetworks.model.Document
import mind.map.neuronalnetworks.model.DocumentDTO
import mind.map.neuronalnetworks.repository.DocumentRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("api/file")
class FileController(
    private val documentRepository: DocumentRepository
) {

    @PostMapping("{id}")
    fun uploadFile(@PathVariable id: String, @RequestBody dto: DocumentDTO): Mono<Document> {
        return documentRepository.save(Document(id, dto.content))
    }

    @GetMapping("{id}")
    fun getFile(@PathVariable id: String): Mono<Document> {
        return documentRepository.findById(id)
    }
}
