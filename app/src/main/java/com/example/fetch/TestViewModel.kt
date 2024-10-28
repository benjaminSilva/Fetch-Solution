package com.example.fetch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fetch.data.FetchData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request

class TestViewModel : ViewModel() {

    private val _data = MutableStateFlow(listOf<FetchData>())
    val data = _data.asStateFlow()

    //This would be done properly in a repository and not hard-coding data here
    fun fetchData() = viewModelScope.launch(Dispatchers.IO) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://fetch-hiring.s3.amazonaws.com/hiring.json")
            .build()
        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            val type = object : TypeToken<List<FetchData>>() {}.type
            val gsonResult = Gson().fromJson<List<FetchData>>(response.body?.string(), type)
            _data.emit(sortedDataButOptimized(gsonResult))
        }
    }

    //This seems simple enough to read. Probably not the most optimized one, but definitely easy to understand.
    private fun sortedData(data: List<FetchData>) =
        mutableListOf<FetchData>().apply {
            data.filter { !it.name.isNullOrBlank() }.groupBy { it.listId }.toSortedMap().forEach { listById ->
                addAll(listById.value.sortedBy { it.id })
            }
        }.toList()

    //A bit more complicated to understand, but definitely with less loops
    private fun sortedDataButOptimized(data: List<FetchData>) =
        mutableListOf<FetchData>().apply {
            val map = mutableMapOf<Int, FetchData>()
            data.forEach {
                if (it.name.isNullOrEmpty())
                    return@forEach
                map[(1000*it.listId) + it.id] = it
            }
            map.toSortedMap().forEach {
                add(it.value)
            }
        }

}