package com.example.pretestormfarhanfadhilah.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.pretestormfarhanfadhilah.model.Mutasi

@Dao
interface MutasiDao {

    @Query("SELECT * FROM Mutasi")
    fun getAllMutasi(): List<Mutasi>

    @Insert
    fun insertMutasi(mutasi: Mutasi):Long

    @Query("DELETE FROM Mutasi WHERE id = :idMutasi")
    fun deleteMutasi(idMutasi: Int):Int
}