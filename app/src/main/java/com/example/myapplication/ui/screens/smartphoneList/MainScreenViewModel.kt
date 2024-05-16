package com.example.myapplication.ui.screens.smartphoneList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.entity.SmartphoneDataResponse
import com.example.myapplication.data.entity.SmartphoneDetailDataResponse
import com.example.myapplication.data.repository.SmartphoneRepository
import com.example.myapplication.ui.entity.RequestState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainScreenViewModel(private val repository: SmartphoneRepository) : ViewModel() {
    private val _requestState = MutableStateFlow<RequestState>(RequestState.Idle)
    val requestState: StateFlow<RequestState> = _requestState

    private val _smartphoneDetailState = MutableStateFlow<RequestState>(RequestState.Idle)
    val smartphoneDetailState: StateFlow<RequestState> = _smartphoneDetailState

    private val _smartphones = MutableStateFlow<List<SmartphoneDataResponse.Smartphone>>(emptyList())
    val smartphones: StateFlow<List<SmartphoneDataResponse.Smartphone>> = _smartphones

    private val pageLimit = 15
    private var currentPage = 1
    private var hasMore = true

    init {
        fetchSmartphones()
    }

    fun fetchSmartphones() {
        if (hasMore) {
            if (_requestState.value is RequestState.Idle) {
                _requestState.value = RequestState.Processing
            }
            viewModelScope.launch {
                val response = repository.getSmartphones(currentPage, pageLimit)

                if (response != null) {
                    val list = mutableListOf<SmartphoneDataResponse.Smartphone>()
                    list.addAll(_smartphones.value)
                    _requestState.value = RequestState.Success(response.data)
                    list.addAll(response.data.items)

                    list.mapIndexed { index, smartphone ->
                        if (repository.isFavorite(smartphone.code))
                            list[index] = smartphone.copy(inFavorites = true)
                    }

                    _smartphones.value = list

                    hasMore = response.data.allItemsCount > currentPage * pageLimit
                    currentPage++
                } else {
                    _requestState.value = RequestState.Error("Something wrong")
                }
            }
        }
    }

    fun toggleFavorite(code: String) {
        viewModelScope.launch {

            _smartphones.value = _smartphones.value.map {

                if (it.code == code) {
                    it.copy(inFavorites = !it.inFavorites)
                }
                else it
            }

            val state = _smartphoneDetailState.value
            if (state is RequestState.Success<*> && state.data is SmartphoneDetailDataResponse.SmartphoneDetails) {
                if (state.data.code == code) {
                    _smartphoneDetailState.value = RequestState.Success(state.data.copy(inFavorites = !state.data.inFavorites))
                }
            }

            if (repository.isFavorite(code)) {
                repository.removeFavorite(code)
            } else {
                repository.addFavorite(code)
            }
        }
    }

    fun loadSmartphoneDetail(id: String) {
        _smartphoneDetailState.value = RequestState.Processing
        viewModelScope.launch {
            val response = repository.getSmartphoneDetail(id)
            if (response != null) {
                var item = response.data
                if (repository.isFavorite(id)) {
                    item = item.copy(inFavorites = true)
                }
                _smartphoneDetailState.value = RequestState.Success(item)
            } else {
                _smartphoneDetailState.value = RequestState.Error("Something wrong")
            }
        }
    }
}