package com.bs.ecommerce.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "StrResource")
class StrResource {

    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0

    @ColumnInfo(name = "languageId")
    var languageId: Int = 1

    @ColumnInfo(name = "key")
    var key: String = ""

    @ColumnInfo(name = "value")
    var value: String = ""

    constructor(key: String, value: String, langId: Int) {
        this.key = key
        this.value = value
        this.languageId = langId
    }

    constructor()
}