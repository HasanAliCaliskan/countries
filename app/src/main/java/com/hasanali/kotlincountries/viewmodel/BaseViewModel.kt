package com.hasanali.kotlincountries.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    private val job = Job()

    // CoroutineScope'un context'ini (IO, Main, Default, Unconfined) yazıyoruz.
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    // Eğer app kapatılırsa job iptal edilir. (ViewModel override metodudur.)
    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}