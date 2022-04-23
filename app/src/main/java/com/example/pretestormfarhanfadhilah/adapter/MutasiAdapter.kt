package com.example.pretestormfarhanfadhilah.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.pretestormfarhanfadhilah.MainActivity
import com.example.pretestormfarhanfadhilah.R
import com.example.pretestormfarhanfadhilah.model.Mutasi
import com.example.pretestormfarhanfadhilah.room.MutasiDatabase
import com.example.pretestormfarhanfadhilah.utils.showToast
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class MutasiAdapter(val listMutasi: List<Mutasi>) : RecyclerView.Adapter<MutasiAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val tvId: TextView = itemView.findViewById(R.id.tv_mutation_id)
        val tvNominalMutasi: TextView = itemView.findViewById(R.id.tv_money_mutation)
        val tvDate: TextView = itemView.findViewById(R.id.tv_date_mutation)
        val ivDelete: ImageView = itemView.findViewById(R.id.iv_delete)

        fun bind(mutasi: Mutasi){
            var isTopUp = ""
            if(mutasi.topUp == true){
                isTopUp = "+"
            }else{
                isTopUp = "-"
            }
            tvId.setText("id: ${mutasi.id.toString()}")
            tvNominalMutasi.setText("$isTopUp ${mutasi.nominal}")
            tvDate.setText(mutasi.timestamp)

            ivDelete.setOnClickListener {
                AlertDialog.Builder(itemView.context)
                    .setPositiveButton("Ya"){ p0, p1 ->
                        val mutasiDb = MutasiDatabase.getInstance(itemView.context)

                        GlobalScope.async {
                            val result = mutasiDb?.mutasiDao()?.deleteMutasi(idMutasi = mutasi.id!!)

                            (itemView.context as MainActivity).runOnUiThread{
                                if(result != 0){
                                    showToast(itemView.context, "Mutasi berhasil dihapus")
                                    //refresh halaman main
                                    (itemView.context as MainActivity).fetchMutasi()
                                }else{
                                    showToast(itemView.context, "Mutasi gagal dihapus")
                                }
                            }


                        }
                    }.setNegativeButton("Tidak") {p0, p1 ->
                        p0.dismiss()
                    }
                    .setMessage("Apkah anda yakin ingin menghapus mutasi ${mutasi.nominal}").setTitle("Konfirmasi hapus").create().show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.mutation_item, parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mutasi = listMutasi.get(position))
    }

    override fun getItemCount(): Int {
        return listMutasi.size
    }
}