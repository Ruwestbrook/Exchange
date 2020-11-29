package com.russell.exchange

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.bigkoo.pickerview.OptionsPickerView
import java.util.ArrayList

class CurrencyConverterActivity : AppCompatActivity() {

    private var dataResult:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_converter)

    }


    fun back(view: View) {
        finish()
    }

    fun chooseOne(view: View) {
        showOptions("one")
    }
    fun chooseTwo(view: View) {
        showOptions("two")
    }


    private fun showOptions(title:String){

        val options= mutableListOf<String>("USD", "GBP", "EUR", "JPY", "AUD", "NZD", "HKD", "CAD")

        var educationPicker: OptionsPickerView<String>? = null
        educationPicker = OptionsPickerView<String>(this)
        educationPicker.setPicker(options as ArrayList<String>?)
        educationPicker.setCyclic(false)
        educationPicker?.setTitle(title)
        educationPicker.setOnoptionsSelectListener(OptionsPickerView.OnOptionsSelectListener { options1, _, _ ->

        })
        educationPicker.show()

    }


}