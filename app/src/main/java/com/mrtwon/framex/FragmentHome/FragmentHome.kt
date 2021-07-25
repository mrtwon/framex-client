package com.mrtwon.framex.FragmentHome

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mrtwon.framex.Content.CollectionContentEnum
import com.mrtwon.framex.Content.GenresEnum
import com.mrtwon.framex.Content.ParcelableEnum
import com.mrtwon.framex.FragmentTop.FragmentTop
import com.mrtwon.framex.MainActivity
import com.mrtwon.framex.MainViewModel
import com.mrtwon.framex.R
import com.mrtwon.framex.room.Content
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.layout_welcome.view.*

class FragmentHome: Fragment() {
    val vm: MainViewModel by lazy { ViewModelProvider(requireActivity()).get(MainViewModel::class.java) }
    val controller by lazy { (requireActivity() as MainActivity).navController }
    lateinit var recent_rv: RecyclerView
    lateinit var card_view_recent: CardView
    val listRecent = arrayListOf<Content>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recent_rv = view.recycler_view_recent
        card_view_recent = view.recent_card_view
        recent_rv.adapter = AdapterRecent(listRecent, requireContext())
        recent_rv.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        clickListener(view)
        return view
    }

    override fun onStart() {
        (activity as MainActivity).reselectedNavigationPosition()
        super.onStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observerRecent()
        vm.getRecent()
        super.onViewCreated(view, savedInstanceState)
    }
    fun observerRecent(){
        vm.listRecent.observe(viewLifecycleOwner, Observer {
            if(it.size > 0){
                listRecent.clear()
                listRecent.addAll(it)
                recent_rv.adapter?.notifyDataSetChanged()
                card_view_recent.visibility = View.VISIBLE
            }else{
                card_view_recent.visibility = View.GONE
            }
        })
    }
    fun clickListener(v: View){
        v.apply {
            findViewById<CardView>(R.id.subscription_card_view).setOnClickListener{
                controller.navigate(R.id.action_fragmentHome_to_fragmentSubscription)
            }
            findViewById<CardView>(R.id.new_type).setOnClickListener{
                controller.navigate(R.id.action_fragmentHome_to_fragmentTop, Bundle().apply {
                    putParcelable("collection_enum", ParcelableEnum(CollectionContentEnum.NEW))
                    //putInt("img_resource", R.drawable.new_content)
                    putInt("img_resource", CollectionContentEnum.NEW.image)
                })
            }
            findViewById<CardView>(R.id.comedy).setOnClickListener{
                controller.navigate(R.id.action_fragmentHome_to_fragmentTop, Bundle().apply {
                    putParcelable("genres_enum", ParcelableEnum(GenresEnum.COMEDY))
                    putInt("img_resource", GenresEnum.COMEDY.image)
                })
            }
            findViewById<CardView>(R.id.horror).setOnClickListener{
                controller.navigate(R.id.action_fragmentHome_to_fragmentTop, Bundle().apply {
                    putParcelable("genres_enum", ParcelableEnum(GenresEnum.HORROR))
                    putInt("img_resource", GenresEnum.HORROR.image)
                })
            }
            findViewById<CardView>(R.id.drama).setOnClickListener{
                controller.navigate(R.id.action_fragmentHome_to_fragmentTop, Bundle().apply {
                    putParcelable("genres_enum", ParcelableEnum(GenresEnum.DRAMA))
                    putInt("img_resource", GenresEnum.DRAMA.image)
                })
            }
            findViewById<CardView>(R.id.criminal).setOnClickListener{
                controller.navigate(R.id.action_fragmentHome_to_fragmentTop, Bundle().apply {
                    putParcelable("genres_enum", ParcelableEnum(GenresEnum.CRIMINAL))
                    putInt("img_resource", GenresEnum.CRIMINAL.image)
                })
            }
            findViewById<CardView>(R.id.detective).setOnClickListener{
                controller.navigate(R.id.action_fragmentHome_to_fragmentTop, Bundle().apply {
                    putParcelable("genres_enum", ParcelableEnum(GenresEnum.DETECTIVE))
                    putInt("img_resource", GenresEnum.DETECTIVE.image)
                })
            }
            findViewById<CardView>(R.id.adventure).setOnClickListener{
                controller.navigate(R.id.action_fragmentHome_to_fragmentTop, Bundle().apply {
                    putParcelable("genres_enum", ParcelableEnum(GenresEnum.ADVENTURE))
                    putInt("img_resource", GenresEnum.ADVENTURE.image)
                })
            }
            findViewById<CardView>(R.id.biography).setOnClickListener{
                controller.navigate(R.id.action_fragmentHome_to_fragmentTop, Bundle().apply {
                    putParcelable("genres_enum", ParcelableEnum(GenresEnum.BIOGRAPHY))
                    putInt("img_resource", GenresEnum.BIOGRAPHY.image)
                })
            }
            findViewById<CardView>(R.id.action).setOnClickListener{
                controller.navigate(R.id.action_fragmentHome_to_fragmentTop, Bundle().apply {
                    putParcelable("genres_enum", ParcelableEnum(GenresEnum.ACTION))
                    putInt("img_resource", GenresEnum.ACTION.image)
                })
            }
            findViewById<CardView>(R.id.documental).setOnClickListener{
                controller.navigate(R.id.action_fragmentHome_to_fragmentTop, Bundle().apply {
                    putParcelable("genres_enum", ParcelableEnum(GenresEnum.DOCUMENTARYFILM))
                    putInt("img_resource", GenresEnum.DOCUMENTARYFILM.image)
                })
            }
            findViewById<CardView>(R.id.fantasy).setOnClickListener{
                controller.navigate(R.id.action_fragmentHome_to_fragmentTop, Bundle().apply {
                    putParcelable("genres_enum", ParcelableEnum(GenresEnum.FANTASY))
                    putInt("img_resource", GenresEnum.FANTASY.image)
                })
            }
        }
    }

    inner class ViewHolderRecent(itemView: View): RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.poster)
        val contentType: TextView = itemView.findViewById(R.id.contentType)
        val contentLayout: LinearLayout = itemView.findViewById(R.id.content_layout)
        fun bind(content: Content){
            contentLayout.setOnClickListener{
                val bundle: Bundle = Bundle().apply {
                    putInt("id", content.id)
                }
                when(content.contentType){
                    "tv_series" -> {
                        (activity as MainActivity).navController.navigate(R.id.fragmentAboutSerial, bundle)
                    }
                    "movie" -> {
                        (activity as MainActivity).navController.navigate(R.id.fragmentAboutMovie, bundle)
                    }
                }
            }
            contentType.text = when(content.contentType){
                "tv_series" -> "Сериал"
                "movie" -> "Фильм"
                else -> "Контент"
            }
            Picasso.get()
                .load(content.poster)
                .into(image)
        }
    }
    inner class AdapterRecent(val list: List<Content>,val  context: Context): RecyclerView.Adapter<ViewHolderRecent>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderRecent {
            return ViewHolderRecent(
                LayoutInflater.from(context).inflate(R.layout.layout_recent_element, parent, false)
            )
        }

        override fun onBindViewHolder(holder: ViewHolderRecent, position: Int) {
            holder.bind(list[position])
        }

        override fun getItemCount(): Int = list.size


    }

}