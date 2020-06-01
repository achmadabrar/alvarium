package com.bs.ecommerce.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface StrResourceDao {

    @Query("SELECT * FROM StrResource")
    fun getAll(): List<StrResource>

    /*@Insert
    fun insertAll(vararg strings: List<StrResource>)*/

    @Insert
    fun insertString(vararg strings: StrResource)

//    @Query("SELECT * FROM StrResource WHERE languageId IS (:langId) AND `key` IS (:key) LIMIT 1")
    @Query("SELECT * FROM StrResource WHERE `key` IS (:key) LIMIT 1")
    fun getString(key: String): StrResource?

    @Query("DELETE FROM StrResource WHERE languageId IS (:langId)")
    fun deleteAllStringsById(langId: Int)

    @Query("DELETE FROM StrResource")
    fun deleteAllStrings()

    @Query("SELECT * FROM StrResource WHERE languageId IS (:langId) LIMIT 1")
    fun isLanguageDownloaded(langId: Int) : StrResource?

    /*@Query("SELECT * FROM StrResource WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): StrResource

    @Delete
    fun delete(user: StrResource)*/

}

