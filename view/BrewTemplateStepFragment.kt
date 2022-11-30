package edu.utap.photolist.view

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import edu.utap.photolist.*
import edu.utap.photolist.databinding.FragmentTemplateStepListBinding
import edu.utap.photolist.model.BrewTemplate
import java.util.*

class BrewTemplateStepFragment :
    Fragment(R.layout.fragment_template_step_list) {
    companion object {
        fun newInstance(fragType: String) = BrewTemplateStepFragment().apply {
            arguments = Bundle().apply {
                putString("type", fragType)
            }
        }
    }
    private val startBrewFragName = "StartBrewFrag"
    private val templatesFragName = "TemplatesFrag"
    private val viewModel: MainViewModel by activityViewModels()
    // https://developer.android.com/topic/libraries/view-binding#fragments
    private var _binding: FragmentTemplateStepListBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //private var currentTemplate = viewModel.observeCurrentTemplate().value

    // Touch helpers provide functionality like detecting swipes or moving
    // entries in a recycler view.  Here we do swipe left to delete

    // No need for onCreateView because we passed R.layout to Fragment constructor
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTemplateStepListBinding.bind(view)
        Log.d(javaClass.simpleName, "Brew template steps frag init")
        println(arguments?.getString("type"))
        var frag = arguments?.getString("type")

        // Long press to edit.
        val adapter = BrewStepAdapter(viewModel, activity)

        val rv = binding.photosRV
        val itemDecor = DividerItemDecoration(rv.context, LinearLayoutManager.VERTICAL)
        rv.addItemDecoration(itemDecor)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(rv.context)

        viewModel.observeStepList().observe(viewLifecycleOwner){
            //Add / remove from list when item added
            //Pull photos again based on items added/deleted
            println(it)
            adapter.submitList(it)
            Log.d(javaClass.simpleName, "Step List changed... observing")
            //set title of fragment
            var title = viewModel.observeCurrentTemplate().value?.title ?: "Steps"
            binding.templateTitle.text = title
        }

        //Go back fragment
        binding.backBtn.setOnClickListener(){
            Log.d(javaClass.simpleName, "Popping back stack")
            activity?.supportFragmentManager?.popBackStackImmediate()
        }

        //Save Data

        if (frag == startBrewFragName){
            binding.saveBtn.text = "Start Brew"
            binding.saveBtn.setOnClickListener() {
                Log.d(javaClass.simpleName, "Starting Brew activity")
                viewModel.replaceCurrentStepList()
                viewModel.start()
                activity?.supportFragmentManager?.commit {
                    setReorderingAllowed(true)
                    add(R.id.mainFragment, BrewHelperFragment.newInstance(1), startBrewFragName)
                    Log.d(javaClass.simpleName, "Navigating to step frag")
                    addToBackStack(null)
                }
            }
        }
        if (frag == templatesFragName) {
            binding.saveBtn.setOnClickListener() {
                Log.d(javaClass.simpleName, "Saving Changes")
                var myToast = Toast.makeText(this.context,"Template has been saved, please go back to get to Home Page",
                    Toast.LENGTH_SHORT)
                myToast.setGravity(Gravity.BOTTOM,10,10)
                myToast.show()

                //Replace currentStepList with ChangedStepList
                viewModel.replaceCurrentStepList()
                viewModel.saveTemplate()

                //Save new template meta
            }
        }



    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


}