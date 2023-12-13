package com.home.paginationlistjc

interface Paginator<Key, Item> {
    suspend fun loadNextItems()
    fun reset()
}