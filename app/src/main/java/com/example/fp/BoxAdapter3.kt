package com.example.fp

import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import org.jsoup.Jsoup
import org.w3c.dom.Text
import java.util.ArrayList
import android.R.attr.colorPrimary
import android.media.MediaPlayer
import androidx.core.content.ContextCompat



class BoxAdapter3(context: Context, deposits: ArrayList<Deposit>) : BaseAdapter() {

    var ctx: Context
    var lInflater: LayoutInflater
    var objects: ArrayList<Deposit>
    var i = 0
    var num = 0
    lateinit var list: List<String>
    lateinit var realPrice: TextView
    var view: View? = null






    init {
        ctx = context
        objects = deposits
        lInflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    private var dbHelper: MainActivity.DBHelper = MainActivity.DBHelper(ctx)
    var db = dbHelper.writableDatabase



    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        view = convertView
        if (view == null) {
            view = lInflater.inflate(R.layout.item3, parent, false)
        }
        val p = getProduct(position)

        (view!!.findViewById(R.id.name) as TextView).setText(p.name)

        (view!!.findViewById(R.id.invested) as TextView).setText(p.invested.toString())

        (view!!.findViewById(R.id.percentages) as TextView).setText(p.persentages)

        (view!!.findViewById(R.id.gain) as TextView).setText(p.gain)


        var dialog = Dialog(ctx)
        dialog.setContentView(R.layout.dialog_layout)
        (view!!.findViewById(R.id.button) as Button).setOnClickListener {
            dialog.show()
        }

        var s = 0
        if (p.invested.toString() != "") {
            s = p.invested.toInt()
        }

        (dialog.findViewById(R.id.btn) as Button).setOnClickListener {
            var k = (dialog.findViewById(R.id.editText) as EditText).text.toString().toInt()
            s += k
            p.invested = s

            val cv2 = ContentValues()
            cv2.put("deposit",p.invested.toString())
            var where = "name=?"
            var whereArgs = arrayOf(p.name)
            db.update("myDeposit",cv2,where,whereArgs)

            var coinSound = MediaPlayer.create(ctx,R.raw.mon)
            coinSound.start()

            dialog.dismiss()
        }



        return view!!
    }

    override fun getItem(position: Int): Any {
        return objects[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return objects.size
    }


    internal fun getProduct(position: Int): Deposit {
        return getItem(position) as Deposit
    }



}