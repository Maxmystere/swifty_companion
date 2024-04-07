package tech.maxdevcoveries.swiftycompanion.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tech.maxdevcoveries.swiftycompanion.databinding.UserProjectItemBinding
import tech.maxdevcoveries.swiftycompanion.lib.UserProject

class UserProjectAdapter(private val dataset: List<UserProject>) : RecyclerView.Adapter<UserProjectAdapter.UserProjectViewHolder>(){

    class UserProjectViewHolder(private val binding: UserProjectItemBinding) : RecyclerView.ViewHolder(binding.root.rootView) {

        fun updateView(item: UserProject)
        {
            binding.textProjectMark.text =  item.finalMark?.toString()
            binding.textProjectName.text = item.project.name
            binding.textProjectStatus.text = item.status
            binding.radioIsValidated.isChecked = item.isValidated == true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserProjectViewHolder {
        val binding = UserProjectItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return UserProjectViewHolder(binding)
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: UserProjectViewHolder, position: Int) {
        holder.updateView(dataset[position])
    }
}