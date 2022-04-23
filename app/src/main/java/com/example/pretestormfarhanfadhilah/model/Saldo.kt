package com.example.pretestormfarhanfadhilah.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Saldo(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo(name = "saldo") var saldo: Int
) : Parcelable