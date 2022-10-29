package com.hasanali.kotlincountries.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hasanali.kotlincountries.model.Country
import com.hasanali.kotlincountries.service.CountryDatabase
import kotlinx.coroutines.launch

class CountryViewModel(application: Application): BaseViewModel(application) {

    val countryLiveData = MutableLiveData<Country>()

    fun getDataFromRoom(uuid: Int) {
        launch {
            val dao = CountryDatabase(getApplication()).getDao()
            val country = dao.getCountry(uuid)
            countryLiveData.value = country
        }
    }

}