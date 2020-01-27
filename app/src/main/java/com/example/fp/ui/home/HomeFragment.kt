package com.example.fp.ui.home

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.fp.BoxAdapter
import com.example.fp.MainActivity
import com.example.fp.R
import com.example.fp.Stock
import kotlinx.android.synthetic.main.fragment_home.*
import org.jsoup.Jsoup
import java.util.ArrayList

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var stocks = ArrayList<Stock>()
    lateinit var boxAdapter: BoxAdapter
    lateinit var db: SQLiteDatabase
    private lateinit var dbHelper: MainActivity.DBHelper
    var arr = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        var lvMain2: ListView = root.findViewById(R.id.lvMain2)
        var thiscontext = container!!.getContext()
        var button2: Button = root.findViewById(R.id.button2)
        var actv: AutoCompleteTextView = root.findViewById(R.id.actv)
        var actv2: AutoCompleteTextView = root.findViewById(R.id.actv2)
        var actv3: AutoCompleteTextView = root.findViewById(R.id.actv3)

        dbHelper = MainActivity.DBHelper(thiscontext)
        db = dbHelper.writableDatabase


        val result = db.query(
            "myStock",
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
            } while (result.moveToNext())
        }


        if (arr.size > 0) {
            for (i in 0..arr.size-1) {
            check(arr[i], thiscontext, lvMain2)}
        }









        button2.setOnClickListener {

            if (actv.text.toString() == "ADI" && actv2.text.toString() != "" && actv3.text.toString() != "" && !arr.contains(
                    "ADI"
                )
            ) {
                var n = parsing("https://investfunds.ru/stocks/Analog-Devices/")
                btnCheck(
                    n,
                    actv.text.toString(),
                    actv2.text.toString(),
                    actv3.text.toString(),
                    thiscontext,
                    lvMain2
                )
            } else if (actv.text.toString() == "APD" && actv2.text.toString() != "" && actv3.text.toString() != "" && !arr.contains(
                    "APD"
                )
            ) {
                var n = parsing("https://investfunds.ru/stocks/Air-Products-and-Chemicals/")
                btnCheck(
                    n,
                    actv.text.toString(),
                    actv2.text.toString(),
                    actv3.text.toString(),
                    thiscontext,
                    lvMain2
                )
            } else if (actv.text.toString() == "AAPL" && actv2.text.toString() != "" && actv3.text.toString() != "" && !arr.contains(
                    "AAPL"
                )
            )
            {var n = parsing("https://investfunds.ru/stocks/Apple/")
                btnCheck(
                    n,
                    actv.text.toString(),
                    actv2.text.toString(),
                    actv3.text.toString(),
                    thiscontext,
                    lvMain2
                )}
        }





        return root
    }

    fun parsing(url: String): String {
        var mt = MyTask(url)
        mt.execute()
        return mt.get()
    }

    fun check(name: String, context: Context, lvMain2: ListView) {
        if (arr.contains(name)) {
            val result = db.query(
                "myStock",
                null,
                "name = ?",
                arrayOf(name),
                null,
                null,
                null
            )

            result.moveToFirst()
            val us1 = result.getString(1)
            val us2 = result.getString(2)
            val us3 = result.getString(3)
            val us4 = result.getString(4)
            val us04 = us4.toDouble()
            val us004 = "%.2f".format(us04)
            val us5 = result.getString(5)
            val us6 = result.getString(6)
            val us06 = us6.toDouble()
            val us006 = "%.2f".format(us06) + "%"

            stocks.add(Stock(us1, us2, us3, us004, us5, us006))
            boxAdapter = BoxAdapter(context, stocks)

            lvMain2.adapter = boxAdapter

        }
    }


    fun btnCheck(
        str: String,
        actv1: String,
        actv2: String,
        actv3: String,
        context: Context,
        lvMain2: ListView
    ) {


        var percentages = (str.toDouble() / actv3.toDouble() - 1) * 100
        var difference = str.toDouble() - actv3.toDouble()

        stocks.add(
            Stock(
                actv1,
                actv2,
                str,
                "%.2f".format(actv3.toDouble()),
                "%.2f".format(difference),
                "%.2f".format(percentages) + "%"
            )
        )
        boxAdapter = BoxAdapter(context, stocks)

        lvMain2.adapter = boxAdapter

        val cv = ContentValues()

        cv.put("name", actv1)
        cv.put("amount", actv2)
        cv.put("realPrice", str)
        cv.put("buyPrice", actv3)
        cv.put("difference", difference)
        cv.put("percentages", percentages)

        db.insert("myStock", null, cv)

    }


    class MyTask(_url: String) : AsyncTask<Void, Void, String>() {
        var url: String = _url

        lateinit var anek: String
        lateinit var list: List<String>
        var num = 0


        override fun doInBackground(vararg params: Void?): String? {

            var doc = Jsoup.connect(url).get()
            val elements2 = doc.select("div.price.left")

            return elements2.text().toString()
        }
    }


}