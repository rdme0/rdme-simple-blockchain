import Security.hash
import java.time.Instant

data class Block(
    val previousHash: String,
    val data: String,
    val timeStamp: Long = Instant.now().toEpochMilli(), //유닉스 타임스탬프
    var hash: String = ""
) {

    init { hash = toHash() }

    fun toHash(): String = "$previousHash$data$timeStamp".hash()
}