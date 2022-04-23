package com.example.pretestormfarhanfadhilah

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.pretestormfarhanfadhilah.databinding.ActivityTopUpBinding
import com.example.pretestormfarhanfadhilah.model.Mutasi
import com.example.pretestormfarhanfadhilah.model.Saldo
import com.example.pretestormfarhanfadhilah.room.MutasiDatabase
import com.example.pretestormfarhanfadhilah.room.SaldoDatabase
import com.example.pretestormfarhanfadhilah.utils.getCurrentTime
import com.example.pretestormfarhanfadhilah.utils.showToast
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TopUpActivity : AppCompatActivity() {

    lateinit var binding: ActivityTopUpBinding
    var saldoDb: SaldoDatabase? = null
    var mutasiDb: MutasiDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTopUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        saldoDb = SaldoDatabase.getInstance(this)
        mutasiDb = MutasiDatabase.getInstance(this)

        binding.edtAccount.isEnabled = false
        binding.edtAccount.setText(R.string.username)

        binding.btnTopUp.setOnClickListener {
            val edtSaldo = Integer.parseInt(binding.edtTopUpNominal.text.toString())
            //check saldo, jika saldo sudah ada maka eksekusi create, jika tidak eksekusi update saldo
            GlobalScope.launch {
                val saldoUser = saldoDb?.saldoDao()?.getSaldo()
                Log.d("TAG", "saldo: ${saldoUser}")
                    if(saldoUser != null){
                        sumSaldo(edtSaldo, saldoUser)
                    }else{
                        topUp(edtSaldo)
                    }

            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        SaldoDatabase.destroyInstance()
        MutasiDatabase.destroyInstance()
    }

    fun sumSaldo(saldo: Int, saldoUser:Saldo){

        val mutasi = Mutasi(
            id = null,
            saldo,
            true,
            getCurrentTime()
        )

        val sum = saldo + saldoUser.saldo
        GlobalScope.async {

            val result = saldoDb?.saldoDao()?.updateSaldo(sum)

            runOnUiThread{
                if (result != 0){
                    addMutasi(mutasi)
                    showToast(this@TopUpActivity,"Sukses Top Up")
                    intentActivity(this@TopUpActivity, MainActivity::class.java)
                }else{
                    showToast(this@TopUpActivity,"Sukses Top Up")
                    Toast.makeText(this@TopUpActivity,"Gagal Top Up", Toast.LENGTH_LONG).show()
                }
                finish()
            }
        }
    }

    fun topUp(saldoNominal: Int){
        val saldo = Saldo(
            id = null,
            saldoNominal
        )
        val mutasi = Mutasi(
            id = null,
            saldoNominal,
            true,
            getCurrentTime()
        )

        GlobalScope.async {
            val result = saldoDb?.saldoDao()?.insertSaldo(saldo)
            runOnUiThread{
                if (result != 0.toLong()){
                    addMutasi(mutasi)
                    showToast(this@TopUpActivity,"Sukses Top Up")
                    intentActivity(this@TopUpActivity, MainActivity::class.java)
                }else{
                    showToast(this@TopUpActivity,"Gagal Top Up")
                }
                finish()
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

    fun intentActivity(context: Context, destinationClass: Class<*>? ){
        val intent = Intent(context, destinationClass)
        startActivity(intent)
        finish()
    }

}