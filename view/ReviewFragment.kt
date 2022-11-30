package edu.utap.photolist.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commitNow
import edu.utap.photolist.*
import edu.utap.photolist.databinding.FragmentReviewBinding
import java.util.*

class ReviewFragment :
    Fragment(R.layout.fragment_review) {
    companion object {
        fun newInstance() = ReviewFragment().apply {
        }
    }
    private val viewModel: MainViewModel by activityViewModels()
    // https://developer.android.com/topic/libraries/view-binding#fragments
    private var _binding: FragmentReviewBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // No need for onCreateView because we passed R.layout to Fragment constructor
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentReviewBinding.bind(view)
        Log.d(javaClass.simpleName, "Brew Review frag init")


        //IDEA: Keep step number in view model, then call stepnumber++ to iterate observe function


        binding.doneButton.setOnClickListener(){
            Log.d(javaClass.simpleName,"Saving Review and closing")
            //SAVE REVIEW
            viewModel.saveReview(binding.rating.rating, binding.reviewNotes.text.toString())

            //Pops all stack
            popBackStack()
            //CLOSE TO HOME PAGE
            activity?.supportFragmentManager?.commitNow {
                replace(R.id.mainFragment, HomeFragment.newInstance(true))
            }
        }
        binding.skipButton.setOnClickListener(){
            Log.d(javaClass.simpleName,"Closed Review without saving")
            //Pops all stack
            popBackStack()
            //CLOSE TO HOME PAGE
            activity?.supportFragmentManager?.commitNow {
                replace(R.id.mainFragment, HomeFragment.newInstance(false))
            }
        }

        //Save Data




    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun popBackStack(){
        val count: Int = activity?.supportFragmentManager!!.getBackStackEntryCount()
        for (i in 0 until count) {
            activity?.supportFragmentManager?.popBackStack()
        }
    }


}