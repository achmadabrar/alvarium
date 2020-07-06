package com.bs.ecommerce.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bs.ecommerce.MyApplication

@Database(entities = [StrResource::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun stringDao(): StrResourceDao

    companion object {

        @JvmStatic
        private var mInstance: AppDatabase? = null

        @JvmStatic
        @Synchronized fun getInstance(): StrResourceDao {

            if(mInstance ==null) {
                mInstance = Room.databaseBuilder(
                    MyApplication.mAppContext!!,
                    AppDatabase::class.java, "database-name"
                ).allowMainThreadQueries().build()

                return mInstance!!.stringDao()
            } else {
                return mInstance!!.stringDao()
            }

        }

    }
}
