package com.mrtwon.framex.FragmentAbout

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.startandroid.MyApplication
import com.google.android.material.appbar.MaterialToolbar
import com.mrtwon.framex.ActivityWebView.ActivityWebView
import com.mrtwon.framex.MainActivity
import com.mrtwon.framex.R
import com.mrtwon.framex.room.SerialWithGenresDataBinding
import com.mrtwon.framex.databinding.FragmentAboutSerialBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.BlurTransformation
import kotlinx.android.synthetic.main.fragment_about_movie.*
import kotlinx.coroutines.*
import java.lang.Exception

class FragmentAboutSerial: Fragment(), View.OnClickListener, Toolbar.OnMenuItemClickListener {
    val aboutVM: AboutSerialViewModel by lazy { ViewModelProvider(this).get(AboutSerialViewModel::class.java) }
    lateinit var DRAWABLE_ON: Drawable
    lateinit var DRAWABLE_OFF: Drawable
    lateinit var view: FragmentAboutSerialBinding

    lateinit var tool_bar: MaterialToolbar
    lateinit var frame_layout: FrameLayout
    var id: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        id = requireArguments().getInt("id")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).hiddenBottomBar()
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        view = DataBindingUtil.inflate<FragmentAboutSerialBinding>(
            inflater,
            R.layout.fragment_about_serial,
            container,
            false
        )
        frame_layout = view.frameLayout
        tool_bar = view.toolBar

        DRAWABLE_ON = ResourcesCompat.getDrawable(
            resources,
            R.drawable.favorite_on,
            requireActivity().theme
        )!!
        DRAWABLE_OFF = ResourcesCompat.getDrawable(
            resources,
            R.drawable.favorite_off,
            requireActivity().theme
        )!!

        view.box.background = ResourcesCompat.getDrawable(
            resources,
            R.drawable.cornet_view_about,
            requireActivity().theme
        )

        tool_bar.setNavigationIcon(R.drawable.ic_back)
        tool_bar.setNavigationOnClickListener(this)
        view.look.setOnClickListener{ checkBlockAndStartActivity() }
        tool_bar.setOnMenuItemClickListener { onMenuItemClick(it) }
        return view.root
    }

        @DelicateCoroutinesApi
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            observerAbout()
            observerIsFavorite()
            id?.let {
                aboutVM.isFavorite(it)
                aboutVM.getAbout(it)
            }
            super.onViewCreated(view, savedInstanceState)
        }

        override fun onDetach() {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            (activity as MainActivity).showBottomBar()
            super.onDetach()
        }

        fun observerIsFavorite() {
            aboutVM.isFavoriteBoolean.observe(viewLifecycleOwner, Observer {
                val favoriteIconElement = tool_bar.menu.findItem(R.id.favorite)
                if (it) {
                    favoriteIconElement.icon = DRAWABLE_ON
                } else {
                    favoriteIconElement.icon = DRAWABLE_OFF
                }
            })
        }

        fun observerAbout() {
            aboutVM.contentData.observe(viewLifecycleOwner) {
                // data binding
                view.serial = SerialWithGenresDataBinding(it)

                //load poster
                Picasso.get()
                    .load(it.poster)
                    .into(view.poster, object : Callback {
                        override fun onSuccess() {}
                        override fun onError(e: Exception?) {
                            Log.i("self-top-content", "error image load")
                            view.poster.setImageResource(R.drawable.connect_error)
                        }
                    })
                loadBackground(it.poster)
            }
        }

        fun loadBackground(url: String?) {
            val image = ImageView(requireContext())
            Picasso.get()
                .load(url)
                .transform(BlurTransformation(requireActivity(), 25, 1))
                .into(image, object : Callback {
                    override fun onSuccess() {
                        val drawable = image.drawable
                        frame_layout.background = drawable
                    }

                    override fun onError(e: Exception?) {}

                })
        }

        private fun checkBlockAndStartActivity() {
            GlobalScope.launch(CoroutineExceptionHandler { _, _ -> }) {
                id?.let {
                    val isBlocked = aboutVM.model.checkedBlockSync(it, "tv_series")
                    if (!isBlocked) {
                        Log.i("self-about","checkBlockAndStartActivity !isBlocked")
                        startActivity(
                            Intent(requireContext(), ActivityWebView::class.java).apply {
                                putExtra("id", it)
                                putExtra("content_type", "tv_series")
                            },
                        )
                    } else {
                        Log.i("self-about","checkBlockAndStartActivity isBlocked")
                        launch(Dispatchers.Main) {
                            Toast.makeText(
                                requireContext(),
                                "Контент не доступен",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }

        override fun onClick(v: View?) {
            (activity as MainActivity).navController.popBackStack()
        }

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            when (item?.itemId) {
                R.id.favorite -> {
                    if (item.icon.equals(DRAWABLE_ON)) {
                        id?.let { aboutVM.deleteFavorite(it) }
                        item.icon = DRAWABLE_OFF
                    } else {
                        id?.let { aboutVM.addFavorite(it) }
                        item.icon = DRAWABLE_ON
                    }
                }
            }
            return true
        }

    }