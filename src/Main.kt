fun main() {

    var genesisBlock = Block(previousHash = "0", data = "첫 번째")
    genesisBlock = BlockChain().add(genesisBlock)
    val secondBlock = BlockChain().add(Block(genesisBlock.hash, "두 번째"))
}