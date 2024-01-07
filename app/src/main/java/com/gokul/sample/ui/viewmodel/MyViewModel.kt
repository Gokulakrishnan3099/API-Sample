package com.gokul.sample.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gokul.sample.data.api.ApiPlaceHolder
import com.gokul.sample.data.model.ListModel
import com.gokul.sample.data.repository.MainRepository
import com.gokul.sample.utils.Helper.formatPostData
import com.gokul.sample.utils.Helper.getErrorMessage
import com.gokul.sample.utils.NetworkHelper
import com.gokul.sample.utils.Resource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MyViewModel(application: Application): AndroidViewModel(application) {
    private val repository: MainRepository by lazy {
        MainRepository()
    }

    private val apiPlaceHelper: ApiPlaceHolder by lazy {
        repository.apiPlaceHolder
    }

    private val networkHelper: NetworkHelper by lazy {
        NetworkHelper(application)
    }

    private fun <T> exceptionHandler(data: MutableLiveData<Resource<T>>): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, exception ->
            if(exception !is CancellationException){
                Log.e("API ERROR", exception.message.toString())
                data.postValue(Resource.failure(exception.message.toString(), 500, exception.message.toString()))
            }
        }
    }

    private var _listResponse: MutableLiveData<Resource<ListModel>> = MutableLiveData<Resource<ListModel>>()
    public val listResponse : LiveData<Resource<ListModel>> get() = _listResponse

    private var _addResponse: MutableLiveData<Resource<String>> = MutableLiveData<Resource<String>>()
    public val addResponse : LiveData<Resource<String>> get() = _addResponse

    fun getListData(page: Int): Job {
        return viewModelScope.launch(exceptionHandler(_listResponse) + Dispatchers.IO) {
            if(networkHelper.isNetworkConnected()){
                _listResponse.postValue(Resource.loading())
                apiPlaceHelper.getUserList(page.toString()).let {response->
                    if(response.isSuccessful){
                        _listResponse.postValue(Resource.success(response.body(), response.code()))
                    }else{
                        val message = response.errorBody()?.getErrorMessage().toString()
                        _listResponse.postValue( Resource.failure(message,response.code(),response.errorBody()?.string()))
                    }
                }

            }else{
                _listResponse.postValue(Resource.noInternet())
            }
        }
    }

    fun createUser(data: Map<String, Any>) {
        viewModelScope.launch(exceptionHandler(_addResponse) + Dispatchers.IO) {
            if(networkHelper.isNetworkConnected()){
                _addResponse.postValue(Resource.loading())
                apiPlaceHelper.createUser(data.formatPostData()).let {response->
                    if(response.isSuccessful){
                        _addResponse.postValue(Resource.success(response.body(), response.code()))
                    }else{
                        val message = response.errorBody()?.getErrorMessage().toString()
                        _addResponse.postValue( Resource.failure(message,response.code(),response.errorBody()?.string()))
                    }
                }

            }else{
                _addResponse.postValue(Resource.noInternet())
            }
        }
    }



}