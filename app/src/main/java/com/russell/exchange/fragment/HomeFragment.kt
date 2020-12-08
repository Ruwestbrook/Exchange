package com.russell.exchange.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.russell.exchange.PositionActivity
import com.russell.exchange.R


class HomeFragment : Fragment() {

    private var rootView:View?=null
    private var list:RecyclerView?=null
    private val stringList= arrayListOf<String>("1M", "5M", "15M", "30M", "1H", "1D")
    private var selectOne=0

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        if(rootView==null){
            rootView=inflater.inflate(R.layout.activity_transaction, container, false)
            list=rootView?.findViewById(R.id.list)
            rootView?.findViewById<View>(R.id.long1)?.setOnClickListener {
                showDialog()
            }
            rootView?.findViewById<View>(R.id.short1)?.setOnClickListener {
                showDialog()

            }
            list?.layoutManager=GridLayoutManager(context, stringList.size)
            list?.adapter=object :RecyclerView.Adapter<RecyclerView.ViewHolder>(){
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                    val textview=TextView(parent.context)
                    textview.layoutParams=RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT)
                    textview.gravity=Gravity.CENTER
                    return object:RecyclerView.ViewHolder(textview){}
                }

                override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                    (holder.itemView as TextView).text=stringList[position]
                    holder.itemView.background=if(position==selectOne)
                        ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_send_button)
                    else
                        null
                    holder.itemView.setOnClickListener {
                        selectOne=position
                        notifyDataSetChanged()
                        checkData()
                    }
                }

                override fun getItemCount()= stringList.size

            }
            checkData()


        }
        return rootView
    }

    private fun checkData() {

       try {
           val chart: LineChart = rootView?.findViewById(R.id.chart) as LineChart

           val dataObjects= arrayListOf(1.04f, 0.96f, 1.01f, 0.97f, 1.14f, 1.00f, 1.4f, 1.04f, 1.24f, 1.04f, 0.94f)
           dataObjects.shuffle()
           val entries: MutableList<Entry> = ArrayList()
           // 循环你的数据，向图表中添加点
           // 循环你的数据，向图表中添加点
           var x=1
           for (data in dataObjects) {
               // turn your data into Entry objects
               // 图形横纵坐标默认为float形式，如果想展示文字形式，需要自定义适配器。后边会讲，这个地方传进去的X轴Y轴值都是float类型
               entries.add(Entry(x.toFloat(), data))
               x++
           }
           val dataSet = LineDataSet(entries, "Label") // 图表绑定数据，设置图表折现备注
           val lineData = LineData(dataSet)
           chart.data = lineData // 图表绑定数据值

           chart.invalidate() //
       }catch (e: Exception){

       }
    }

    companion object {
        fun newInstance() = HomeFragment()
    }


    private fun showDialog(){
        val mWindowWidth: Int
        val dialog = Dialog(context!!, R.style.simpleDialogStyle)
        val view: View = LayoutInflater.from(context!!).inflate(R.layout.layout_success, null)
        val success_button=view.findViewById(R.id.success_button) as TextView
        success_button.setOnClickListener {
            startActivity(Intent(context!!,PositionActivity::class.java))
        }
        val displayMetrics = this.resources.displayMetrics
        mWindowWidth = displayMetrics.widthPixels-dp2Px(100)
        val mWindowHeight: Int = dp2Px(200)
        dialog.setContentView(view, MarginLayoutParams(mWindowWidth, mWindowHeight))
        dialog.show()
    }



    fun dp2Px( dp: Int): Int {
        val scale = resources.displayMetrics.density
        return (dp * scale + 0.5).toInt()
    }
}