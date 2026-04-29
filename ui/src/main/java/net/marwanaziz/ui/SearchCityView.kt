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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.marwanaziz.cityscoutshared.SearchCityResult
import net.marwanaziz.cityscoutshared.SearchCityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchCityView(viewModel: SearchCityViewModel = hiltViewModel<SearchCityHiltViewModel>().searchViewModel, onCitySelectedListener: (SearchCityResult) -> Unit) {
    val searchResults = viewModel.searchCityResult.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.search_for_city_title)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent,
                )
            )
        },
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SearchInputSection(viewModel)
            SearchResultList(searchResults.value, onCitySelectedListener)
        }
    }
}

@Composable
private fun SearchInputSection(searchViewModel: SearchCityViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val error = searchViewModel.searchError.collectAsState()
    val isLoading = searchViewModel.loading.collectAsState()

    LaunchedEffect(searchQuery) {
        if (searchQuery.isBlank()) {
            return@LaunchedEffect
        }
        delay(350)
        searchViewModel.searchCity(searchQuery)
    }

    OutlinedTextField(
        value = searchQuery,
        onValueChange = {
            searchQuery = it
        },
        modifier = Modifier
            .fillMaxWidth(),
        label = { Text(stringResource(R.string.search_city_label)) },
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
        return
    }

    if (error.value.isNotEmpty() && searchQuery.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                error.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(),
                color = Color.Red
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.Center
            ) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            searchViewModel.searchCity(searchQuery)
                        }
                    }
                ) {
                    Text(stringResource(R.string.retry))
                }
            }
        }
    }
}

@Composable
private fun SearchResultList(
    searchResults: List<SearchCityResult>,
    onCitySelectedListener: (SearchCityResult) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(searchResults) { result ->
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