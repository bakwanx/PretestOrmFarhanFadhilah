package com.example.pretestormfarhanfadhilah

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pretestormfarhanfadhilah.adapter.MutasiAdapter
import com.example.pretestormfarhanfadhilah.databinding.ActivityMainBinding
import com.example.pretestormfarhanfadhilah.room.MutasiDatabase
import com.example.pretestormfarhanfadhilah.room.SaldoDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    private var dbSaldo: SaldoDatabase? = null
    private var dbMutasi: MutasiDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbSaldo = SaldoDatabase.getInstance(this)
        dbMutasi = MutasiDatabase.getInstance(this)

        binding.lnTopUp.setOnClickListener {
            intentActivity(this,TopUpActivity::class.java)
        }

        binding.lnTransfer.setOnClickListener{
            intentActivity(this,TransferActivity::class.java)
        }

        fetchSaldo()

        fetchMutasi()
    }

    fun intentActivity(context: Context, destinationClass: Class<*>? ){
        val intent = Intent(context, destinationClass)
        startActivity(intent)
    }

    override fun onRestart() {
        super.onRestart()
        fetchMutasi()
    }

    fun fetchMutasi(){
        binding.rvMutation.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        GlobalScope.launch {
            val listMutasi = dbMutasi?.mutasiDao()?.getAllMutasi()
            Log.d("TAG", "fetchMutasi: ${listMutasi}")
            runOnUiThread {
                listMutasi?.let {
                    val adapter = MutasiAdapter(it)
                    binding.rvMutation.adapter = adapter
                }
            }
        }
    }

    fun fetchSaldo(){
        GlobalScope.launch {
            val saldo = dbSaldo?.saldoDao()?.getSaldo()
            if (saldo == null){
                binding.tvSaldo.setText("Rp 0")
            }else{
                runOnUiThread{
                    saldo.let {
                        binding.tvSaldo.text = "Rp."+it.saldo.toString()

                    }
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SaldoDatabase.destroyInstance()
        MutasiDatabase.destroyInstance()
    }
}