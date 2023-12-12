package com.home.paginationlistjc

import kotlinx.coroutines.delay

class Repository {

    private val remoteDataSource = (1..100).map {
        ListItem(
            title = "Item $it",
            description = "Description $it"
        )
    }

    suspend fun getItems(page: Int, pageSize: Int): Result<List<ListItem>> {
        delay(2000)
        val startingIndex = page * pageSize
        return if (startingIndex + pageSize <= remoteDataSource.size) {
            Result.success(
                remoteDataSource.slice(startingIndex until startingIndex + pageSize)
            )
        } else Result.success(emptyList())
    }
}

/*
val remoteDataSource = (1..100).map {}
menggunakan range operator (..) untuk membuat rentang dari 1 hingga 100 (inklusif).
setiap elemen dalam rentang (angka dari 1 hingga 100) diubah menjadi objek ListItem.

suspend fun getItems(page: Int, pageSize: Int): Result<List<ListItem>> {}
val startingIndex = page * pageSize: Menghitung indeks awal berdasarkan nomor halaman (page) dan ukuran halaman (pageSize).

if (startingIndex + pageSize <= remoteDataSource.size) { ... }
mengecek apakah indeks awal bersama dengan ukuran halaman tidak melebihi ukuran sumber data jarak jauh (remoteDataSource).
Jika tidak melebihi, maka fungsi slice digunakan untuk mengambil sebagian dari remoteDataSource mulai dari startingIndex hingga startingIndex + pageSize. Hasilnya dibungkus dalam objek Result.success.
**/