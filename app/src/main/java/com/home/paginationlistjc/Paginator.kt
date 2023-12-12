package com.home.paginationlistjc

interface Paginator<Key, Item> {
    suspend fun loadNext()
    fun reset()
}