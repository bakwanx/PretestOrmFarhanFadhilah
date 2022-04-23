package com.example.pretestormfarhanfadhilah

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.pretestormfarhanfadhilah.databinding.ActivityTransferBinding
import com.example.pretestormfarhanfadhilah.model.Mutasi
import com.example.pretestormfarhanfadhilah.model.Saldo
import com.example.pretestormfarhanfadhilah.room.MutasiDatabase
import com.example.pretestormfarhanfadhilah.room.SaldoDatabase
import com.example.pretestormfarhanfadhilah.utils.getCurrentTime
import com.example.pretestormfarhanfadhilah.utils.showToast
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TransferActivity : AppCompatActivity() {
    lateinit var binding: ActivityTransferBinding
    var saldoDb: SaldoDatabase? = null
    var mutasiDb: MutasiDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransferBinding.inflate(layoutInflater)
        setContentView(binding.root)

        saldoDb = SaldoDatabase.getInstance(this)
        mutasiDb = MutasiDatabase.getInstance(this)

        binding.edtNumberRekening.isEnabled = false
        binding.edtNumberRekening.setText(R.string.default_rekening)

        binding.btnTransfer.setOnClickListener {
            val edtNominal = Integer.parseInt(binding.edtNominal.text.toString())
            GlobalScope.launch {
                val saldo = saldoDb?.saldoDao()?.getSaldo()
                if (saldo != null) {
                    if(saldo.saldo < edtNominal){
                        showToast(this@TransferActivity,"Jumlah saldo anda tidak mencukupi")
                        return@launch
                    }else{
                        updateSaldo(saldo,edtNominal)
                    }
                }else{
                    showToast(this@TransferActivity, "Anda tidak memiliki saldo")
                }
            }
        }
    }

    fun updateSaldo(saldo: Saldo, edtNominal: Int){
        val substraction = saldo.saldo - edtNominal
        val mutasi = Mutasi(
            id = null,
            edtNominal,
            false,
            getCurrentTime()
        )

        GlobalScope.async {
            val result = saldoDb?.saldoDao()?.updateSaldo(substraction)

            runOnUiThread {
                if(result != 0){
                    addMutasi(mutasi)
                    showToast(this@TransferActivity, "Berhasil melakukan transfer")
                    intentActivity(this@TransferActivity,MainActivity::class.java)
                }else{
                    showToast(this@TransferActivity, "Gagal melakukan transfer")
                }
            }
        }
    }

    fun addMutasi(mutasi: Mutasi){
        GlobalScope.async {
            val result = mutasiDb?.mutasiDao()?.insertMutasi(mutasi)
            runOnUiThread{
                if (result != 0.toLong()){
                    Log.d("TAG", "Berhasil menambah mutasi")
                }else{
                    Log.d("TAG", "Gagal menambah mutasi")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SaldoDatabase.destroyInstance()
        MutasiDatabase.destroyInstance()
    }

    fun intentActivity(context: Context, destinationClass: Class<*>? ){
        val intent = Intent(context, destinationClass)
        startActivity(intent)
        finish()
    }

}