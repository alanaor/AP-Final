package edu.utap.photolist.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

// Firebase insists we have a no argument constructor
data class BrewStep(
    // Auth information
    //Step number
    var Title: String = "",
    var CoffeeAdded : String = "", //In grams
    var WaterAdded : String = "", //In grams
    var Time: String = "", //In seconds
    var Temperature: String = "", //In Fahrenheit
    var Description: String? = "",
    var ImageName: String? = "",

    // firestoreID is generated by firestore, used as primary key
    @DocumentId var firestoreID: String = ""
)
