package de.jenswangenheim.cleanarchitecture.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.jenswangenheim.cleanarchitecture.model.DogBreed

class DetailViewModel: ViewModel() {

    val dogLiveData = MutableLiveData<DogBreed>()

    fun fetch() {
        val dog = DogBreed("1", "Corgi", "15 years", "group 1", "bredFor", "temperament", "")
        dogLiveData.value = dog
    }
}