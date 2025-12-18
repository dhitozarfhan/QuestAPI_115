package com.example.localapi.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localapi.modeldata.DataSiswa
import com.example.localapi.repositori.RepositoriDataSiswa
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface StatusUiSiswa {
    data class Success(
        val siswa: List<DataSiswa> = listOf()
    ) : StatusUiSiswa

    object Error : StatusUiSiswa
    object Loading : StatusUiSiswa
}

class HomeViewModel(
    private val repositoryDataSiswa: RepositoriDataSiswa
) : ViewModel() {

    var listSiswa: StatusUiSiswa by mutableStateOf(StatusUiSiswa.Loading)
        private set

    init {
        loadSiswa()
    }

    fun loadSiswa() {
        viewModelScope.launch {
            listSiswa = StatusUiSiswa.Loading
            try {
                val response = repositoryDataSiswa.getDataSiswa()
                listSiswa = StatusUiSiswa.Success(response)
            } catch (e: Exception) { // Gunakan Exception umum agar SocketTimeout tertangkap
                e.printStackTrace() // Munculkan di log agar kita tahu errornya
                listSiswa = StatusUiSiswa.Error
            }
        }
    }
}