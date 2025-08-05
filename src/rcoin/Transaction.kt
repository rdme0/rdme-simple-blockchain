package rcoin

import rcoin.Extensions.encodeToString
import rcoin.Extensions.hash
import rcoin.Extensions.sign
import rcoin.Extensions.verifySignature
import java.security.PrivateKey
import java.security.PublicKey


data class TransactionOutput(
    var recipient: PublicKey,
    val amount: Int,
    val transactionHash: String,
    var hash: String = ""
) {
    init {
        hash = "${recipient.encodeToString()}$amount$transactionHash".hash()
    }

    fun isMine(me: PublicKey): Boolean {
        return recipient == me
    }
}

data class Transaction(
    val sender: PublicKey,
    val recipient: PublicKey, // -> eth scan 같은 곳에 첫 화면 요약에 쓰이는 용도
    val amount: Int, // -> eth scan 같은 곳에 첫 화면 요약에 쓰이는 용도
    var hash: String = "",
    val inputs: MutableList<TransactionOutput> = mutableListOf(), //이게 진짜임
    val outputs: MutableList<TransactionOutput> = mutableListOf() //이게 진짜임
) {

    private var signature: ByteArray = ByteArray(0)
    private var isSigned: Boolean = false

    init {
        hash = "${sender.encodeToString()}${recipient.encodeToString()}$amount$salt".hash()
    }

    companion object {
        fun create(sender: PublicKey, recipient: PublicKey, amount: Int): Transaction {
            return Transaction(sender, recipient, amount)
        }

        var salt: Long = 0
            get() {
                field += 1
                return field
            }
    }

    fun sign(privateKey: PrivateKey): Transaction {
        signature =
            "${sender.encodeToString()}${recipient.encodeToString()}$amount".sign(privateKey)
        isSigned = true
        return this
    }

    fun isSignatureValid(): Boolean {

        if (!isSigned || signature.isEmpty())
            return true

        return "${sender.encodeToString()}${recipient.encodeToString()}$amount".verifySignature(
            sender,
            signature
        )
    }

}
