package com.example.fp.ui.notifications

import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.fp.BoxAdapter3
import com.example.fp.Deposit
import com.example.fp.MainActivity
import com.example.fp.R
import java.text.SimpleDateFormat
import java.util.*

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private var deposits = ArrayList<Deposit>()
    lateinit var boxAdapter3: BoxAdapter3
    lateinit var actv: AutoCompleteTextView
    var k = 0
    lateinit var db: SQLiteDatabase
    private lateinit var dbHelper: MainActivity.DBHelper
    val cv = ContentValues()
    var arr = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)



        var lvMain2: ListView = root.findViewById(R.id.lvMain2)
        actv = root.findViewById(R.id.actv2)
        var button2: Button = root.findViewById(R.id.button2)

        var thiscontext = container!!.getContext()

        var arr1: Array<String> = arrayOf("MKB", "Tinkoff")
        var adapter1 = ArrayAdapter<String>(thiscontext, R.layout.list_item, arr1)
        actv.setAdapter(adapter1)

        dbHelper = MainActivity.DBHelper(thiscontext)
        db = dbHelper.writableDatabase


        val result = db.query(
            "myDeposit",
            null,
            null,
            null,
            null,
            null,
            null
        )

        if (result.moveToFirst()) {
            val name = result.getColumnIndex("name")
            do {
                result.getString(name)
                arr.add(result.getString(name))
                println(result.getString(name) + "NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN")
            } while (result.moveToNext())
        }

        if (arr.contains("MKB")) {
            val result2 = db.query(
                "myDeposit",
                null,
                "name = ?",
                arrayOf("MKB"),
                null,
                null,
                null
            )

            result2.moveToFirst()
            val us1 = result2.getString(1)
            val us2 = result2.getString(2)
            val us3 = result2.getString(3)
            val us5 = result2.getString(5)
            var us04 = time(us5) * us2.toDouble() * 0.05 / 365
            var us4 = "%.2f".format(us04)

            deposits.add(
                Deposit(
                    us1, us2.toInt(), us3, us4
                )
            )
            boxAdapter3 = BoxAdapter3(thiscontext, deposits)

            lvMain2.adapter = boxAdapter3


        }


        if (arr.contains("Tinkoff")) {
            val result1 = db.query(
                "myDeposit",
                null,
                "name = ?",
                arrayOf("Tinkoff"),
                null,
                null,
                null
            )

            result1.moveToFirst()
            val us1 = result1.getString(1)
            val us2 = result1.getString(2)
            val us3 = result1.getString(3)
            val us5 = result1.getString(5)
            var us04 = time(us5) * us2.toDouble() * 0.05 / 365
            var us4 = "%.2f".format(us04)



            deposits.add(
                Deposit(
                    us1, us2.toInt(), us3, us4
                )
            )
            boxAdapter3 = BoxAdapter3(thiscontext, deposits)

            lvMain2.adapter = boxAdapter3

        }



        button2.setOnClickListener {

            if (actv.text.toString() == "MKB" && !arr.contains("MKB")) {

                var dialog = Dialog(thiscontext)
                dialog.setContentView(R.layout.dialog_layout)
                dialog.show()

                (dialog.findViewById(R.id.btn) as Button).setOnClickListener {
                    k = (dialog.findViewById(R.id.editText) as EditText).text.toString().toInt()
                    if (k > 0) {

                        deposits.add(
                            Deposit(
                                "MKB", k, "5,5%", "0"
                            )
                        )
                        boxAdapter3 = BoxAdapter3(thiscontext, deposits)

                        lvMain2.adapter = boxAdapter3
                    }

                    val format = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                    var str = (format.format(Date())).toString()

                    cv.put("name", "MKB")
                    cv.put("deposit", k)
                    cv.put("percentages", "5,5%")
                    cv.put("gain", "0")
                    cv.put("date", str)

                    db.insert("myDeposit", null, cv)


                    dialog.dismiss()

                    actv.setText("")

                }




            } else if (actv.text.toString() == "Tinkoff" && !arr.contains("Tinkoff")) {
                var dialog = Dialog(thiscontext)
                dialog.setContentView(R.layout.dialog_layout)
                dialog.show()

                (dialog.findViewById(R.id.btn) as Button).setOnClickListener {
                    k = (dialog.findViewById(R.id.editText) as EditText).text.toString().toInt()
                    if (k > 0) {
                        deposits.add(
                            Deposit(
                                "Tinkoff", k, "5,0%", "0"
                            )
                        )
                        boxAdapter3 = BoxAdapter3(thiscontext, deposits)

                        lvMain2.adapter = boxAdapter3
                    }

                    val format = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                    var str = (format.format(Date())).toString()

                    cv.put("name", "Tinkoff")
                    cv.put("deposit", k)
                    cv.put("percentages", "5,0%")
                    cv.put("gain", "0")
                    cv.put("date", str)

                    db.insert("myDeposit", null, cv)

                    dialog.dismiss()
                    actv.setText("")



                }


            } else {

                val imm =
                    thiscontext.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view!!.getWindowToken(), 0)


                var toast = Toast.makeText(thiscontext, "Нет такого депозита или он уже добавлен", Toast.LENGTH_SHORT)
                toast.show()
            }
        }


        return root
    }

    fun time(str: String): Long {
        var format = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        var timeUp = format.parse(str).time
        var diff = System.currentTimeMillis() - timeUp
        var diffDays = diff / (24 * 60 * 60 * 1000)
        return diffDays
    }





}