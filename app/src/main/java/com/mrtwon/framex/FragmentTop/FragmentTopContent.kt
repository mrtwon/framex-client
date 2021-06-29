package com.mrtwon.framex.FragmentTop

import android.os.Bundle
import android.os.Parcelable
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
import com.mrtwon.framex.Helper.HelperFunction.Companion.roundRating
import com.mrtwon.framex.MainActivity
import com.mrtwon.framex.R
import com.mrtwon.framex.room.Content
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recyclerview_top_element.view.*
import kotlinx.coroutines.DelicateCoroutinesApi
import pl.droidsonroids.gif.GifImageView
import java.lang.Exception

class FragmentTopContent: Fragment() {
    val vm: TopViewModel by lazy { ViewModelProvider(this).get(TopViewModel::class.java) }
    val controller by lazy { (requireActivity() as MainActivity).navController }
    val listContent = arrayListOf<Content>()
    lateinit var gif_load: GifImageView
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
        rv.adapter = Adapter(listContent)
        gif_load = view.findViewById(R.id.gif_load)
        rv.layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        observer()
        if(genres != null){
            vm.getContentByGenresEnum(genres!!, contentType)
        }
        else if(collection != null){
            vm.getContentByCollectionEnum(collection!!, contentType)
        }
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        rv.layoutManager?.onSaveInstanceState()?.let {
            outState.putParcelable("layout_manager",it)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        savedInstanceState?.getParcelable<Parcelable>("layout_manager")?.let {
        rv.layoutManager?.onRestoreInstanceState(it)
        }
        super.onActivityCreated(savedInstanceState)
    }
    fun observer(){
        vm.listLiveData.observe(viewLifecycleOwner, Observer {
            log("observer is running")
            listContent.clear()
            listContent.addAll(it)
            rv.adapter?.notifyDataSetChanged()
            gif_load.visibility = View.GONE
            rv.visibility = View.VISIBLE
        })
    }
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        lateinit var content_layout: LinearLayout
        lateinit var poster: ImageView
        lateinit var rating_kp: TextView
        lateinit var rating_imdb: TextView
        lateinit var title: TextView
        fun build(content: Content){
            //init
            content_layout = itemView.findViewById(R.id.content_layout)
            poster = itemView.findViewById(R.id.poster)
            title = itemView.findViewById(R.id.title)
            rating_kp = itemView.findViewById(R.id.kp_rating)
            rating_imdb = itemView.findViewById(R.id.imdb_rating)
            //build
            if(content.poster != null) {
                Picasso.get().load(content.poster).into(poster, object: Callback{
                    override fun onSuccess() {}
                    override fun onError(e: Exception?) {
                        Log.i("self-top-content","error image load")
                        poster.setImageResource(R.drawable.connect_error)
                    }
                })
            }

            rating_kp.text = roundRating(content.kp_rating)
            rating_imdb.text = roundRating(content.imdb_rating)
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
    private fun log(s: String){
        Log.i("self-top-content",s)
    }

}