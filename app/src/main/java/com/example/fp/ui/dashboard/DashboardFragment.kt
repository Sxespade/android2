package com.example.fp.ui.dashboard

import android.app.Dialog
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.fp.BoxAdapter2
import com.example.fp.MainActivity
import com.example.fp.Obligation
import com.example.fp.R
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.*

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    lateinit var mt: MyTask
    lateinit var boxAdapter2: BoxAdapter2
    private var obligations = ArrayList<Obligation>()
    lateinit var db: SQLiteDatabase
    private lateinit var dbHelper: MainActivity.DBHelper
    val cv = ContentValues()
    var arr1 = mutableListOf<String>()
    var str = ""
    lateinit var actv: AutoCompleteTextView
    var k = 0
    var m = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)




        var lvMain2: ListView = root.findViewById(R.id.lvMain2)
        var button2: Button = root.findViewById(R.id.button2)
        var thiscontext = container!!.getContext()
        actv = root.findViewById(R.id.actv2)

        dbHelper = MainActivity.DBHelper(thiscontext)
        db = dbHelper.writableDatabase


        val result = db.query(
            "obligation",
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
                arr1.add(result.getString(name))
                println(result.getString(name) + "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS")
            } while (result.moveToNext())
        }





        if (arr1.contains("Jakut")) {
            val result1 = db.query(
                "obligation",
                arrayOf("buyPrice"),
                "name = ?",
                arrayOf("Jakut"),
                null,
                null,
                null
            )

            result.moveToFirst()
            val us0 = result.getString(2)
            val us1 = result.getString(4)
            val us2 = result.getString(5)
            var us3 = result.getString(6)
            var us4 = result.getString(7)


            m = parsing("https://smart-lab.ru/bonds/GUP-JKH-RS")

            println(us4 + "LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL") // день старый
            val format = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
            var str = (format.format(Date())).toString()
            println(str + "YYYYYYYYYYYYYYYYYYYYYYYYYYYY") // день новый
            println(time(us4).toString() + "XXXXXXXXXXXXXXXXXXXX") // разница между старым днем и новым



            if(time(us4)>0){
                var us3 = us3.toDouble() + time(us4).toDouble() * us2.toDouble() * us0.toDouble()
                us4 = str
                val cv2 = ContentValues()
                cv2.put("diff", us3)
                cv2.put("date",us4)
                var where = "name=?"
                var whereArgs = arrayOf(result.getString(1))
                db.update("obligation",cv2,where,whereArgs)
                println("GOOOOOOOOOOOOOOOOOOOOOOD")
                obligations.add(
                    Obligation(
                        "Jakut",
                        us0,
                        m,
                        us1,
                        us2.toDouble(),
                        us3,
                        us4
                    )
                )
            } else {


                obligations.add(
                    Obligation(
                        "Jakut",
                        us0,
                        m,
                        us1,
                        us2.toDouble(),
                        us3.toDouble(),
                        us4
                    )
                )}



            boxAdapter2 = BoxAdapter2(thiscontext, obligations)

            lvMain2.adapter = boxAdapter2
        }



        button2.setOnClickListener {


            var dialog = Dialog(thiscontext)
            dialog.setContentView(R.layout.dialog_layout)
            dialog.show()







            if (!arr1.contains("Jakut") && actv.text.toString() == "Jakut") {


                (dialog.findViewById(R.id.btn) as Button).setOnClickListener {
                    k = (dialog.findViewById(R.id.editText) as EditText).text.toString().toInt()
                    dialog.dismiss()


                    var str = parsing("https://smart-lab.ru/bonds/GUP-JKH-RS")
                    var coup = 0.3424
                    var diff = str.toDouble() - str.toDouble()


                    val format = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                    var str2 = (format.format(Date())).toString()


                    obligations.add(
                        Obligation("Jakut", k.toString(), str, str, coup, diff,str2)
                    )
                    cv.put("name", "Jakut")
                    cv.put("amount", k.toString())
                    cv.put("realPrice",str)
                    cv.put("buyPrice", str)
                    cv.put("coup", coup)
                    cv.put("diff", diff)
                    cv.put("date", str2)

                    println(str2 + "UUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU")

                    db!!.insert("obligation", null, cv)

                    boxAdapter2 = BoxAdapter2(thiscontext, obligations)

                    lvMain2.adapter = boxAdapter2

                }

            }
        }



        return root
    }

    fun parsing(url: String): String {
        mt = MyTask(url)
        mt.execute()
        return mt.get()
    }

    fun time(str: String): Long {
        var format = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        var timeUp = format.parse(str).time
        var diff = System.currentTimeMillis() - timeUp
        var diffDays = diff / (24 * 60 * 60 * 1000)
        return diffDays
    }

    class MyTask(_url: String) : AsyncTask<Void, Void, String>() {
        var url: String = _url

        override fun doInBackground(vararg params: Void?): String? {

            var doc = Jsoup.connect(url).get()
            val elements2 = doc.select("td")[3]
            var k = elements2.text().toString()
            for (v in k.toCharArray()) {
                k = k.replace(" ", "")
            }
            return k
        }
    }
}