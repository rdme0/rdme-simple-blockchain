class BlockChain {
    companion object {
        private const val DIFFICULTY = 5
        private val VALID_PREFIX = "a".repeat(DIFFICULTY)
    }

    private var blocks: MutableList<Block> = mutableListOf()

    fun add(block: Block): Block {
        val minedBlock = if (isMined(block)) block else mine(block)
        blocks.add(minedBlock)
        return minedBlock
    }

    private fun isMined(block: Block): Boolean {
        return block.hash.startsWith(VALID_PREFIX)
    }

    private fun mine(block: Block): Block {
        println("채굴 중 -> $block")

        var minedBlock = block.copy()

        while (!isMined(minedBlock)) {
            minedBlock = minedBlock.copy(nonce = minedBlock.nonce + 1)
        }

        println("채굴 완료 -> $minedBlock")

        return minedBlock
    }

    fun isValid(): Boolean {
        when {
            blocks.isEmpty() -> return true
            blocks.size == 1 -> return blocks.first().hash == blocks.first().calculateHash()

            else -> {
                for (i in 1 until blocks.size) {
                    val previousBlock = blocks[i - 1]
                    val currentBlock = blocks[i]

                    when {
                        currentBlock.hash != currentBlock.calculateHash() -> return false
                        currentBlock.previousHash != previousBlock.calculateHash() -> return false
                        !(isMined(previousBlock) && isMined(currentBlock)) -> return false
                    }
                }
                return true
            }
        }
    }

}