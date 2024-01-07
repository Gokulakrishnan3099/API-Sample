package com.gokul.sample.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.gokul.sample.R
import com.gokul.sample.databinding.ActivityCreateBinding
import com.gokul.sample.ui.viewmodel.MyViewModel
import com.gokul.sample.utils.Helper.hideKeyboard
import com.gokul.sample.utils.Helper.toast
import com.gokul.sample.utils.Resource

class CreateActivity : AppCompatActivity() {
    private val binding: ActivityCreateBinding by lazy {
        ActivityCreateBinding.inflate(layoutInflater)
    }

    private val viewModel: MyViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupObserver()
        setupView()

    }

    private fun setupView() {
        binding.apply {
            btnCreate.setOnClickListener {
                it.hideKeyboard()
                when{
                    edtName.text?.trim()?.isEmpty() == true -> toast("Name should not be empty")
                    edtJob.text?.trim()?.isEmpty() == true -> toast("Job should not be empty")
                    else -> {
                        viewModel.createUser(
                            mapOf("name" to edtName.text.toString(),
                                "job" to edtJob.text.toString(),
                            )
                        )
                    }
                }
            }
        }
    }

    private fun setupObserver() {
        viewModel.addResponse.observe(this@CreateActivity, Observer {
            when(it.status){
                Resource.Status.SUCCESS ->{
                   toast("User Created")
                    finish()
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
    }
}