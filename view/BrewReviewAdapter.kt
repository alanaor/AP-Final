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
import edu.utap.photolist.databinding.JournalRowBinding
import edu.utap.photolist.model.BrewReview


class BrewReviewAdapter(private val viewModel: MainViewModel, private val fragmentActivity: FragmentActivity?, private val fragType: String)
    : ListAdapter<BrewReview, BrewReviewAdapter.VH>(Diff()) {
    // This class allows the adapter to compute what has changed
    class Diff : DiffUtil.ItemCallback<BrewReview>() {
        override fun areItemsTheSame(oldItem: BrewReview, newItem: BrewReview): Boolean {
            return oldItem.firestoreID == newItem.firestoreID
        }

        override fun areContentsTheSame(oldItem: BrewReview, newItem: BrewReview): Boolean {

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

    inner class VH(private val rowBinding: JournalRowBinding) :
        RecyclerView.ViewHolder(rowBinding.root) {

        fun bind(holder: VH, position: Int) {
            val review = viewModel.getBrewReview(position)
            holder.rowBinding.reviewTitle.text = review.Title
            holder.rowBinding.reviewRating.rating = review.ReviewStars.toFloat()
            holder.rowBinding.reviewDate.text = review.DateTime.take(10)
            holder.rowBinding.reviewNotes.text = review.Notes
            //holder.rowBinding.rowSize.text = template.totalTime
            // Note to future me: It might be fun to display the date

            holder.rowBinding.reviewTitle.setOnClickListener(){
                //Set on click listener to call fragment change

                //Change fragment to steps frag
                fragmentActivity?.supportFragmentManager?.commit{
                    add(R.id.mainFragment, BrewTemplateStepFragment.newInstance(fragType))
                    Log.d(javaClass.simpleName, "Navigating to step frag")
                    addToBackStack(null)
                    Log.d(javaClass.simpleName, "Adding to back stack")
                }

                //Change viewmodel to view current template
                viewModel.fetchStepsofTemplate(review.firestoreID, "Journal")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val rowBinding = JournalRowBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return VH(rowBinding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(holder, position)
    }
}