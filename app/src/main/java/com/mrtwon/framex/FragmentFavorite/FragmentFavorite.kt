package com.mrtwon.framex.FragmentFavorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.dueeeke.tablayout.SlidingTabLayout
import com.mrtwon.framex.MainActivity
import com.mrtwon.framex.R
import com.mrtwon.framex.room.Content
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recyclerview_favorite.*

class FragmentFavorite: Fragment() {
    val vm: FavoriteViewModel by lazy { ViewModelProvider(this).get(FavoriteViewModel::class.java) }
    lateinit var recycler_view: RecyclerView
    lateinit var tv_not_found: TextView
    val contentList = arrayListOf<Content>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)
        recycler_view = view.findViewById(R.id.recycler_view)
        tv_not_found = view.findViewById(R.id.textView_favorite_not_found)
        recycler_view.layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        observerContent()
        vm.getContent()
        recycler_view.adapter = Adapter(contentList)
        return view
    }

    override fun onStart() {
        (activity as MainActivity).reselectedNavigationPosition()
        super.onStart()
    }
    fun observerContent(){
        vm.contentList.observe(viewLifecycleOwner, Observer {
            if(it.isEmpty()){
                recycler_view.visibility = View.GONE
                tv_not_found.visibility = View.VISIBLE
            }else{
                contentList.clear()
                contentList.addAll(it.reversed())
                recycler_view.adapter?.notifyDataSetChanged()
                tv_not_found.visibility = View.GONE
                recycler_view.visibility = View.VISIBLE
            }
        })
    }

    inner class ViewHolder(val itemView: View): RecyclerView.ViewHolder(itemView){

        val title: TextView = itemView.findViewById(R.id.title)
        val poster: ImageView = itemView.findViewById(R.id.poster)
        val contentType: TextView = itemView.findViewById(R.id.contentType)
        val btn_delete: Button = itemView.findViewById(R.id.btn_delete_favorite)

        fun bind(content: Content){

            btn_delete.setOnClickListener{
                if(content.contentType != null){
                    Log.i("self-favorite","start remove ${content.id} | ${content.contentType}")
                    contentList.remove(content)
                    vm.removeFavorite(content.id, content.contentType!!)
                }
            }
            poster.setOnClickListener{
                val bundle = Bundle().apply { putInt("id", content.id) }
                when(content.contentType){
                    "movie" -> {
                        (requireActivity() as MainActivity).navController.navigate(R.id.action_fragmentFavorite_to_fragmentAboutMovie, bundle)
                    }
                    "tv_series" -> {
                        (requireActivity() as MainActivity).navController.navigate(R.id.action_fragmentFavorite_to_fragmentAboutSerial, bundle)
                    }
                }
            }

            content.ru_title?.let {
                title.text = it
            }

            content.contentType?.let {
                contentType.text = when(it){
                    "movie" -> "Фильм"
                    else -> "Сериал"
                }
            }

            Picasso.get()
                .load(content.poster)
                .into(poster)

        }
    }
    inner class Adapter(val listContent: List<Content>) : RecyclerView.Adapter<ViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(layoutInflater.inflate(R.layout.one_favorite_element, parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(listContent[position])
        }

        override fun getItemCount(): Int {
            return listContent.size
        }

    }
}