package $package$

import akka.Done
import akka.actor.ActorSystem
import akka.kafka.scaladsl._
import akka.kafka._
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization._

import scala.concurrent._
import scala.concurrent.duration.Duration

object $className$ extends App {

  private val config      = ConfigFactory.load()
  private val inputTopic  = config.getString("input-topic")
  private val outputTopic = config.getString("output-topic")

  private implicit val actorSystem: ActorSystem =
    ActorSystem("$project_name$-system", config)

  try {
    val producerSettings = ProducerSettings(
      actorSystem,
      new ByteArraySerializer,
      new StringSerializer
    )

    val consumerSettings = ConsumerSettings(
      actorSystem,
      new ByteArrayDeserializer,
      new StringDeserializer
    )

    implicit val mat: ActorMaterializer = ActorMaterializer()

    // Using a committable Source and a committable Sink, we can resume from
    // where we left off if our process crashes.
    val completion: Future[Done] =
      Consumer
        .committableSource(consumerSettings, Subscriptions.topics(inputTopic))
        .map { msg =>
          // Basically copy from one topic to another.
          // Note input timestamp may be -1 but output timestamp cannot be -1!
          val newTimestamp: Option[java.lang.Long] = {
            if (msg.record.timestamp() < 0) None
            else Some(msg.record.timestamp())
          }
          ProducerMessage.Message(
            new ProducerRecord(
              outputTopic,
              null, // scalastyle:ignore
              newTimestamp.orNull,
              msg.record.key,
              msg.record.value
            ),
            msg.committableOffset
          )
        }
        .runWith(Producer.commitableSink(producerSettings))

    Await.result(completion, Duration.Inf)
  } finally {
    Await.result(actorSystem.terminate(), Duration.Inf)
  }

}
