package com.example.pretestormfarhanfadhilah.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Mutasi(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo(name="nominal") var nominal: Int,
    @ColumnInfo(name = "topup") var topUp: Boolean,
    @ColumnInfo(name = "timestamp") var timestamp: String
) : Parcelable