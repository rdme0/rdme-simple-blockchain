package rcoin

import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey

data class Wallet(
    val address: PublicKey,
    val privateKey: PrivateKey,
    val blockChain: BlockChain
) {
    companion object {
        fun create(blockChain: BlockChain): Wallet {
            val generator = KeyPairGenerator.getInstance("RSA")
            generator.initialize(2048)
            val keyPair = generator.generateKeyPair()

            return Wallet(keyPair.public, keyPair.private, blockChain)
        }
    }

    val balance: Int
        get() {
            return getMyTransactions().sumOf { it.amount }
        }

    fun sendFundsTo(recipientAddress: PublicKey, amountToSend: Int): Transaction {

        if (amountToSend > this.balance) {
            throw IllegalArgumentException("잔액이 부족합니다.")
        }

        val transaction = Transaction.create(
            sender = this.address,
            recipient = recipientAddress,
            amount = amountToSend
        )

        transaction.outputs.add(
            TransactionOutput(
                recipient = recipientAddress,
                amount = amountToSend,
                transactionHash = transaction.hash
            )
        )

        var collectedAmount = 0

        for (myTransaction in getMyTransactions()) {
            collectedAmount += myTransaction.amount
            transaction.inputs.add(myTransaction)


            if (collectedAmount > amountToSend) {
                val change = collectedAmount - amountToSend //거스름 돈
                transaction.outputs.add(
                    TransactionOutput(
                        recipient = this.address,
                        amount = change,
                        transactionHash = transaction.hash
                    )
                )
            }

            if (collectedAmount >= amountToSend) break

        }
        return transaction.sign(privateKey)
    }

    private fun getMyTransactions(): Collection<TransactionOutput> {
        return blockChain.UTXO.filterValues { it.isMine(this.address) }.values
    }
}
