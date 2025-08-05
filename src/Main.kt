import rcoin.*

fun main() {

    val blockChain = BlockChain()
    val wallet1 = Wallet.create(blockChain)
    val wallet2 = Wallet.create(blockChain)

    printBalance(wallet1, wallet2)

    val tx1 =
        Transaction.create(sender = wallet1.address, recipient = wallet1.address, amount = 100)
    tx1.outputs.add(
        TransactionOutput(
            recipient = wallet1.address,
            amount = 100,
            transactionHash = tx1.hash
        )
    )
    tx1.sign(wallet1.privateKey)

    var genesisBlock = Block(previousHash = "0")
    genesisBlock.addTransaction(tx1)
    genesisBlock = blockChain.add(genesisBlock)

    printBalance(wallet1, wallet2)

    val tx2 = wallet1.sendFundsTo(recipientAddress = wallet2.address, amountToSend = 45)
    val secondBlock = blockChain.add(Block(previousHash = genesisBlock.hash).addTransaction(tx2))
    printBalance(wallet1, wallet2)
    println("block chain is valid? -> ${blockChain.isValid()}")

    val evil = Wallet.create(blockChain)

    blockChain.blocks.first().transactions.first().outputs.first().recipient = evil.address

    println("block chain is valid? -> ${blockChain.isValid()}")

}

private fun printBalance(wallet1: Wallet, wallet2: Wallet) {
    println("지갑 1 잔액 -> ${wallet1.balance}")
    println("지갑 2 잔액 -> ${wallet2.balance}")
}