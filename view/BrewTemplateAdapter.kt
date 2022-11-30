package edu.utap.photolist.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.utap.photolist.MainViewModel
import edu.utap.photolist.R
import edu.utap.photolist.databinding.RowBinding
import edu.utap.photolist.model.BrewTemplate


class BrewTemplateAdapter(private val viewModel: MainViewModel, private val fragmentActivity: FragmentActivity?, private val fragType: String)
    : ListAdapter<BrewTemplate, BrewTemplateAdapter.VH>(Diff()) {
    // This class allows the adapter to compute what has changed
    class Diff : DiffUtil.ItemCallback<BrewTemplate>() {
        override fun areItemsTheSame(oldItem: BrewTemplate, newItem: BrewTemplate): Boolean {
            return oldItem.firestoreID == newItem.firestoreID
        }

        override fun areContentsTheSame(oldItem: BrewTemplate, newItem: BrewTemplate): Boolean {

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

    inner class VH(private val rowBinding: RowBinding) :
        RecyclerView.ViewHolder(rowBinding.root) {

        fun bind(holder: VH, position: Int) {
            val template = viewModel.getBrewTemplate(position)
            viewModel.glideFetch(template.imageID, rowBinding.rowImageView)
            holder.rowBinding.rowPictureTitle.text = template.title
            //holder.rowBinding.rowSize.text = template.totalTime
            // Note to future me: It might be fun to display the date

            holder.rowBinding.rowPictureTitle.setOnClickListener(){
                //Set on click listener to call fragment change

                //Change fragment to steps frag
                fragmentActivity?.supportFragmentManager?.commit{
                    add(R.id.mainFragment, BrewTemplateStepFragment.newInstance(fragType))
                    Log.d(javaClass.simpleName, "Navigating to step frag")
                    addToBackStack(null)
                    Log.d(javaClass.simpleName, "Adding to back stack")
                }

                //Change viewmodel to view current template
                viewModel.fetchStepsofTemplate(template.firestoreID, "Template")
                viewModel.setCurrentTemplate(template)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val rowBinding = RowBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return VH(rowBinding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(holder, position)
    }
}