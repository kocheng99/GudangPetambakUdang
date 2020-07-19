package com.example.shrimpfeedmanagement.Holder

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.shrimpfeedmanagement.R

class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    @JvmField
    var namaBarang: TextView

    @JvmField
    var merkBarang: TextView

    @JvmField
    var hargaBarang: TextView

    @JvmField
    var jumlahBarang: TextView

    @JvmField
    var deskripsiBarang: TextView

    @JvmField
    var view: CardView

    init {
        namaBarang = itemView.findViewById(R.id.nama_barang)
        merkBarang = itemView.findViewById(R.id.merk_barang)
        hargaBarang = itemView.findViewById(R.id.harga_barang)
        jumlahBarang = itemView.findViewById(R.id.jumlah_barang)
        deskripsiBarang = itemView.findViewById(R.id.deskripsi_barang)
        view = itemView.findViewById(R.id.cvMain)
    }
}