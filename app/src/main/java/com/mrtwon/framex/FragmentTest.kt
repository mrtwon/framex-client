package com.mrtwon.framex

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class FragmentTest: Fragment(), View.OnClickListener {
    val vm: TestViewModel by lazy { ViewModelProvider(this).get(TestViewModel::class.java) }
    lateinit var btn_delete: Button
    lateinit var btn_add: Button
    val ID: Int = 10
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.test_layout, container, false)
        btn_delete = view.findViewById(R.id.delete)
        btn_add = view.findViewById(R.id.add)
        btn_add.setOnClickListener(this)
        btn_delete.setOnClickListener(this)
        observer()
        return view
    }
    fun observer(){
        vm.liveData.observe(viewLifecycleOwner, Observer {
            Log.i("self-test","${it.size}")
            for (elem in it){
                Log.i("self-test", "id ${elem.id} | idRef ${elem.idRef} | contentType ${elem.contentType}")
            }
        })
    }
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.delete -> { vm.remove(ID)}
            R.id.add -> { vm.add(ID) }
        }
    }
}