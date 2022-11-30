package edu.utap.photolist.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.utap.photolist.*
import edu.utap.photolist.databinding.FragmentTemplateListBinding
import java.util.*

class BrewTemplateFragment :
    Fragment(R.layout.fragment_template_list) {
    companion object {
        fun newInstance(fragType: String) = BrewTemplateFragment().apply {
            arguments = Bundle().apply {
                putString("type", fragType)
            }
        }
    }
    private val startBrewFragName = "StartBrewFrag"
    private val templatesFragName = "TemplatesFrag"
    private val viewModel: MainViewModel by activityViewModels()
    // https://developer.android.com/topic/libraries/view-binding#fragments
    private var _binding: FragmentTemplateListBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    // https://developer.android.com/reference/androidx/recyclerview/widget/RecyclerView.ViewHolder#getBindingAdapterPosition()
    // Getting the position of the selected item is unfortunately complicated
    // This always returns a valid index.
    private fun getPos(holder: RecyclerView.ViewHolder) : Int {
        val pos = holder.bindingAdapterPosition
        // notifyDataSetChanged was called, so position is not known
        if( pos == RecyclerView.NO_POSITION) {
            return holder.absoluteAdapterPosition
        }
        return pos
    }


    // No need for onCreateView because we passed R.layout to Fragment constructor
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTemplateListBinding.bind(view)
        Log.d(javaClass.simpleName, "Brew template frag init")

        viewModel.fetchTemplateList()
        // Long press to edit.
        val adapter = BrewTemplateAdapter(viewModel, activity, arguments?.getString("type") ?: "error")
        val rv = binding.photosRV
        val itemDecor = DividerItemDecoration(rv.context, LinearLayoutManager.VERTICAL)
        rv.addItemDecoration(itemDecor)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(rv.context)


        viewModel.observeTemplateList().observe(viewLifecycleOwner){
            //Add / remove from list when item added
            //Pull photos again based on items added/deleted
            adapter.submitList(it)
        }
        binding.backBtn.setOnClickListener(){
            Log.d(javaClass.simpleName, "Popping back stack")
            activity?.supportFragmentManager?.popBackStackImmediate()
        }






    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    public fun goToSteps(collectionID: String){

    }
}