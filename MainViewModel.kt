package edu.utap.photolist

import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.utap.photolist.glide.Glide
import edu.utap.photolist.model.BrewReview
import edu.utap.photolist.model.BrewStep
import edu.utap.photolist.model.BrewTemplate
import java.util.*

class MainViewModel() : ViewModel() {

    //OLD VARIABLES
    // Remember the uuid, and hence file name of file camera will create
    private var pictureUUID: String =""
    // Firestore state
    private val storage = Storage()
    private var firebaseAuthLiveData = FirestoreAuthLiveData()
    // Database access
    private val dbHelp = ViewModelDBHelper()
    // assert does not work
    private lateinit var crashMe: String


    //*********************************************************************
    //NEW VARIABLES
    private var brewTemplateList = MutableLiveData<List<BrewTemplate>>()

    private var journalReviewList = MutableLiveData<List<BrewReview>>()

    private var currentBrewTemplate = MutableLiveData<BrewTemplate>()

    //Temporary step list used to replace current step list if changes saved
    private var changedStepList = MutableLiveData<List<BrewStep>>()
    //Master step list for template
    private var currentStepList = MutableLiveData<List<BrewStep>>()

    private var currentStep = MutableLiveData<Int>()


    //For templates
    fun fetchTemplateList() {
        dbHelp.fetchTemplates(brewTemplateList)
    }

    fun fetchJournalList(){
        dbHelp.fetchJournalReviews(journalReviewList)
    }

    fun getBrewTemplate(position: Int) : BrewTemplate {
        val template = brewTemplateList.value?.get(position)
        return template!!
    }

    fun getBrewReview(position: Int) : BrewReview {
        val review = journalReviewList.value?.get(position)
        return review!!
    }

    fun observeTemplateList(): LiveData<List<BrewTemplate>> {
        return brewTemplateList
    }

    fun observeReviewList(): LiveData<List<BrewReview>> {
        return journalReviewList
    }

    //For individual steps
    fun fetchStepsofTemplate(docID: String, type: String){
        dbHelp.fetchSteps(currentStepList, changedStepList, docID, type)
    }

    fun observeStepList(): LiveData<List<BrewStep>> {
        return currentStepList
    }

    fun observeCurrentStep(): LiveData<Int> {
        return currentStep
    }

    fun setCurrentTemplate(template: BrewTemplate){
        currentBrewTemplate.postValue(template)
    }

    fun observeCurrentTemplate(): LiveData<BrewTemplate>{
        return currentBrewTemplate
    }

    fun replaceCurrentStepList(){
        currentStepList.postValue(changedStepList.value)
    }

    fun saveTemplate(){
        //Replace template meta
        dbHelp.createTemplateMeta(currentBrewTemplate.value!!.firestoreID, currentBrewTemplate.value!!, currentStepList.value!!)
    }


    fun replaceStep(newStep: BrewStep, position: Int){
        var tempList = changedStepList.value?.toMutableList()
        tempList?.set(position, newStep)
        changedStepList.postValue(tempList!!)
        println(changedStepList.value)
    }

    fun nextStep(){
        currentStep.postValue(currentStep.value?.plus(1) ?: 0)
    }

    fun prevStep(){
        currentStep.postValue(currentStep.value?.plus(-1) ?: 0)
    }

    fun start(){
        //Start brew helper
        currentStep.postValue(0)
    }

    fun saveReview(review: Float, notes: String){
        //Save review using dbhelper
        dbHelp.saveReviewToCloud(review, notes, currentBrewTemplate.value!!, currentStepList.value!!)
    }



    /////////////////////////////////////////////////////////////
    fun updateUser() {
        firebaseAuthLiveData.updateUser()
    }

    fun glideFetch(uuid: String, imageView: ImageView) {
        Glide.fetch(storage.uuid2StorageReference(uuid),
            imageView)
    }
}
