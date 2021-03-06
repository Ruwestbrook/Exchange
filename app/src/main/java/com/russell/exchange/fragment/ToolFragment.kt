package com.russell.exchange.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.bigkoo.pickerview.OptionsPickerView
import com.russell.exchange.R
import com.russell.exchange.ServiceActivity
import java.util.ArrayList

class ToolFragment : Fragment() {

    private var dataResult:String?=null

    private var rootView:View?=null

    companion object {
        fun newInstance() = ToolFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if(rootView==null){
            rootView=inflater.inflate(R.layout.activity_currency_converter,container,false)
            rootView?.findViewById<ImageView>(R.id.first_id)?.apply{
                setOnClickListener {
                    chooseOne(it)
                }
            }
            rootView?.findViewById<ImageView>(R.id.second_id)?.apply{
                setOnClickListener {
                    chooseTwo(it)
                }
            }
        }

        return  rootView
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
        educationPicker = OptionsPickerView<String>(context)
        educationPicker.setPicker(options as ArrayList<String>?)
        educationPicker.setCyclic(false)
        educationPicker?.setTitle(title)
        educationPicker.setOnoptionsSelectListener(OptionsPickerView.OnOptionsSelectListener { options1, _, _ ->

        })
        educationPicker.show()

    }


}