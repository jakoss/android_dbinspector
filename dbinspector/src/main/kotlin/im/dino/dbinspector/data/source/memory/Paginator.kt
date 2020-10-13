package im.dino.dbinspector.data.source.memory

internal interface Paginator {

    fun setPageCount(rowCount: Int, pageSize: Int)

    fun nextPage(currentPage: Int?): Int?

    fun boundary(page: Int?, pageSize: Int, count: Int): Boundary

    data class Boundary(
        val startRow: Int,
        val endRow: Int
    )
}
