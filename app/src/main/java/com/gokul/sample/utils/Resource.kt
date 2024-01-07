package com.gokul.sample.utils

data class Resource<out T>(val status: Status, val data: T?, val message: String?, val responseCode: Int, val errorData: String?) {

    companion object {
        fun <T> loading(): Resource<T> {
            return Resource(Status.LOADING, null, null, 0,null)
        }

        fun <T> success(data: T?, responseCode: Int): Resource<T> {
            return Resource(Status.SUCCESS, data, null, responseCode,null)
        }

        fun <T> failure(msg: String, responseCode: Int, errorData: String?): Resource<T> {
            return Resource(Status.FAILURE, null, msg, responseCode,errorData)
        }

        fun <T> noInternet(): Resource<T> {
            return Resource(Status.NOINTERNET, null, null, 0,null)
        }

    }

    enum class Status {
        LOADING,
        SUCCESS,
        FAILURE,
        NOINTERNET
    }
}