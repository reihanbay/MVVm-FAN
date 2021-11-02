package com.trytolearn.mvvmfan.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.trytolearn.mvvmfan.model.ModelQuotes
import org.json.JSONArray
import org.json.JSONException

class QuotesViewModel: ViewModel() {
    private val modelQuotes = MutableLiveData<ArrayList<ModelQuotes>>()
    private val modelSearch = MutableLiveData<ArrayList<ModelQuotes>>()

    fun setRandomQuotes() {
        val modelQuotesList = ArrayList<ModelQuotes>()
        AndroidNetworking.get("https://animechan.vercel.app/api/quotes")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray) {
                    try {
                        for (i in 0 until response.length()) {
                            val jsonObject = response.getJSONObject(i)
                            val modelQuotes = ModelQuotes()
                            modelQuotes.anime = jsonObject.getString("anime")
                            modelQuotes.character = jsonObject.getString("character")
                            modelQuotes.quote = jsonObject.getString("quote")
                            modelQuotesList.add(modelQuotes)
                        }
                        modelQuotes.postValue(modelQuotesList)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

                override fun onError(anError: ANError) {
                    anError.errorDetail
                }
            })
    }

    fun setSearchQuotes(strName: String, strPage: Int) {
        val modelSearchList = ArrayList<ModelQuotes>()
        //AndroidNetworking.get("https://animechan.vercel.app/api/quotes/anime?title={title}&?page={page}")
        AndroidNetworking.get("https://animechan.vercel.app/api/quotes/character?name={name}&page={page}")
            .addPathParameter("name", strName)
            .addPathParameter("page", strPage.toString())
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray) {
                    try {
                        for (i in 0 until response.length()) {
                            val jsonObject = response.getJSONObject(i)
                            val modelQuotes = ModelQuotes()
                            modelQuotes.anime = jsonObject.getString("anime")
                            modelQuotes.character = jsonObject.getString("character")
                            modelQuotes.quote = jsonObject.getString("quote")
                            modelSearchList.add(modelQuotes)
                        }
                        modelSearch.postValue(modelSearchList)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

                override fun onError(anError: ANError) {
                    anError.errorDetail
                }
            })
    }

    fun getRandomQuotes(): LiveData<ArrayList<ModelQuotes>> = modelQuotes

    fun getSearchQuotes(): LiveData<ArrayList<ModelQuotes>> = modelSearch

}


