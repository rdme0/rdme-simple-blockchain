class BlockChain {
    private var blocks: MutableList<Block> = mutableListOf()

    fun add(block: Block) : Block {
        blocks.add(block)
        return block
    }
}