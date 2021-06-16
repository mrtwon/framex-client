package com.mrtwon.framex.FragmentTop

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mrtwon.framex.Content.CollectionContentEnum
import com.mrtwon.framex.Content.ContentTypeEnum
import com.mrtwon.framex.Content.GenresEnum
import com.mrtwon.framex.Content.ParcelableEnum
import com.mrtwon.framex.MainActivity
import com.mrtwon.framex.R
import com.mrtwon.framex.room.Content
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recyclerview_top_element.view.*
import kotlinx.coroutines.DelicateCoroutinesApi

class FragmentTopContent: Fragment() {
    val vm: TopViewModel by lazy { ViewModelProvider(this).get(TopViewModel::class.java) }
    val controller by lazy { (requireActivity() as MainActivity).navController }
    //val mainVm: MainViewModel by lazy { ViewModelProvider(requireActivity()).get(MainViewModel::class.java) }

    lateinit var contentType: ContentTypeEnum
    var genres: GenresEnum? = null
    var collection: CollectionContentEnum? = null
    lateinit var rv: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("self-top","onCreate()")
        // init content data of arg
        contentType = requireArguments().getParcelable<ParcelableEnum>("content_enum")!!.contentTypeEnum!!
        genres = arguments?.getParcelable<ParcelableEnum>("genres_enum")?.genresEnum
        collection = arguments?.getParcelable<ParcelableEnum>("collection_enum")?.collectionEnum
        Log.i("self-top","contentType = $contentType | genres = $genres")
        super.onCreate(savedInstanceState)
    }

    @DelicateCoroutinesApi
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.recyclerview_top_element, container, false)
        rv = view.recycler_view
        rv.layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        observer()
        genres?.let {
            vm.getContentByGenresEnum(it, contentType)
        }
        return view
    }
    fun observer(){
        vm.listLiveData.observe(viewLifecycleOwner, Observer {
            rv.adapter = Adapter(it)
        })
    }
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        lateinit var content_layout: LinearLayout
        lateinit var poster: ImageView
        lateinit var rating: TextView
        lateinit var title: TextView
        fun build(content: Content){
            //init
            content_layout = itemView.findViewById(R.id.content_layout)
            poster = itemView.findViewById(R.id.poster)
            title = itemView.findViewById(R.id.title)
            rating = itemView.findViewById(R.id.rating)
            //build
            if(content.poster != null) {
                Picasso.get().load(content.poster).into(poster)
            }

            if(content.kp_rating != null){
                rating.text = content.kp_rating.toString()
            }
            else if(content.imdb_id != null){
                rating.text = content.imdb_id.toString()
            }

            if(content.ru_title != null){
                title.text = content.ru_title
            }
            content_layout.setOnClickListener {
                val bundle = Bundle().apply {
                    putInt("id", content.id)
                }
                when(contentType){
                    ContentTypeEnum.MOVIE -> { controller.navigate(R.id.action_fragmentTop_to_fragmentAboutMovie, bundle) }
                    ContentTypeEnum.SERIAL -> { controller.navigate(R.id.action_fragmentTop_to_fragmentAboutSerial, bundle)}
                }
            }

        }
    }
    inner class Adapter(val contentList: List<Content>): RecyclerView.Adapter<ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = layoutInflater.inflate(R.layout.one_top_element, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.build(contentList[position])
        }

        override fun getItemCount(): Int {
            return contentList.size
        }
    }

    companion object{
        fun instance(contentType: ContentTypeEnum, bundle: Bundle): Fragment{
            return FragmentTopContent().apply {
                arguments = bundle.apply {
                    putParcelable("content_enum", ParcelableEnum(contentType))
                }
            }
        }
    }

    override fun onPause() {
        Log.i("self-top","onPause()")
        super.onPause()
    }
    override fun onStop() {
        Log.i("self-top","onStop()")
        super.onStop()
    }
    override fun onResume() {
        Log.i("self-top","onResume()")
        super.onResume()
    }
    override fun onDestroy() {
        Log.i("self-top","Destroy()")
        super.onDestroy()
    }

}