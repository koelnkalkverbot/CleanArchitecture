package de.jenswangenheim.cleanarchitecture.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import de.jenswangenheim.cleanarchitecture.SharedPrefsHelper
import de.jenswangenheim.cleanarchitecture.model.DogApiService
import de.jenswangenheim.cleanarchitecture.model.DogBreed
import de.jenswangenheim.cleanarchitecture.model.DogDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class ListViewModel(application: Application) : BaseViewModel(application) {

    private val apiService = DogApiService()
    private val disposable = CompositeDisposable()
    private val prefsHelper = SharedPrefsHelper(getApplication())
    private val refreshTime = 5 * 60 * 1000 * 1000 * 1000L

    val dogs = MutableLiveData<List<DogBreed>>()
    val dogsLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
        val lastUpdateTime = prefsHelper.getLastUpdateTime()
        if (lastUpdateTime != null && lastUpdateTime > 0 && System.nanoTime() - lastUpdateTime < refreshTime) {
            fetchFromDatabase()
        } else {
            fetchFromServer()
        }
    }

    fun forceRefresh() {
        fetchFromServer()
    }

    private fun fetchFromDatabase() {
        loading.value = true
        launch {
            val items = DogDatabase(getApplication()).dogDao().getAllItems()
            dataRetrieved(items)
            Toast.makeText(getApplication(), "Data retrieved from DB", Toast.LENGTH_LONG).show()
        }
    }

    private fun fetchFromServer() {
        loading.value = true
        disposable.add(
            apiService.getDogs().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<DogBreed>>() {
                    override fun onSuccess(dogList: List<DogBreed>) {
                        writeToDatabase(dogList)
                    }

                    override fun onError(error: Throwable) {
                        dogsLoadError.value = true
                        loading.value = false
                        error.printStackTrace()
                    }
                })
        )
    }

    private fun dataRetrieved(dogList: List<DogBreed>) {
        dogs.value = dogList
        dogsLoadError.value = false
        loading.value = false
    }

    private fun writeToDatabase(items: List<DogBreed>) {
        launch {
            val dao = DogDatabase(getApplication()).dogDao()
            dao.deleteAllItems()
            val result = dao.insertAll(*items.toTypedArray())
            var i = 0
            while (i < items.size) {
                items[i].uuid = result[i].toInt()
                ++i
            }
            dataRetrieved(items)
        }
        prefsHelper.saveLastUpdateTime(System.nanoTime())
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}