package mind.map.neuronalnetworks

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.net.InetAddress

@SpringBootApplication
class NeuronalNetworksApplication

fun main(args: Array<String>) {
    val log: Logger = LoggerFactory.getLogger(NeuronalNetworksApplication::class.java)
    val context = runApplication<NeuronalNetworksApplication>(*args)

    log.info(
        "External: http://{}:{}",
        InetAddress.getLoopbackAddress().hostName,
        context.environment.getProperty("server.port", "8080")
    )
}
