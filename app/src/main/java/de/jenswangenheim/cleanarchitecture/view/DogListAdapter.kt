package de.jenswangenheim.cleanarchitecture.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import de.jenswangenheim.cleanarchitecture.R
import de.jenswangenheim.cleanarchitecture.getCustomProgressDrawable
import de.jenswangenheim.cleanarchitecture.loadImage
import de.jenswangenheim.cleanarchitecture.model.DogBreed
import kotlinx.android.synthetic.main.list_item.view.*

class DogListAdapter(private val dogsList: ArrayList<DogBreed>) :
    RecyclerView.Adapter<DogListAdapter.DogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item, parent, false)
        return DogViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dogsList.size
    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        holder.view.listItemName.text = dogsList[position].dogBreed
        holder.view.listItemDescription.text = dogsList[position].lifeSpan
        holder.view.setOnClickListener {
            Navigation.findNavController(it).navigate(ListFragmentDirections.actionDetailFragment())
        }
        holder.view.listItemImage.loadImage(
            dogsList[position].imageUrl,
            getCustomProgressDrawable(holder.view.context)
        )
    }

    fun updateItemsList(newItems: List<DogBreed>) {
        dogsList.clear()
        dogsList.addAll(newItems)
        notifyDataSetChanged()
    }

    class DogViewHolder(var view: View) : RecyclerView.ViewHolder(view)
}