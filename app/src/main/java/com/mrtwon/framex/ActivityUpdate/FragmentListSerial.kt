package com.mrtwon.framex.ActivityUpdate

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mrtwon.framex.R
import com.mrtwon.framex.room.Movie
import com.mrtwon.framex.room.Serial
import kotlinx.android.synthetic.main.fragment_search.view.*
import kotlinx.android.synthetic.main.recyclerview_search_update_element.view.*
import kotlinx.android.synthetic.main.recyclerview_top_element.view.*
import org.w3c.dom.Text
import pl.droidsonroids.gif.GifImageView
import kotlin.math.log

class FragmentListSerial: Fragment() {
    //val vm: SerialUpdateVM by lazy { ViewModelProvider(this).get(SerialUpdateVM::class.java) }
    val mainVM: UpdateSearchVM by lazy { ViewModelProvider(requireActivity()).get(UpdateSearchVM::class.java) }
    val vm: ListSerialVM by lazy { ViewModelProvider(requireActivity()).get(ListSerialVM::class.java) }

    val list = arrayListOf<Serial>()
    lateinit var rv: RecyclerView
    lateinit var help_search_content: TextView
    lateinit var help_not_found: TextView
    lateinit var tv_no_connect: TextView
    lateinit var gif_load: GifImageView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.recyclerview_search_update_element, container, false)
        rv = view.findViewById(R.id.recycler_view)
        help_search_content = view.findViewById(R.id.help_search)
        help_not_found = view.findViewById(R.id.help_not_found)
        gif_load = view.findViewById(R.id.gif_load)
        tv_no_connect = view.tv_network_error
        rv.adapter = Adapter(list)
        rv.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observer()
        super.onViewCreated(view, savedInstanceState)
    }
    fun observer(){
        vm.serialList.observe(viewLifecycleOwner){
            if(it.isEmpty()){
                help_not_found.visibility = View.VISIBLE
                help_search_content.visibility = View.GONE
                rv.visibility = View.GONE
            }else{
                gif_load.visibility = View.GONE
                rv.visibility = View.VISIBLE
                help_not_found.visibility = View.GONE
                help_search_content.visibility = View.GONE
            }
            list.clear()
            list.addAll(it)
            rv.adapter?.notifyDataSetChanged()
        }
        mainVM.serialToUpdate.observe(viewLifecycleOwner){
            Log.i("self-search-update","notifyDataSetChanged")
            rv.adapter?.notifyDataSetChanged()
        }
        mainVM.searchQuery.observe(viewLifecycleOwner){
            vm.searchSerial(it)
        }
        vm.noConnect.observe(viewLifecycleOwner){
            rv.visibility = View.GONE
            help_search_content.visibility = View.GONE
            help_not_found.visibility = View.GONE
            gif_load.visibility = View.GONE
            tv_no_connect.visibility = View.VISIBLE
        }
        vm.visibilityPb.observe(viewLifecycleOwner){
            if(it){
                help_search_content.visibility = View.GONE
                help_not_found.visibility = View.GONE
                tv_no_connect.visibility = View.GONE
                gif_load.visibility = View.VISIBLE
            }else{
                gif_load.visibility = View.GONE
            }
        }
    }
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.title)
        val check_box: CheckBox = itemView.findViewById(R.id.check_box)
        fun bind(serial: Serial){
            var ru_title = serial.ru_title
            serial.year?.let {
                ru_title = "$ru_title ($it)"
            }
            title.text = "🎬 $ru_title"

            check_box.isChecked = (mainVM.serialToUpdate.value?.indexOf(serial) ?: -1) > -1
            check_box.setOnClickListener{
                when(check_box.isChecked){
                    true -> {
                        val newList = mainVM.serialToUpdate.value ?: arrayListOf()
                        newList.add(serial)
                        mainVM.serialToUpdate.postValue(newList)
                    }
                    false -> {
                        val newList = mainVM.serialToUpdate.value
                        newList?.remove(serial)
                        mainVM.serialToUpdate.postValue(newList)
                    }
                }
            }
        }
    }
    inner class Adapter(val list: List<Serial>): RecyclerView.Adapter<ViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = layoutInflater.inflate(R.layout.layout_one_element_for_update, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            Log.i("self-search-update","onBindViewHolder")
            holder.bind(list[position])
        }

        override fun getItemCount(): Int {
            return list.size
        }

    }
}