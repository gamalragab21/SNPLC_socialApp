package gamal.myappnew.com.socialappx.data.entities

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import java.io.Serializable
import java.util.*


@IgnoreExtraProperties
data class  Comment(
    val commentId:String,
    val postId:String,
    val uid:String,
    @get:Exclude
    var username:String,
    @get:Exclude
    var profilePictureUri:String,
    var comment:String,
    val date:Long=System.currentTimeMillis()
){
    constructor() : this("","","","","","",0L)
}
