package com.unal.edu.companieslist

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class CompanyDatabaseHelper  (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    companion object{
        private const val DATABASE_NAME = "companiesapp.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "company"
        private const val COLUMN_ID = "company_id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_URI = "uri"
        private const val COLUMN_PHONE = "phone"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PRODANDSERV = "prodandserv"
        private const val  COLUMN_CLASSIFICATION = "classification"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_NAME TEXT, $COLUMN_URI TEXT, $COLUMN_PHONE INTEGER, $COLUMN_EMAIL TEXT, $COLUMN_PRODANDSERV TEXT, $COLUMN_CLASSIFICATION TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun insertCompany(company: Company){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, company.name)
            put(COLUMN_URI, company.uri)
            put(COLUMN_PHONE, company.phone)
            put(COLUMN_EMAIL, company.email)
            put(COLUMN_PRODANDSERV, company.prodAndServ)
            put(COLUMN_CLASSIFICATION, company.classification)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllCompanies(): List<Company>{
        val companiesList = mutableListOf<Company>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            val uri = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_URI))
            val phone = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PHONE))
            val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
            val prodandservice = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODANDSERV))
            val classification = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLASSIFICATION))

            val company = Company(
                id,
                name,
                uri,
                phone,
                email,
                prodandservice,
                classification
            )
            companiesList.add(company)
        }

        cursor.close()
        db.close()
        return companiesList
    }

    fun updateCompany(company: Company){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, company.name)
            put(COLUMN_URI, company.uri)
            put(COLUMN_PHONE, company.phone)
            put(COLUMN_EMAIL, company.email)
            put(COLUMN_PRODANDSERV, company.prodAndServ)
            put(COLUMN_CLASSIFICATION, company.classification)
        }
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(company.id.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }

    fun getCompanyById(companyId: Int): Company{
        val  db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $companyId"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
        val uri = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_URI))
        val phone = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PHONE))
        val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
        val prodandservice = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODANDSERV))
        val classification = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLASSIFICATION))

        cursor.close()
        db.close()
        return Company(
            id,
            name,
            uri,
            phone,
            email,
            prodandservice,
            classification)
    }

    fun deleteCompany (companyId: Int){
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(companyId.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }
}