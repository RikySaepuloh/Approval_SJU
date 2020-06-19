package com.saku.approval_sju.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.saku.approval_sju.models.ModelFilter

class FilterHandler(context: Context, name: String?,
                    factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context,
    DATABASE_NAME, factory,
    DATABASE_VERSION
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createFilterTable = ("CREATE TABLE " +
                TABLE_FILTERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_VALUE
                + " VARCHAR(50) UNIQUE ," + COLUMN_PARAM + " VARCHAR(50)" + ")")
        db?.execSQL(createFilterTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_FILTERS")
        onCreate(db)
    }

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "filterDB.db"
        val TABLE_FILTERS = "filters"

        val COLUMN_ID = "_id"
        val COLUMN_VALUE = "value"
        val COLUMN_PARAM = "param"
    }

    fun addKodePP(filter: String) {

        val values = ContentValues()
        values.put(COLUMN_VALUE, filter)
        values.put(COLUMN_PARAM, "Kode PP")

        val db = this.writableDatabase

        db.insert(TABLE_FILTERS, null, values)
        db.close()
    }

    fun getAllFilter(): ArrayList<ModelFilter> {
        val list: ArrayList<ModelFilter> = ArrayList()

        // Select All Query

        // Select All Query
        val selectQuery = "SELECT  * FROM $TABLE_FILTERS "

        val db = this.readableDatabase
        try {
            val cursor: Cursor = db.rawQuery(selectQuery, null)
            try {

                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        val obj = ModelFilter()
                        //only one column
                        obj.id = cursor.getString(0)
                        obj.value = cursor.getString(1)
                        obj.param = cursor.getString(2)

                        //you could add additional columns here..
                        list.add(obj)
                    } while (cursor.moveToNext())
                }
            } finally {
                try {
                    cursor.close()
                } catch (ignore: Exception) {
                }
            }
        } finally {
            try {
                db.close()
            } catch (ignore: Exception) {
            }
        }

        return list
    }

    fun addModul(filter:String) {

        val values = ContentValues()
        values.put(COLUMN_VALUE, filter)
        values.put(COLUMN_PARAM, "Modul")

        val db = this.writableDatabase

        db.insert(TABLE_FILTERS, null, values)
        db.close()
    }

    fun resetFilter(): Boolean {
        var result = false
        val db = this.writableDatabase
        db.delete(TABLE_FILTERS,null,null)
        result = true
        db.close()
        return result
    }

    fun deleteFilter(filtername: String): Boolean {

        var result = false

        val query =
            "SELECT * FROM $TABLE_FILTERS WHERE $COLUMN_VALUE = \"$filtername\""

        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            val id = Integer.parseInt(cursor.getString(0))
            db.delete(
                TABLE_FILTERS, "$COLUMN_ID = ?",
                arrayOf(id.toString()))
            cursor.close()
            result = true
        }
        db.close()
        return result
    }

}
