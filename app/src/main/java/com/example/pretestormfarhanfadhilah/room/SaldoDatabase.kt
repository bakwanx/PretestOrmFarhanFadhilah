package com.example.pretestormfarhanfadhilah.room


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pretestormfarhanfadhilah.model.Saldo

@Database(entities = [Saldo::class], version = 1)
abstract class SaldoDatabase: RoomDatabase() {
    abstract fun saldoDao(): SaldoDao

    companion object{
        private var INSTANCE: SaldoDatabase? = null

        fun getInstance(context: Context): SaldoDatabase? {
            if(INSTANCE == null){
                synchronized(SaldoDatabase::class){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        SaldoDatabase::class.java,
                        "Saldo.db"
                    ).build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance(){
            INSTANCE = null
        }
    }

}