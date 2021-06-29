package com.mrtwon.framex.FragmentSearch

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputEditText
import com.mrtwon.framex.MainActivity
import com.mrtwon.framex.R
import com.mrtwon.framex.room.Content
import kotlinx.android.synthetic.main.fragment_about_movie.view.*
import kotlinx.android.synthetic.main.fragment_search.view.*
import kotlinx.android.synthetic.main.fragment_search.view.recycler_view
import kotlinx.android.synthetic.main.fragment_search.view.text_input
import kotlinx.android.synthetic.main.fragment_search.view.welcome_search
import kotlinx.android.synthetic.main.fragment_search_description.view.*
import kotlinx.android.synthetic.main.one_element_search.view.*
import kotlinx.android.synthetic.main.one_element_search.view.title
import java.util.*

class FragmentSearchDescription: Fragment(), View.OnClickListener {
    val vm: SearchViewModel by lazy { ViewModelProvider(this).get(SearchViewModel::class.java) }
    lateinit var rv: RecyclerView
    lateinit var text_input: TextInputEditText
    lateinit var welcome_search: LinearLayout
    val list = arrayListOf<Content>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search_description, container, false)
        rv = view.recycler_view
        text_input = view.text_input
        welcome_search = view.welcome_search
        rv.adapter = SearchAdapter(list)
        rv.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        view.chip_one.setOnClickListener(this)
        view.chip_two.setOnClickListener(this)
        view.chip_three.setOnClickListener(this)
        view.chip_four.setOnClickListener(this)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        listenerKey()
        observerSearch()
        super.onViewCreated(view, savedInstanceState)
    }
    fun listenerKey(){
        text_input.doAfterTextChanged {
            Log.i("self-search","search: ${it.toString()}")
            if(it.toString().length >= 3) {
                welcome_search.visibility = View.GONE
                rv.visibility = View.VISIBLE
                vm.searchDescription(it.toString().toLowerCase(Locale.ROOT))
            }else{
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
        val descripton = itemView.description
        fun bind(content: Content){
            var title = "\uD83C\uDFAC ${content.ru_title}"
            if(content.year != null){
                title += " (${content.year})"
            }
            tv_title.text = title

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                descripton.setText(Html.fromHtml(content.description_lower,Html.FROM_HTML_MODE_LEGACY));
            } else {
                descripton.setText(Html.fromHtml(content.description_lower));
            }

            char_category.text = when(content.contentType){
                "movie" -> "Ф"
                else -> "С"
            }
            itemView.setOnClickListener{
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
            val view = layoutInflater.inflate(R.layout.one_element_search_description, parent, false)
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
        text_input.setText((v as Chip).text)
    }

}