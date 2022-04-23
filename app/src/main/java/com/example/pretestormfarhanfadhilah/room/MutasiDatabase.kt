package com.example.pretestormfarhanfadhilah.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pretestormfarhanfadhilah.model.Mutasi

@Database(entities = [Mutasi::class], version = 1)
abstract class MutasiDatabase: RoomDatabase() {
    abstract fun mutasiDao(): MutasiDao

    companion object{
        private var INSTANCE: MutasiDatabase? = null

        fun getInstance(context: Context): MutasiDatabase?{
            if (INSTANCE == null){
                synchronized(MutasiDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                    MutasiDatabase::class.java,"Mutasi.db").build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance(){
            INSTANCE = null
        }
    }
}