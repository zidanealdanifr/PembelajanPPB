package com.example.cardview_sqlite

import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        val DATABASE_NAME = "Users.db"
        val DATABASE_VERSION = 1
        private val SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DBInfo.UserInput.TABLE_NAME + " (" + DBInfo.UserInput.COL_EMAIL +
                    " VARCHAR(200) PRIMARY KEY, " + DBInfo.UserInput.COL_PASS + " TEXT, " +
                    DBInfo.UserInput.COL_USERNAME + " VARCHAR(200), " + DBInfo.UserInput.COL_FULLNAME +
                    " TEXT)"
        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DBInfo.UserInput.TABLE_NAME
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    @Throws(SQLiteConstraintException::class)
    fun insertData(emailin: String, passin: String, usernamein: String, fullnamein: String): Boolean {
        val db = writableDatabase
        val namatablet = DBInfo.UserInput.TABLE_NAME
        val emailt = DBInfo.UserInput.COL_EMAIL
        val passt = DBInfo.UserInput.COL_PASS
        val usernamet = DBInfo.UserInput.COL_USERNAME
        val fullnamet = DBInfo.UserInput.COL_FULLNAME
        var sql = "INSERT INTO "+ namatablet +"("+emailt+", "+passt+", "+usernamet+", "+fullnamet+") " +
                "VALUES('"+emailin+"', '"+passin+"', '"+usernamein+"', '"+fullnamein+"')"
        db.execSQL(sql)
        return true
    }

    fun fullData():ArrayList<DBModel> {
//        val users = ArrayList<DBModel>()
        val users = arrayListOf<DBModel>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT * FROM "+DBInfo.UserInput.TABLE_NAME, null)
        } catch (e: SQLException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var emailt: String
        var passt: String
        var usernamet: String
        var fullnamet: String
        if (cursor!!.moveToFirst()){
            while (cursor.isAfterLast==false){
                emailt = cursor.getString(cursor.getColumnIndex(DBInfo.UserInput.COL_EMAIL))
                passt = cursor.getString(cursor.getColumnIndex(DBInfo.UserInput.COL_PASS))
                usernamet = cursor.getString(cursor.getColumnIndex(DBInfo.UserInput.COL_USERNAME))
                fullnamet = cursor.getString(cursor.getColumnIndex(DBInfo.UserInput.COL_FULLNAME))

                users.add(DBModel(emailt, passt, usernamet, fullnamet))
                cursor.moveToNext()
            }
        }
        return  users
    }
    fun deleteData(emailin: String){
        val db = writableDatabase
        val namatablet = DBInfo.UserInput.TABLE_NAME
        val emailt = DBInfo.UserInput.COL_EMAIL
        val sql = "DELETE FROM " +namatablet+ " WHERE "+emailt+"='"+emailin+"'"
        db.execSQL(sql)
    }
    fun updateData(emailin: String, passin: String, usernamein: String, fullnamein: String){
        val db = writableDatabase
        val namatablet = DBInfo.UserInput.TABLE_NAME
        val emailt = DBInfo.UserInput.COL_EMAIL
        val passt = DBInfo.UserInput.COL_PASS
        val usernamet = DBInfo.UserInput.COL_USERNAME
        val fullnamet = DBInfo.UserInput.COL_FULLNAME
        var sql = "UPDATE "+ namatablet + " SET "+
                usernamet+"='"+usernamein+"', "+fullnamet+"='"+fullnamein+"', "+passt+"='"+passin+"' "+
                "WHERE "+emailt+"='"+emailin+"'"
        db.execSQL(sql)
    }


}