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
import pl.droidsonroids.gif.GifImageView
import kotlin.math.log

class FragmentListMovie: Fragment() {
    //val vm: MovieUpdateVM by lazy { ViewModelProvider(this).get(MovieUpdateVM::class.java) }
    val mainVM: UpdateSearchVM by lazy { ViewModelProvider(requireActivity()).get(UpdateSearchVM::class.java) }
    val vm: ListMovieVM by lazy { ViewModelProvider(requireActivity()).get(ListMovieVM::class.java) }

    val list = arrayListOf<Movie>()
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
        tv_no_connect = view.findViewById(R.id.tv_network_error)
        gif_load = view.findViewById(R.id.gif_load)
        rv.adapter = Adapter(list)
        rv.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observer()
        super.onViewCreated(view, savedInstanceState)
    }
    fun observer(){
            vm.movieList.observe(viewLifecycleOwner){
                Log.i("self-update-search","get data size = ${it.size}")
                if(it.isEmpty()){
                    help_not_found.visibility = View.VISIBLE
                    help_search_content.visibility = View.GONE
                    rv.visibility = View.GONE
                    gif_load.visibility = View.GONE
                }else{
                    rv.visibility = View.VISIBLE
                    help_not_found.visibility = View.GONE
                    help_search_content.visibility = View.GONE
                    gif_load.visibility = View.GONE
                }
                list.clear()
                list.addAll(it)
                rv.adapter?.notifyDataSetChanged()
            }
        mainVM.movieToUpdate.observe(viewLifecycleOwner){
            log("size movie list = ${it.size}")
            rv.adapter?.notifyDataSetChanged()
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
        mainVM.searchQuery.observe(viewLifecycleOwner){
            vm.searchMovie(it)
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
            title.text = "🎬 $ru_title"

            //val isChecked = (mainVM.movieToUpdate.value?.indexOf(movie) ?: -1) > -1
            val isChecked: Boolean = when{
                mainVM.movieToUpdate.value != null ->{
                    mainVM.movieToUpdate.value!!.filter { it.id == movie.id }.count() > 0
                }
                else -> false
            }
            mainVM.movieToUpdate.value?.let {
                log(it.toString())
            }
            log("[${movie.ru_title}]isCheckced = $isChecked")
            check_box.isChecked = isChecked
            check_box.setOnClickListener{
                when(check_box.isChecked){
                    true -> {
                        val newList = mainVM.movieToUpdate.value ?: arrayListOf()
                        newList.add(movie)
                        mainVM.movieToUpdate.postValue(newList)
                    }
                    false -> {
                        val newList = mainVM.movieToUpdate.value
                        val oldSize = newList?.size
                        newList?.removeIf{
                            it.id == movie.id
                        }
                        val newSize = newList?.size
                        log("remove element [$oldSize -> $newSize]")
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
    fun log(s: String){
        Log.i("self-list-movie",s)
    }
}