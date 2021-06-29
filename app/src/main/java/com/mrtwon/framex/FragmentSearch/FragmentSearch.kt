package com.mrtwon.framex.FragmentSearch

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.doOnAttach
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.mrtwon.framex.MainActivity
import com.mrtwon.framex.R
import com.mrtwon.framex.room.Content
import kotlinx.android.synthetic.main.fragment_search.view.*
import kotlinx.android.synthetic.main.one_element_search.view.*
import java.util.*

class FragmentSearch: Fragment(), View.OnClickListener {
    val vm: SearchViewModel by lazy { ViewModelProvider(this).get(SearchViewModel::class.java) }
    lateinit var rv: RecyclerView
    lateinit var text_input: TextInputEditText
    lateinit var welcome_search: LinearLayout
    val list = arrayListOf<Content>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        rv = view.recycler_view
        text_input = view.text_input
        welcome_search = view.welcome_search
        rv.adapter = SearchAdapter(list)
        rv.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        view.btn_search_description.setOnClickListener(this)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        listenerKey()
        observerSearch()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        (activity as MainActivity).reselectedNavigationPosition()
        super.onStart()
    }

    fun listenerKey() {
        text_input.doAfterTextChanged {
            if (it.toString().length >= 3) {
                welcome_search.visibility = View.GONE
                rv.visibility = View.VISIBLE
                Log.i("self-search", "search: ${it.toString()}")
                vm.search(it.toString().toLowerCase(Locale.ROOT))
            } else {
                welcome_search.visibility = View.VISIBLE
                rv.visibility = View.GONE
            }
        }
    }
    fun observerSearch(){
        vm.searchContent.observe(viewLifecycleOwner){
            list.clear()
            list.addAll(it)
            rv.adapter?.notifyDataSetChanged()
        }
    }
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tv_title = itemView.title
        val char_category = itemView.char_category
        fun bind(content: Content){
            var title = content.ru_title
            if(content.year != null){
                title += " (${content.year})"
            }
            tv_title.text = title
            char_category.text = when(content.contentType){
                "movie" -> "Ф"
                else -> "С"
            }
            itemView.linear_layout.setOnClickListener{
                val bundle = Bundle().apply {
                    putInt("id", content.id)
                }
                when(content.contentType){
                    "tv_series" -> {
                        (requireActivity() as MainActivity).navController.navigate(R.id.fragmentAboutSerial, bundle)
                    }
                    "movie" -> {
                        (requireActivity() as MainActivity).navController.navigate(R.id.fragmentAboutMovie, bundle)
                    }
                }
            }
        }
    }
    inner class SearchAdapter(val contentList: List<Content>): RecyclerView.Adapter<ViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = layoutInflater.inflate(R.layout.one_element_search, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(contentList[position])
        }

        override fun getItemCount(): Int {
            return contentList.size
        }

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_search_description -> {
                (requireActivity() as MainActivity).navController.navigate(R.id.fragmentSearchDescription)
            }
        }
    }
}