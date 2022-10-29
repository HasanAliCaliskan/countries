package com.hasanali.kotlincountries.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.hasanali.kotlincountries.model.Country
import com.hasanali.kotlincountries.service.CountryAPIService
import com.hasanali.kotlincountries.service.CountryDatabase
import com.hasanali.kotlincountries.util.CustomSharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class FeedViewModel(application: Application): BaseViewModel(application) {

    val countries = MutableLiveData<List<Country>>()
    val countryError = MutableLiveData<Boolean>()
    val countryLoading = MutableLiveData<Boolean>()
    private val countryAPIService = CountryAPIService()
    private val compositeDisposable = CompositeDisposable()
    private val customSharedPreferences = CustomSharedPreferences(getApplication())
    private val refreshTime = 10 * 60 * 1000 * 1000 * 1000L

    fun refreshData() {
        val updateTime = customSharedPreferences.getTime()
        if(updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            getDataFromSQLite()
        } else {
            getDataFromAPI()
        }
    }

    fun refreshFromAPI() {
        getDataFromAPI()
    }

    private fun getDataFromSQLite() {
        countryLoading.value = true
        launch {
            val listFromDB = CountryDatabase(getApplication()).getDao().getAllCountries()
            showCountries(listFromDB)
            Toast.makeText(getApplication(),"From SQLite",Toast.LENGTH_SHORT).show()
        }
    }

    private fun getDataFromAPI() {
        countryLoading.value = true
        compositeDisposable.add(countryAPIService.getData()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<List<Country>>(){
                override fun onSuccess(t: List<Country>) {
                    storeInSQLite(t)
                    Toast.makeText(getApplication(),"From API",Toast.LENGTH_SHORT).show()
                }
                override fun onError(e: Throwable) {
                    countryLoading.value = false
                    countryError.value = true
                    e.printStackTrace()
                }
            }))
    }

    private fun storeInSQLite(list: List<Country>) {
        launch {
            val dao = CountryDatabase(getApplication()).getDao()
            dao.deleteAllCountries()
            val listLong = dao.insertAll(*list.toTypedArray())
            var i = 0
            while (i < list.size) {
                list[i].uuid = listLong[i].toInt()
                i += 1
            }
            showCountries(list)
        }
        customSharedPreferences.saveTime(System.nanoTime())
    }

    private fun showCountries(countryList: List<Country>) {
        countries.value = countryList
        countryError.value = false
        countryLoading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}