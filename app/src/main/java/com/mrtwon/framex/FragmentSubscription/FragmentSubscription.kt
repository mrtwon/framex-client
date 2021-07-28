package com.mrtwon.framex.FragmentSubscription

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.mrtwon.framex.ActivityWebView.ActivityWebView
import com.mrtwon.framex.MainActivity
import com.mrtwon.framex.R
import com.mrtwon.framex.WorkManager.OldWork
import com.mrtwon.framex.room.Notification
import com.mrtwon.framex.room.Serial
import com.squareup.picasso.Picasso

class FragmentSubscription:  Fragment(){
    val vm: SubscriptionVM by lazy { ViewModelProvider(this).get(SubscriptionVM::class.java) }
    val listNotification = arrayListOf<Notification>()
    val listSubscription = arrayListOf<Serial>()
    lateinit var rv_notification: RecyclerView
    lateinit var rv_subscription: RecyclerView
    lateinit var helper_notify: TextView
    lateinit var helper_subscript: TextView
    lateinit var state_workmanager_online: TextView
    lateinit var state_workmanager_offline: TextView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_subscription, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rv_notification = view.findViewById(R.id.rv_notification)
        rv_subscription = view.findViewById(R.id.rv_subscription)
        helper_notify = view.findViewById(R.id.helper_notification)
        helper_subscript = view.findViewById(R.id.helper_subscription)
        state_workmanager_online = view.findViewById(R.id.state_online)
        state_workmanager_offline = view.findViewById(R.id.state_offline)
        rv_subscription.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        rv_notification.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        rv_notification.adapter = AdapterNotification(listNotification)
        rv_subscription.adapter = SubscriptionAdapter(listSubscription)
        observerNotification()
        observerSubscription()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        showStatusWorkManager()
        super.onStart()
    }

    fun observerSubscription(){
        vm.subscriptionListLiveData.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                helper_subscript.visibility = View.VISIBLE
                rv_subscription.visibility = View.GONE
            } else {
                helper_subscript.visibility = View.GONE
                rv_subscription.visibility = View.VISIBLE
                listSubscription.clear()
                listSubscription.addAll(it)
                listSubscription.reverse()
                rv_subscription.adapter?.notifyDataSetChanged()
            }
        }
    }
    fun observerNotification(){
        vm.notificationListLiveData.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                rv_notification.visibility = View.GONE
                helper_notify.visibility = View.VISIBLE
            } else {
                rv_notification.visibility = View.VISIBLE
                helper_notify.visibility = View.GONE
                listNotification.clear()
                listNotification.addAll(it)
                listNotification.reverse()
                rv_notification.adapter?.notifyDataSetChanged()
            }
        }

    }

    private fun showStatusWorkManager(){
        val instance = WorkManager.getInstance(requireContext())
        val workInfo = instance.getWorkInfosByTag("subscription")
        val state = workInfo.get()
        if(state.isNotEmpty() && state[state.lastIndex].state == WorkInfo.State.ENQUEUED){
            state_workmanager_online.visibility = View.VISIBLE
            state_workmanager_offline.visibility = View.GONE
        }else{
            state_workmanager_offline.visibility = View.VISIBLE
            state_workmanager_online.visibility = View.GONE
        }
    }


    // subscription element
    inner class SubscriptionViewHolder(private val itemView: View): RecyclerView.ViewHolder(itemView){
        val poster: ImageView = itemView.findViewById(R.id.poster)
        val delete: ImageButton = itemView.findViewById(R.id.btn_delete_subscription)
        fun bind(serial: Serial){
            Picasso.get().load(serial.poster).into(poster)
            delete.setOnClickListener{
                vm.removeSubscription(serial.id)
            }
            poster.setOnClickListener{
                val bundle = Bundle().apply { putInt("id", serial.id) }
                (requireActivity() as MainActivity).navController.navigate(R.id.fragmentAboutSerial, bundle)
            }
        }
    }
    inner class SubscriptionAdapter(private val list: List<Serial>): RecyclerView.Adapter<SubscriptionViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriptionViewHolder {
            return SubscriptionViewHolder(layoutInflater.inflate(R.layout.one_element_subscription, parent, false))
        }

        override fun onBindViewHolder(holder: SubscriptionViewHolder, position: Int) {
            holder.bind(list[position])
        }

        override fun getItemCount(): Int {
            return list.size
        }

    }



    // notification element
    inner class NotificationViewHolder(private val itemView: View): RecyclerView.ViewHolder(itemView){
        var ru_title: TextView = itemView.findViewById(R.id.ru_title)
        var season_and_episode: TextView = itemView.findViewById(R.id.season_and_episode)
        var delete: ImageButton = itemView.findViewById(R.id.delete)
        var layout: LinearLayout = itemView.findViewById(R.id.content_layout)
        fun bind(notification: Notification){
            ru_title.text = notification.ru_title
            season_and_episode.text = "${notification.season} x ${notification.series}"
            delete.setOnClickListener{
                vm.removeNotification(notification)
            }
            layout.setOnClickListener{
                val intent = Intent(requireActivity(), ActivityWebView::class.java).apply {
                    putExtra("id", notification.content_id)
                    putExtra("content_type","tv_series")
                }
                startActivity(intent)
            }
        }
    }
    inner class AdapterNotification(val list: List<Notification>): RecyclerView.Adapter<NotificationViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
            return NotificationViewHolder(layoutInflater.inflate(R.layout.one_element_notification, parent, false))
        }

        override fun onBindViewHolder(holder: NotificationViewHolder, position: Int){
            holder.bind(list[position])
        }

        override fun getItemCount(): Int {
            return list.size
        }

    }
}