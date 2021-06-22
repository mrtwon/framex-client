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
import kotlinx.android.synthetic.main.fragment_search.view.*
import kotlinx.android.synthetic.main.one_top_element.view.*
import kotlin.math.log

class FragmentListMovie: Fragment() {
    //val vm: MovieUpdateVM by lazy { ViewModelProvider(this).get(MovieUpdateVM::class.java) }
    val mainVM: UpdateSearchVM by lazy { ViewModelProvider(requireActivity()).get(UpdateSearchVM::class.java) }
    val list = arrayListOf<Movie>()
    lateinit var rv: RecyclerView
    lateinit var help_search_content: TextView
    lateinit var help_not_found: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.recyclerview_search_update_element, container, false)
        rv = view.findViewById(R.id.recycler_view)
        help_search_content = view.findViewById(R.id.help_search)
        help_not_found = view.findViewById(R.id.help_not_found)
        rv.adapter = Adapter(list)
        rv.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observer()
        super.onViewCreated(view, savedInstanceState)
    }
    fun observer(){
            mainVM.movieList.observe(viewLifecycleOwner){
                Log.i("self-update-search","get data size = ${it.size}")
                if(it.isEmpty()){
                    help_not_found.visibility = View.VISIBLE
                    help_search_content.visibility = View.GONE
                    rv.visibility = View.GONE
                }else{
                    rv.visibility = View.VISIBLE
                    help_not_found.visibility = View.GONE
                    help_search_content.visibility = View.GONE
                }
                list.clear()
                list.addAll(it)
                rv.adapter?.notifyDataSetChanged()
            }
        mainVM.movieToUpdate.observe(viewLifecycleOwner){
            rv.adapter?.notifyDataSetChanged()
        }
    }
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.title)
        val check_box: CheckBox = itemView.findViewById(R.id.check_box)
        fun bind(movie: Movie){
            var ru_title = movie.ru_title
            movie.year?.let {
                ru_title = "$ru_title ($it)"
            }
            title.text = ru_title

            check_box.isChecked = (mainVM.movieToUpdate.value?.indexOf(movie) ?: -1) > -1
            check_box.setOnClickListener{
                when(check_box.isChecked){
                    true -> {
                        val newList = mainVM.movieToUpdate.value ?: arrayListOf()
                        newList.add(movie)
                        mainVM.movieToUpdate.postValue(newList)
                    }
                    false -> {
                        val newList = mainVM.movieToUpdate.value
                        newList?.remove(movie)
                        mainVM.movieToUpdate.postValue(newList)
                    }
                }
            }
        }
    }
    inner class Adapter(val list: List<Movie>): RecyclerView.Adapter<ViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = layoutInflater.inflate(R.layout.layout_one_element_for_update, parent, false)
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