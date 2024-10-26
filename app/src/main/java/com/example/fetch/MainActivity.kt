package com.example.fetch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fetch.data.FetchData
import com.example.fetch.ui.theme.FetchTheme

class MainActivity : ComponentActivity() {
    private val viewModel: TestViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchData()
        setContent {
            FetchTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
                    val data by viewModel.data.collectAsState()
                    DataList(data, Modifier.padding(padding))
                }
            }
        }
    }
}

@Composable
fun DataList(data: List<FetchData>, modifier: Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(data) {
            Row {
                Text(text = it.listId.toString())
                Spacer(modifier = Modifier.padding(end = 8.dp))
                Text(text = it.id.toString())
                Spacer(modifier = Modifier.padding(end = 8.dp))
                Text(text = it.name.toString())
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FetchTheme {
        DataList(data = generatedFakeData(), modifier = Modifier)
    }
}

fun generatedFakeData() = listOf(
    FetchData(2,7,"Test"),
    FetchData(4,8,"Test"),
    FetchData(5,5,"Test"),
    FetchData(6,3,"Test"),
    FetchData(2,3,"Test"),
)