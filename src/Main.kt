fun main() {
    val genesisBlock = Block(previousHash = "0", data = "첫 번째")
    val secondBlock = Block(genesisBlock.hash, "두 번째")
    val thirdBlock = Block(secondBlock.hash, "세 번째")

    println(genesisBlock)
    println(secondBlock)
    println(thirdBlock)
}