package edu.utap.photolist.view

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commitNow
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import edu.utap.photolist.*
import edu.utap.photolist.databinding.FragmentHomeBinding
import java.util.*

class HomeFragment :
    Fragment(R.layout.fragment_home) {

    companion object {
        fun newInstance(submit: Boolean) = HomeFragment().apply {
            arguments = Bundle().apply {
                putBoolean("submit", submit)
            }
        }
    }
    private val viewModel: MainViewModel by activityViewModels()
    // https://developer.android.com/topic/libraries/view-binding#fragments
    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val startBrewFragName = "StartBrewFrag"
    private val templatesFragName = "TemplatesFrag"
    private val journalFragName = "JournalFrag"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        val submit = arguments?.getBoolean("submit")


        //Set all button click listeners
        binding.startBrewButton.setOnClickListener{
            activity?.supportFragmentManager?.commit {
                setReorderingAllowed(true)
                add(R.id.mainFragment, BrewTemplateFragment.newInstance(startBrewFragName), startBrewFragName)
                Log.d(javaClass.simpleName, "Navigating to start brew frag")
                addToBackStack(null)

            }
        }
        binding.JournalButton.setOnClickListener{
            activity?.supportFragmentManager?.commit {
                add(R.id.mainFragment, JournalFragment.newInstance(journalFragName), journalFragName)
                Log.d(javaClass.simpleName, "Navigating to Journal frag")
                addToBackStack(null)
            }
        }
        binding.BrewTemplatesButton.setOnClickListener{
            activity?.supportFragmentManager?.commit {
                add(R.id.mainFragment, BrewTemplateFragment.newInstance(templatesFragName), templatesFragName)
                Log.d(javaClass.simpleName, "Navigating to templates frag")
                addToBackStack(null)
            }
        }



        //Toast for if successful
        Log.d(javaClass.simpleName, submit.toString())
        if(submit == true){
            var myToast = Toast.makeText(this.context,"Review successfully submitted to journal!",Toast.LENGTH_SHORT)
            myToast.setGravity(Gravity.BOTTOM,10,10)
            myToast.show()
        }
    }

}