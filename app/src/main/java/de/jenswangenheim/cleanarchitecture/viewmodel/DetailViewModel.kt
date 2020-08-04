package de.jenswangenheim.cleanarchitecture.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import de.jenswangenheim.cleanarchitecture.model.DogBreed
import de.jenswangenheim.cleanarchitecture.model.DogDatabase
import kotlinx.coroutines.launch

class DetailViewModel(application: Application): BaseViewModel(application) {

    val dogLiveData = MutableLiveData<DogBreed>()

    fun fetch(id: Int) {
        launch {
            val dog = DogDatabase(getApplication()).dogDao().getDog(id)
            dataRetrieved(dog)
        }
    }

    private fun dataRetrieved(dogDetail: DogBreed) {
        dogLiveData.value = dogDetail
    }
}