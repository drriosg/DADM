package com.unal.edu.companieslist

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class CompanyDatabaseHelper  (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    companion object{
        private const val DATABASE_NAME = "companiesapp.db"
        private const val DATABASE_VERSION = "1"
        private const val TABLE_NAME = "company"
        private const val COLUM_ID = "id"
        private const val COLUM_NAME = "name"
        private const val COLUM_URI = "uri"
        private const val COLUM_PHONE = "phone"
        private const val COLUM_EMAIL = "email"
        private const val COLUM_PRODANDSERV = "prodandserv"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        
        TODO("Not yet implemented")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}