package de.jenswangenheim.cleanarchitecture.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.jenswangenheim.cleanarchitecture.model.DogBreed

class ListViewModel: ViewModel() {

    val dogs = MutableLiveData<List<DogBreed>>()
    val dogsLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
        val dog1 = DogBreed("1", "Corgi", "15 years", "group 1", "bredFor", "temperament", "")
        val dog2 = DogBreed("2", "Labrador", "12 years", "group 2", "bredFor", "temperament", "")
        val dog3 = DogBreed("3", "Rottweiler", "25 years", "group 3", "bredFor", "temperament", "")
        val dogList = arrayListOf<DogBreed>(dog1, dog2, dog3)

        dogs.value = dogList
        dogsLoadError.value = false
        loading.value = false
    }
}