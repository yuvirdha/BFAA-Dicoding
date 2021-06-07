package com.example.yuvirdhasubmission3bfaa.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.yuvirdhasubmission3bfaa.database.UserContract.UserColumns.Companion.TABLE_NAME
import com.example.yuvirdhasubmission3bfaa.database.UserContract.UserColumns.Companion.USERNAME
import java.sql.SQLException

class FavHelper(context: Context) {
    companion object{
        private const val DATABASE_TABLE = TABLE_NAME

        private var INSTANCE: FavHelper? = null
        fun getInstance(context: Context): FavHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FavHelper(context)
            }
    }

    private var userHelper: UserHelper = UserHelper(context)
    private lateinit var database: SQLiteDatabase

    //OPEN DATABASE
    @Throws(SQLException::class)
    fun open() {
        database = userHelper.writableDatabase
    }

    //CLOSE DATABASE
    fun close() {
        userHelper.close()
        if (database.isOpen)
            database.close()
    }


    //GET ALL DATA
    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$USERNAME ASC")
    }


    //GET DATA BY ID
    fun queryByUsername(username: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$USERNAME = ?",
            arrayOf(username),
            null,
            null,
            null,
            null)
    }


    //SAVE THE DATA
    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }


    //UPDATE DATA
    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$USERNAME = ?", arrayOf(id))
    }


    //DELETE DATA
    fun deleteByUsername(id: String): Int {
        return database.delete(DATABASE_TABLE, "$USERNAME = '$id'", null)
    }
}