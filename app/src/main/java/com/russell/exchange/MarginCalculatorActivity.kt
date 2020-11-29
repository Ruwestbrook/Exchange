package com.russell.exchange

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.bigkoo.pickerview.OptionsPickerView
import java.util.ArrayList

class MarginCalculatorActivity : AppCompatActivity() {

    private var dataResult:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_margin_calculator)



    }


    fun back(view: View) {
        finish()
    }

    fun currency(view: View) {
        val list= mutableListOf<String>("USD", "EUR", "GBP")
        showOptions(list, true,"currency")
    }
    fun leverage(view: View) {
        val list= mutableListOf<String>("1：100", "1：50", "1：30", "1：20", "1：10")
        showOptions(list, false,"leverage")
    }
    fun pair(view: View) {
        val list= mutableListOf<String>("USD", "JPY USD", "NZD EUR", "USD EUR", "CAD EUR", "JPY EUR", "GBP GBP", "JPY GBP")
        showOptions(list, false,"pair")
    }
    fun calculate(view: View) {
        if(dataResult==null){
            return
        }
        findViewById<TextView>(R
            .id.result).text="11373.98"+dataResult
    }



    fun showOptions(options: List<String>, flag: Boolean,title:String){


        var educationPicker: OptionsPickerView<String>? = null
        educationPicker = OptionsPickerView<String>(this)
        educationPicker.setPicker(options as ArrayList<String>?)
        educationPicker.setCyclic(false)
        educationPicker?.setTitle(title)
        educationPicker.setOnoptionsSelectListener(OptionsPickerView.OnOptionsSelectListener { options1, _, _ ->
            if(flag){
                dataResult=options[options1]
            }
        })
        educationPicker.show()

    }
}