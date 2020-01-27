package com.example.fp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

/*
                this.deleteDatabase(baseContext.getDatabasePath("myDB").absolutePath)
*/

    }

    internal class DBHelper(context: Context)// конструктор суперкласса
        : SQLiteOpenHelper(context, "myDB", null, 1) {

        val KEY_ID = "_id"
        val NAME = "name"
        val REALPRICE = "realPrice"

        override fun onCreate(db: SQLiteDatabase) {
            // создаем таблицу с полями
            db.execSQL(
                "create table myStock ("
                        + "id integer primary key autoincrement,"
                        + "name text," + "amount text,"
                        + "realPrice text," + "buyPrice text," + "difference text," + "percentages" + ");"
            )

            db.execSQL(
                "create table obligation ("
                        + KEY_ID + " integer primary key autoincrement,"
                        + NAME + " text,"
                        + "amount text," + "realPrice text," + "buyPrice text," + "coup text," + "diff text," + "date text" + ");"
            )


            db.execSQL(
                "create table myDeposit ("
                        + KEY_ID + " integer primary key autoincrement,"
                        + "name text,"
                        + "deposit text," + "percentages text," + "gain text," + "date text" + ");"
            )


        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        }
    }



}
