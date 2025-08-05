package rcoin

import rcoin.Extensions.hash
import java.time.Instant

data class Block(
    val previousHash: String,
    val timeStamp: Long = Instant.now().toEpochMilli(), //유닉스 타임스탬프
    val nonce: Long = 0,
    var hash: String = "",
    val transactions: MutableList<Transaction> = mutableListOf()
) {
    init { hash = calculateHash() }

    fun calculateHash(): String = "$previousHash$transactions$timeStamp$nonce".hash()

    fun addTransaction(transaction: Transaction) : Block {
        if (transaction.isSignatureValid())
            transactions.add(transaction)

        return this
    }
}