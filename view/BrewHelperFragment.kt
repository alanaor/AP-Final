package edu.utap.photolist.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import edu.utap.photolist.*

import edu.utap.photolist.databinding.FragmentBrewHelperBinding
import edu.utap.photolist.databinding.FragmentTemplateStepListBinding
import edu.utap.photolist.model.BrewTemplate
import java.util.*

class BrewHelperFragment :
    Fragment(R.layout.fragment_brew_helper) {
    companion object {
        fun newInstance(stepNumber: Int) = BrewHelperFragment().apply {
            arguments = Bundle().apply {
                putInt("Step Number", stepNumber)
            }
        }
    }
    private val stepNumberArg = "Step Number"
    private val viewModel: MainViewModel by activityViewModels()
    // https://developer.android.com/topic/libraries/view-binding#fragments
    private var _binding: FragmentBrewHelperBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //private var currentTemplate = viewModel.observeCurrentTemplate().value

    // Touch helpers provide functionality like detecting swipes or moving
    // entries in a recycler view.  Here we do swipe left to delete

    // No need for onCreateView because we passed R.layout to Fragment constructor
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBrewHelperBinding.bind(view)
        Log.d(javaClass.simpleName, "Brew helper frag init")
        Log.d(javaClass.simpleName, "Loading bindings from view model")
        val currentStep = viewModel.observeStepList().value

        viewModel.observeCurrentStep().observe(viewLifecycleOwner){
            try {
                binding.templateTitle.text = currentStep?.get(it)?.Title.toString()
                binding.coffeeaddedText.text = currentStep?.get(it)?.CoffeeAdded.toString()
                binding.wateraddedText.text = currentStep?.get(it)?.WaterAdded.toString()
                binding.timeText.text = currentStep?.get(it)?.Time.toString()
                binding.temperature.text = currentStep?.get(it)?.Temperature.toString()
                binding.descriptionBox.text = currentStep?.get(it)?.Description.toString()
                showCorrectViews(it)

                viewModel.glideFetch(currentStep?.get(it)?.ImageName.toString(), binding.image)
            }
            catch (e: java.lang.Exception){
                Log.d(javaClass.simpleName, "THE STEP BREWER HAS COMPLETED")
                //Throw new fragment here to review brew
                val count: Int = activity?.supportFragmentManager!!.getBackStackEntryCount()
                for (i in 0 until count) {
                    activity?.supportFragmentManager?.popBackStack()
                }
                activity?.supportFragmentManager?.commit {
                    //Insert new "Review Fragment" here
                    add(R.id.mainFragment, ReviewFragment.newInstance())
                    setReorderingAllowed(true)
                }
            }

        }

        //IDEA: Keep step number in view model, then call stepnumber++ to iterate observe function

        //Go back fragment
        binding.backBtn.setOnClickListener(){
            Log.d(javaClass.simpleName,"Going back to last step")
            viewModel.prevStep()
            //activity?.supportFragmentManager?.popBackStackImmediate()
        }

        binding.continueButton.setOnClickListener(){
            Log.d(javaClass.simpleName,"Skipping to next step")
            viewModel.nextStep()
        }

        //Save Data




    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun showCorrectViews(step: Int){
        var currentStep = viewModel.observeStepList().value

        //Coffee Added
        if (currentStep?.get(step)?.CoffeeAdded.toString() ==""){
            binding.coffeeAddedTitle.visibility = View.GONE
            binding.coffeeaddedText.visibility = View.GONE
        }
        else{
            binding.coffeeAddedTitle.visibility = View.VISIBLE
            binding.coffeeaddedText.visibility = View.VISIBLE
        }

        //Water Added
        if (currentStep?.get(step)?.WaterAdded.toString() ==""){
            binding.waterAddedTitle.visibility = View.GONE
            binding.wateraddedText.visibility = View.GONE
        }
        else{
            binding.waterAddedTitle.visibility = View.VISIBLE
            binding.wateraddedText.visibility = View.VISIBLE
        }

        //Time
        if (currentStep?.get(step)?.Time.toString() ==""){
            binding.timeTitle.visibility = View.GONE
            binding.timeText.visibility = View.GONE
        }
        else{
            binding.timeTitle.visibility = View.VISIBLE
            binding.timeText.visibility = View.VISIBLE
        }

        //Temperature
        if (currentStep?.get(step)?.Temperature.toString() ==""){
            binding.temperatureTitle.visibility = View.GONE
            binding.temperature.visibility = View.GONE
        }
        else{
            binding.temperatureTitle.visibility = View.VISIBLE
            binding.temperature.visibility = View.VISIBLE
        }

        //Description
        if (currentStep?.get(step)?.Description.toString() ==""){
            binding.descriptionTitle.visibility = View.GONE
            binding.descriptionEmpty.visibility = View.GONE
            binding.descriptionBox.visibility = View.GONE
        }
        else{
            binding.descriptionTitle.visibility = View.VISIBLE
            binding.descriptionEmpty.visibility = View.VISIBLE
            binding.descriptionBox.visibility = View.VISIBLE
        }

    }

}