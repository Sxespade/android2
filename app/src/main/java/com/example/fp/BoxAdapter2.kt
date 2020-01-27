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



class BoxAdapter2(context: Context, obligations: ArrayList<Obligation>) : BaseAdapter() {

    var ctx: Context
    var lInflater: LayoutInflater
    var objects: ArrayList<Obligation>
    var i = 0
    var num = 0
    lateinit var list: List<String>
    lateinit var realPrice: TextView
    var view: View? = null





    init {
        ctx = context
        objects = obligations
        lInflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }


    private var dbHelper: MainActivity.DBHelper = MainActivity.DBHelper(ctx)
    var db = dbHelper.writableDatabase



    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        view = convertView
        if (view == null) {
            view = lInflater.inflate(R.layout.item2, parent, false)
        }
        val p = getProduct(position)

        (view!!.findViewById(R.id.name1) as TextView).setText(p.name)

        (view!!.findViewById(R.id.amount) as TextView).setText(p.amount)

        (view!!.findViewById(R.id.realPrice1) as TextView).setText(p.realPrice)


        var dialog = Dialog(ctx)
        dialog.setContentView(R.layout.dialog_layout)
        (view!!.findViewById(R.id.button) as Button).setOnClickListener {
            dialog.show()
        }

        var s = 0
        if (p.amount != "") {
            s = p.amount.toInt()
        }

        (dialog.findViewById(R.id.btn) as Button).setOnClickListener {
            var k = (dialog.findViewById(R.id.editText) as EditText).text.toString().toInt()
            s += k
            p.amount = s.toString()

            val cv2 = ContentValues()
            cv2.put("amount",p.amount)
            var where = "name=?"
            var whereArgs = arrayOf(p.name)
            db.update("obligation",cv2,where,whereArgs)

            var coinSound = MediaPlayer.create(ctx,R.raw.mon)
            coinSound.start()

            dialog.dismiss()
        }



        (view!!.findViewById(R.id.buyPrice1) as TextView).setText(p.buyPrice)

        (view!!.findViewById(R.id.coup) as TextView).setText(p.coup.toString())

        (view!!.findViewById(R.id.diff) as TextView).setText(p.diff.toString())


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


    internal fun getProduct(position: Int): Obligation {
        return getItem(position) as Obligation
    }



}