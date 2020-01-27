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


class BoxAdapter(context: Context, products: ArrayList<Stock>) : BaseAdapter() {

    var ctx: Context
    var lInflater: LayoutInflater
    var objects: ArrayList<Stock>
    var i = 0
    var num = 0
    lateinit var mt: MyTask
    lateinit var list: List<String>
    lateinit var realPrice: TextView
    var view: View? = null
/*    var arr2 = mutableListOf(0)*/
/*    var list2 = mutableListOf<String>()*/







    init {
        ctx = context
        objects = products
        lInflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    private var dbHelper: MainActivity.DBHelper = MainActivity.DBHelper(ctx)
    var db = dbHelper.writableDatabase


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        view = convertView
        if (view == null) {
            view = lInflater.inflate(R.layout.item, parent, false)
        }
        val p = getProduct(position)



        (view!!.findViewById(R.id.stockName) as TextView).setText(p.stockName)
        (view!!.findViewById(R.id.amount) as TextView).setText(p.amount)
        (view!!.findViewById(R.id.realPrice) as TextView).setText(p.realPrice)
        (view!!.findViewById(R.id.buyPrice) as TextView).setText(p.buyPrice)
        (view!!.findViewById(R.id.difference) as TextView).setText((p.difference))
        (view!!.findViewById(R.id.percentages) as TextView).setText(p.percentages)

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
            var whereArgs = arrayOf(p.stockName)
            db.update("myStock",cv2,where,whereArgs)

            var coinSound = MediaPlayer.create(ctx,R.raw.mon)
            coinSound.start()

            dialog.dismiss()
        }


        val textColor = ContextCompat.getColor(ctx, R.color.black)
        val textColor2 = ContextCompat.getColor(ctx, R.color.red)
        val textColor3 = ContextCompat.getColor(ctx, R.color.light_green)


        for (v in p.difference.toCharArray()) {
            p.difference = p.difference.replace(",", ".")
        }
        if (p.difference.toDouble() < 0) {
            (view!!.findViewById(R.id.difference) as TextView).setTextColor(textColor2)
            (view!!.findViewById(R.id.difference) as TextView).setText(p.difference)
            (view!!.findViewById(R.id.percentages) as TextView).setTextColor(textColor2)
            (view!!.findViewById(R.id.percentages) as TextView).setText(p.percentages)
        } else {
            (view!!.findViewById(R.id.difference) as TextView).setTextColor(textColor3)
            (view!!.findViewById(R.id.percentages) as TextView).setTextColor(textColor3)
        }


/*        var stock = Stock(p.realPrice,p.stockName,p.buyPrice,p.amount,p.difference,p.percentages)*/

        /*      var str = (view!!.findViewById(R.id.amount) as TextView).text.toString()*/

        /*        var k = (dialog.findViewById(R.id.editText) as EditText).text.toString().toInt()
        arr2[position] = arr2[position] + k
        (view!!.findViewById(R.id.amount) as TextView).setText(arr2[position])*/

/*        (dialog.findViewById(R.id.btn) as Button).setOnClickListener {
            var k = (dialog.findViewById(R.id.editText) as EditText).text
            if (k != null && k.toString() != "") {
                arr2.add(0)
                arr2[position] = arr2[position]!! + ( k.toString().toInt())
                (view!!.findViewById(R.id.amount) as TextView).setText(arr2[position].toString())
                dialog.dismiss()
            }



        }*/


/*
        (view.findViewById(R.id.ivImage) as ImageView).setImageResource(p.image)
*/

/*        val cbBuy = view.findViewById(R.id.cbBox) as CheckBox*/
        // присваиваем чекбоксу обработчик
/*        cbBuy.setOnCheckedChangeListener(myCheckChangeList)
        // пишем позицию
        cbBuy.tag = position
        // заполняем данными из товаров: в корзине или нет
        cbBuy.isChecked = p.box*/
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


    internal fun getProduct(position: Int): Stock {
        return getItem(position) as Stock
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