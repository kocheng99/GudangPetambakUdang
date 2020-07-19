package com.example.shrimpfeedmanagement.Model

class ModelBarang {

    var key: String? = null
    var nama: String? = null
    var merk: String? = null
    var harga: String? = null
    var jumlah: String? = null
    var deskripsi: String? = null

    constructor() {}

    constructor(namaBarang: String?, merkBarang: String?, hargaBarang: String?, jumlahBarang: String?, deskripsiBarang: String?) {
        nama = namaBarang
        merk = merkBarang
        harga = hargaBarang
        jumlah = jumlahBarang
        deskripsi = deskripsiBarang
    }
}