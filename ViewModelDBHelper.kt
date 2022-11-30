package edu.utap.photolist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.FieldValue
import edu.utap.photolist.model.BrewReview
import edu.utap.photolist.model.BrewStep
import edu.utap.photolist.model.BrewTemplate
import java.time.Instant
import java.util.*
import java.time.format.DateTimeFormatter


class ViewModelDBHelper() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val rootCollection = "BrewTemplates"
    private val reviewCollection = "Reviews"
    private val stepsCollection = "Steps"

    fun fetchTemplates(templatesList: MutableLiveData<List<BrewTemplate>>){
        Log.d(javaClass.simpleName, "Fetching templates from Firestore")
        dbFetchTemplateMeta(templatesList)
    }

    fun fetchSteps(stepList: MutableLiveData<List<BrewStep>>, changedStepList: MutableLiveData<List<BrewStep>>, docID: String, type: String){
        Log.d(javaClass.simpleName, "Fetching templates from Firestore")
        if (type == "Template") {
            dbFetchStepMeta(stepList, changedStepList, docID, rootCollection)
        }
        else if (type == "Journal"){
            dbFetchStepMeta(stepList, changedStepList, docID, reviewCollection)
        }
    }

    fun fetchJournalReviews(journalList: MutableLiveData<List<BrewReview>>){
        Log.d(javaClass.simpleName, "Fetching reviews from Firestore")
        dbFetchReviewMeta(journalList)
    }

    private fun dbFetchTemplateMeta(templatesList: MutableLiveData<List<BrewTemplate>>) {
        // XXX Write me and use limitAndGet
        //Deal with ascending/descending
        //Call to database
        //limitAndGet(db.collection(rootCollection), notesList)
        QueryTemplatesAndGet(
            db.collection(rootCollection),
            templatesList)
        //.orderBy(sortInfo.sortColumn.toString(), sortMethod), notesList)
    }
    private fun dbFetchReviewMeta(journalList: MutableLiveData<List<BrewReview>>) {
        // XXX Write me and use limitAndGet
        //Deal with ascending/descending
        //Call to database
        //limitAndGet(db.collection(rootCollection), notesList)
        QueryReviewsAndGet(
            db.collection(reviewCollection),
            journalList)
        //.orderBy(sortInfo.sortColumn.toString(), sortMethod), notesList)
    }

    private fun dbFetchStepMeta(stepList: MutableLiveData<List<BrewStep>>, changedStepList: MutableLiveData<List<BrewStep>>, docID: String, rootCollectionID: String) {
        // XXX Write me and use limitAndGet
        //Deal with ascending/descending
        //Call to database
        //limitAndGet(db.collection(rootCollection), notesList)
        QueryStepsAndGet(
            db.collection(rootCollectionID).document(docID).collection(stepsCollection),
            stepList, changedStepList)
        //.orderBy(sortInfo.sortColumn.toString(), sortMethod), notesList)
    }

    private fun QueryTemplatesAndGet(query: Query,
                            templatesList: MutableLiveData<List<BrewTemplate>>){
        Log.d(javaClass.simpleName, "Brew Template query executed ")
        query
            .limit(100)
            .get()
            .addOnSuccessListener { result ->
                Log.d(javaClass.simpleName, "allNotes fetch ${result!!.documents.size}")
                // NB: This is done on a background thread
                templatesList.postValue(result.documents.mapNotNull {
                    Log.d(javaClass.simpleName, "Documents successfully queried")
                    it.toObject(BrewTemplate::class.java)
                })
            }
            .addOnFailureListener {
                Log.d(javaClass.simpleName, "allNotes fetch FAILED ", it)
            }
    }

    private fun QueryReviewsAndGet(query: Query,
                                     journalList: MutableLiveData<List<BrewReview>>){
        Log.d(javaClass.simpleName, "Brew Review query executed ")
        query
            .limit(100)
            .get()
            .addOnSuccessListener { result ->
                Log.d(javaClass.simpleName, "Brew reviews fetched ${result!!.documents.size}")
                // NB: This is done on a background thread
                journalList.postValue(result.documents.mapNotNull {
                    Log.d(javaClass.simpleName, "Documents successfully queried")
                    it.toObject(BrewReview::class.java)
                })
            }
            .addOnFailureListener {
                Log.d(javaClass.simpleName, "allNotes fetch FAILED ", it)
            }
    }
    private fun QueryStepsAndGet(query: Query,
                                 stepList: MutableLiveData<List<BrewStep>>,
                                 changedStepList: MutableLiveData<List<BrewStep>>){
        Log.d(javaClass.simpleName, "Brew Steps query executed ")
        query
            .limit(100)
            .get()
            .addOnSuccessListener { result ->
                Log.d(javaClass.simpleName, "Steps fetch ${result!!.documents.size}")
                // NB: This is done on a background thread
                stepList.postValue(result.documents.mapNotNull {
                    Log.d(javaClass.simpleName, "Steps successfully queried")
                    it.toObject(BrewStep::class.java)
                })
                changedStepList.postValue(result.documents.mapNotNull {
                    Log.d(javaClass.simpleName, "Steps successfully queried")
                    it.toObject(BrewStep::class.java)
                })
            }
            .addOnFailureListener {
                Log.d(javaClass.simpleName, "Steps fetch FAILED ", it)
            }
    }

    fun createTemplateMeta(
        docID: String,
        brewTemplate: BrewTemplate,
        brewSteps: List<BrewStep>
    ) {
        // You can get a document id if you need it.
        //photoMeta.firestoreID = db.collection(rootCollection).document().id
        // XXX Write me: add photoMeta
        val data = hashMapOf(
            "imageID"      to brewTemplate.imageID,
            "title"      to brewTemplate.title,
            "totalTime"     to brewTemplate.totalTime,
        )
        var template = db.collection(rootCollection).document(docID)
        var templateID = template.id
        template.set(data).addOnSuccessListener {
            Log.d(
                javaClass.simpleName,
                "Template meta creation successful"
            )

            //Iterate through and create steps
            var itr = brewSteps!!.iterator()
            var i = 1
            while(itr.hasNext()){
                createTemplateStep(itr.next(), templateID, i, rootCollection)
                i+=1
            }
            //Log.d(javaClass.simpleName, "Calling fetch list from Firestore")
            //dbFetchPhotoMeta(sortInfo, notesList)
        }
            .addOnFailureListener { e ->
                Log.d(javaClass.simpleName, "Template metadata creation failed")
                Log.w(javaClass.simpleName, "Error ", e)
            }

    }

    fun createTemplateStep(
        brewStep: BrewStep,
        templateID: String,
        stepID: Int,
        collection: String
    ) {
        val data = hashMapOf(
            "Title"           to brewStep.Title,
            "CoffeeAdded"     to brewStep.CoffeeAdded,
            "WaterAdded"      to brewStep.WaterAdded,
            "Time"            to brewStep.Time,
            "Temperature"     to brewStep.Temperature,
            "Description"     to brewStep.Description,
        )
        Log.d(javaClass.simpleName, "Creating step")
        println(templateID)
        db.collection(collection).document(templateID).collection(stepsCollection).document(stepID.toString())
            .set(data).addOnSuccessListener {
            Log.d(
                javaClass.simpleName,
                "Step meta creation successful"
            )
            //Log.d(javaClass.simpleName, "Calling fetch list from Firestore")
            //dbFetchPhotoMeta(sortInfo, notesList)
        }
            .addOnFailureListener { e ->
                Log.d(javaClass.simpleName, "Step metadata creation failed")
                Log.w(javaClass.simpleName, "Error ", e)
            }
    }

    fun saveReviewToCloud(review: Float, notes: String, brewTemplate: BrewTemplate, brewSteps: List<BrewStep>) {
        //Save the upper review details
        val data = hashMapOf(
            "ReviewStars" to review,
            "Title" to brewTemplate.title,
            "Notes" to notes,
            "DateTime" to DateTimeFormatter.ISO_INSTANT.format(Instant.now())

        )

        db.collection(reviewCollection).add(data)
            .addOnSuccessListener {
                Log.d(
                    javaClass.simpleName,
                    "Template meta creation successful"
                )
                //Iterate through and create steps
                var itr = brewSteps!!.iterator()
                var i = 1
                while(itr.hasNext()){
                    createTemplateStep(itr.next(), it.id, i, reviewCollection)
                    i+=1
                }
            }
            .addOnFailureListener { e ->
                Log.d(javaClass.simpleName, "Template metadata creation failed")
                Log.w(javaClass.simpleName, "Error ", e)
            }
    }

}