package com.gokul.sample.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gokul.sample.data.model.ListModel
import com.gokul.sample.databinding.ActivityMainBinding
import com.gokul.sample.ui.adapter.ListAdapter
import com.gokul.sample.ui.viewmodel.MyViewModel
import com.gokul.sample.utils.Helper.toast
import com.gokul.sample.utils.Resource


class MainActivity : AppCompatActivity() {
    /*val myViewModel: MyViewModel by lazy {
        ViewModelProvider(this@MainActivity)[MyViewModel::class.java]
    }*/
    val myViewModel: MyViewModel by viewModels()
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater);
    }
    private val listData: ArrayList<ListModel.Data> = arrayListOf()

    val adapter: ListAdapter by lazy {
        ListAdapter(listData, this@MainActivity)
    }
    var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupView()
        setupObserver()
    }

    private fun setupView() {
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL,false)
            recyclerView.isNestedScrollingEnabled = false
            recyclerView.adapter = adapter
            mScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, oldScrollX, oldScrollY ->
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                     page++
//                    loadingPB.setVisibility(View.VISIBLE)
                    myViewModel.getListData(page)
                }

                if (scrollY > oldScrollY) {
                    btnAdd.hide()
                } else {
                    btnAdd.show()
                }
            })

            btnAdd.setOnClickListener {
                startActivity(Intent(this@MainActivity, CreateActivity::class.java))
            }
        }
    }

    private fun setupObserver() {
        myViewModel.listResponse.observe(this@MainActivity, Observer {
            when(it.status){
                Resource.Status.SUCCESS ->{
                    it.data?.data.let {users->
                        val previousLength = listData.size
                        users?.let { arr ->
                            listData.addAll(arr)
                            listData.addAll(arr)
                        }
                        adapter.notifyItemRangeInserted(previousLength, listData.size)
                    }
                }
                Resource.Status.FAILURE ->{
                    toast(it.message.toString())
                }
                Resource.Status.NOINTERNET ->{
                    toast("No Internet")
                }

                else -> {

                }
            }
        })

        myViewModel.getListData(page)
    }
}