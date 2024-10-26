package com.example.fetch.data

import kotlinx.serialization.Serializable

@Serializable
data class FetchData(val id: Int, val listId: Int, val name: String?)