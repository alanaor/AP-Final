package edu.utap.photolist.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.utap.photolist.MainViewModel
import edu.utap.photolist.databinding.StepRowBinding
import edu.utap.photolist.model.BrewStep


class BrewStepAdapter(private val viewModel: MainViewModel, private val fragmentActivity: FragmentActivity?)
    : ListAdapter<BrewStep, BrewStepAdapter.VH>(Diff()) {
    // This class allows the adapter to compute what has changed
    class Diff : DiffUtil.ItemCallback<BrewStep>() {
        override fun areItemsTheSame(oldItem: BrewStep, newItem: BrewStep): Boolean {
            return oldItem.firestoreID == newItem.firestoreID
        }

        override fun areContentsTheSame(oldItem: BrewStep, newItem: BrewStep): Boolean {

            /*
            return oldItem.firestoreID == newItem.firestoreID
                    && oldItem.pictureTitle == newItem.pictureTitle
                    && oldItem.ownerUid == newItem.ownerUid
                    && oldItem.ownerName == newItem.ownerName
                    && oldItem.uuid == newItem.uuid
                    && oldItem.byteSize == newItem.byteSize
                    && oldItem.timeStamp == newItem.timeStamp


             */
            //needs to be updated
            return false
        }
    }

    inner class VH(val rowPostBinding: StepRowBinding)
        : RecyclerView.ViewHolder(rowPostBinding.root){

        // XXX Write me.
        init {
        }
        // NB: This one-liner will exit the current fragment
        // fragmentActivity.supportFragmentManager.popBackStack()
    }

    //EEE // XXX Write me
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        //XXX Write me.
        val binding = StepRowBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: BrewStepAdapter.VH, position: Int) {
        val binding = holder.rowPostBinding
        this.currentList[position].let{
            var step = it
            binding.stepInt.text = (position+1).toString()

            binding.stepTitle.setText(it.Title)
            binding.stepTitle.doAfterTextChanged {
                step.Title = binding.stepTitle.text.toString()
                viewModel.replaceStep(step, position)
            }

            binding.coffeeAdded.setText(it.CoffeeAdded)
            binding.coffeeAdded.doAfterTextChanged {
                step.CoffeeAdded = binding.coffeeAdded.text.toString()
                viewModel.replaceStep(step, position)
            }

            binding.waterAdded.setText(it.WaterAdded)
            binding.waterAdded.doAfterTextChanged {
                step.WaterAdded = binding.waterAdded.text.toString()
                viewModel.replaceStep(step, position)
            }

            binding.time.setText(it.Time)
            binding.time.doAfterTextChanged {
                step.Time = binding.time.text.toString()
                viewModel.replaceStep(step, position)
            }

            binding.temperature.setText(it.Temperature)
            binding.temperature.doAfterTextChanged {
                step.Temperature = binding.temperature.text.toString()
                viewModel.replaceStep(step, position)
            }

            binding.description.setText(it.Description)
            binding.description.doAfterTextChanged {
                step.Description = binding.description.text.toString()
                viewModel.replaceStep(step, position)
            }

        }
    }

}