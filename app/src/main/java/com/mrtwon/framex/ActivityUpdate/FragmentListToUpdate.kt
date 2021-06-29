package com.mrtwon.framex.ActivityUpdate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Update
import com.mrtwon.framex.R
import com.mrtwon.framex.room.Content
import kotlinx.android.synthetic.main.fragment_search.view.*

class FragmentListToUpdate: Fragment() {
    val mainVM: UpdateSearchVM by lazy { ViewModelProvider(requireActivity()).get(UpdateSearchVM::class.java) }
    lateinit var rv: RecyclerView
    lateinit var help_empty_list: TextView
    val list = arrayListOf<Content>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.recyclervuew_search_update_list_to_update, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rv = view.recycler_view
        help_empty_list = view.findViewById(R.id.help_search)
        rv.adapter = Adapter(list)
        rv.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        observer()
        super.onViewCreated(view, savedInstanceState)
    }
    fun observer(){
        mainVM.movieToUpdate.observe(viewLifecycleOwner){
            if(it.isEmpty()){
                rv.visibility = View.GONE
                help_empty_list.visibility = View.VISIBLE
            }else{
                help_empty_list.visibility = View.GONE
                rv.visibility = View.VISIBLE
            }
            list.removeIf{
                it.contentType == "movie"
            }
            list.addAll(it)
        }
        mainVM.serialToUpdate.observe(viewLifecycleOwner){
            if(it.isEmpty()){
                rv.visibility = View.GONE
                help_empty_list.visibility = View.VISIBLE
            }else{
                help_empty_list.visibility = View.GONE
                rv.visibility = View.VISIBLE
            }
            list.removeIf{
                it.contentType == "tv_series"
            }
            list.addAll(it)
        }
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.title)
        val delete: ImageButton = itemView.findViewById(R.id.delete)
        fun bind(content: Content){
            var ru_title = content.ru_title
            content.year?.let { ru_title = "$ru_title ($it)" }
            title.text = "🎬 $ru_title"
            delete.setOnClickListener{
                when(content.contentType){
                    "movie" -> {
                        val newList = mainVM.movieToUpdate.value
                        newList?.remove(content)
                        mainVM.movieToUpdate.postValue(newList)
                        rv.adapter?.notifyDataSetChanged()
                    }
                    "tv_series" -> {
                        val newList = mainVM.serialToUpdate.value
                        newList?.remove(content)
                        mainVM.serialToUpdate.postValue(newList)
                        rv.adapter?.notifyDataSetChanged()
                    }
                }
            }
        }
    }
    inner class Adapter(val list: List<Content>): RecyclerView.Adapter<ViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = layoutInflater.inflate(R.layout.layout_one_element_for_update_list, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(list[position])
        }

        override fun getItemCount(): Int {
            return list.size
        }

    }
}