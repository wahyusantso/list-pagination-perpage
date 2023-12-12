package com.home.paginationlistjc

class DefaultPaginator<Key, Item>(
    private val initialKey: Key,
    private inline val onLoadUpdate: (Boolean) -> Unit,
    private inline val onRequest: suspend (nextKey: Key) -> Result<List<Item>>,
    private inline val getNextKey: suspend (List<Item>) -> Key,
    private inline val onError: suspend (Throwable?) -> Unit,
    private inline val onSuccess: suspend (item: List<Item>, newKey: Key) -> Unit
): Paginator<Key, Item> {

    private var currentKey = initialKey
    private var isMakingRequest = false

    override suspend fun loadNext() {
        if (isMakingRequest) {
            return
        }
        isMakingRequest = true //diatur menjadi true untuk menunjukkan bahwa permintaan data sedang berlangsung.
        onLoadUpdate(true) //dipanggil untuk memberi tahu bahwa proses pengambilan data sedang berlangsung.
        val result = onRequest(currentKey)
        isMakingRequest = false
        val items = result.getOrElse {
            onError(it)
            onLoadUpdate(false)
            return
        }
        currentKey = getNextKey(items)
        onSuccess(items, currentKey)
        onLoadUpdate(false) //dipanggil untuk memberi tahu bahwa proses pengambilan data telah selesai.
    }

    //mengatur kembali kunci paginasi ke nilai awal
    override fun reset() {
        currentKey = initialKey
    }
}

/*
class DefaultPaginator<Key, Item>
initialKey: Merupakan kunci awal yang akan digunakan untuk memulai proses paginasi.
onLoadUpdate: Sebuah lambda yang menerima satu parameter Boolean, digunakan untuk memberitahu saat proses pengambilan data sedang berlangsung (true) atau tidak (false).
onRequest: Sebuah lambda dengan fungsi suspend yang menerima satu parameter nextKey bertipe Key, dan mengembalikan nilai dari tipe Result<List<Item>>. Fungsi ini bertanggung jawab untuk mengambil data berikutnya berdasarkan kunci yang diberikan.
getNextKey: Sebuah lambda dengan fungsi suspend yang menerima satu parameter bertipe List<Item>, dan mengembalikan nilai dari tipe Key. Fungsi ini digunakan untuk mendapatkan kunci berikutnya berdasarkan data yang telah diambil.
onError: Sebuah lambda dengan fungsi suspend yang menerima satu parameter bertipe Throwable, bertanggung jawab untuk menangani kesalahan yang mungkin terjadi selama proses paginasi.
onSuccess: Sebuah lambda dengan fungsi suspend yang menerima dua parameter, yaitu item bertipe List<Item> (data yang berhasil diambil), dan newKey bertipe Key (kunci yang diperbarui). Fungsi ini bertanggung jawab untuk menangani data yang berhasil diambil.

val items = result.getOrElse {
    onError(it)
    onLoadUpdate(false)
    return
}
currentKey = getNextKey(items)
onSuccess(items, currentKey)

Hasil permintaan (result) diolah menggunakan fungsi ekstensi getOrElse. Jika hasilnya adalah Failure (terdapat kesalahan), maka onError dipanggil, onLoadUpdate(false) dilakukan untuk memberi tahu bahwa proses pengambilan data telah selesai, dan fungsi keluar dari sini.
Jika hasilnya adalah Success, maka onSuccess dipanggil dengan data yang berhasil diambil dan kunci baru (currentKey diperbarui menggunakan getNextKey(items)).
*/