package com.example.pretestormfarhanfadhilah.room

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.pretestormfarhanfadhilah.model.Saldo

@Dao
interface SaldoDao {

    @Query("SELECT * FROM Saldo WHERE id=1")
    fun getSaldo(): Saldo

    @Insert(onConflict = REPLACE)
    fun insertSaldo(saldo: Saldo):Long

    @Query("UPDATE Saldo SET saldo=:saldo WHERE id=1")
    fun updateSaldo(saldo: Int):Int
}