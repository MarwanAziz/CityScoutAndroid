package net.marwanaziz.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import net.marwanaziz.cityscoutshared.SearchCityResult
import net.marwanaziz.cityscoutshared.SearchCityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchCityScreenView(viewModel: SearchCityViewModel = hiltViewModel<SearchCityScreenHiltViewModel>().searchViewModel, onCitySelectedListener: (SearchCityResult) -> Unit) {
    SearchCityScreenContent(viewModel, onCitySelectedListener)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchCityScreenContent(searchViewModel: SearchCityViewModel,  onCitySelectedListener: (SearchCityResult) -> Unit) {
    val searchResult = searchViewModel.searchCityResult.collectAsState()
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search For City") },
                colors = TopAppBarColors(
                    subtitleContentColor = Color.Transparent,
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent,
                    navigationIconContentColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = Color.Transparent
                )
            )
        },
        containerColor = androidx.compose.ui.graphics.Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SearchView(scope, searchViewModel)
            SearchResultView(searchResult, onCitySelectedListener)
        }
    }
}

@Composable
private fun SearchView(
    scope: CoroutineScope,
    searchViewModel: SearchCityViewModel
) {
    var searchQuery by remember { mutableStateOf("") }
    val error = searchViewModel.searchError.collectAsState()
    val isLoading = searchViewModel.loading.collectAsState()
    OutlinedTextField(
        value = searchQuery,
        onValueChange = {
            searchQuery = it
            scope.launch {
                searchViewModel.searchCity(it)
            }
        },
        modifier = Modifier
            .fillMaxWidth(),
        label = { Text("Search city") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        singleLine = true,
    )
    Spacer(
        modifier = Modifier.padding(vertical = 16.dp)
    )

    if (isLoading.value) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    } else {
        if (error.value.isNotEmpty() && searchQuery.isNotEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    error.value,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(),
                    color = androidx.compose.ui.graphics.Color.Red
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Absolute.Center
                ) {
                    Button(
                        onClick = {
                            scope.launch {
                                searchViewModel.searchCity(searchQuery)
                            }
                        }
                    ) {
                        Text("Retry")
                    }
                }
            }
        }
    }

}

@Composable
private fun SearchResultView(searchResult: State<List<SearchCityResult>>, onCitySelectedListener: (SearchCityResult) -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(searchResult.value) { result ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding()
                    .clickable {
                        onCitySelectedListener(result)
                    },
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        result.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = 8.dp),
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        result.country,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 8.dp),
                        fontWeight = FontWeight.Thin
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun SearchCityScreenViewPreview() {
    SearchCityScreenView(FakeSearchViewModel(), { selectedCity ->
        print("Selected city ${selectedCity.name}")
    })
}

private class FakeSearchViewModel: SearchCityViewModel {
    override val searchCityResult: StateFlow<List<SearchCityResult>>
    override val searchError: StateFlow<String>
    override val loading: StateFlow<Boolean>
    private var _searchCityResult = MutableStateFlow<List<SearchCityResult>>(emptyList())
    private  var _searchError: MutableStateFlow<String> = MutableStateFlow("")
    private  var _loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    constructor() {
        searchCityResult = _searchCityResult
        searchError = _searchError
        loading = _loading
    }

    override suspend fun searchCity(city: String) {
        if (city.count() < 2) {
            _searchError.value = "Preview error test"
            _searchCityResult.value = emptyList()
        } else {
            _searchError.value = ""
            _searchCityResult.value = listOf(
                SearchCityResult(
                    "London",
                    "United Kingdom",
                    0.0,
                    0.0
                ),
                SearchCityResult(
                    "Paris",
                    "France",
                    0.0,
                    0.0
                ),
                SearchCityResult(
                    "Rome",
                    "Italy",
                    0.0,
                    0.0
                )
            )
        }
    }
}