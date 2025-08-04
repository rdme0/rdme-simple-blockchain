import Security.hash
import java.time.Instant

data class Block(
    val previousHash: String,
    val data: String,
    val timeStamp: Long = Instant.now().toEpochMilli(), //유닉스 타임스탬프
    val nonce: Long = 0,
    var hash: String = ""
) {

    init { hash = calculateHash() }

    fun calculateHash(): String = "$previousHash$data$timeStamp$nonce".hash()
}