package de.jenswangenheim.cleanarchitecture.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import de.jenswangenheim.cleanarchitecture.R
import de.jenswangenheim.cleanarchitecture.databinding.ListItemBinding
import de.jenswangenheim.cleanarchitecture.getCustomProgressDrawable
import de.jenswangenheim.cleanarchitecture.loadImage
import de.jenswangenheim.cleanarchitecture.model.DogBreed
import kotlinx.android.synthetic.main.list_item.view.*

class DogListAdapter(private val dogsList: ArrayList<DogBreed>) :
    RecyclerView.Adapter<DogListAdapter.DogViewHolder>(), ListItemClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ListItemBinding>(inflater, R.layout.list_item, parent, false)
        return DogViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dogsList.size
    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        holder.view.dog = dogsList[position]
        holder.view.listener = this
    }

    fun updateItemsList(newItems: List<DogBreed>) {
        dogsList.clear()
        dogsList.addAll(newItems)
        notifyDataSetChanged()
    }

    class DogViewHolder(var view: ListItemBinding) : RecyclerView.ViewHolder(view.root)

    override fun onListItemClicked(view: View) {
        val uuid = view.dogId.text.toString().toInt()
        val action = ListFragmentDirections.actionDetailFragment()
        action.dogUuid = uuid
        Navigation.findNavController(view).navigate(action)
    }
}