package gamal.myappnew.com.socialappx.data.entities

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import gamal.myappnew.com.socialappx.other.Constants.DEFAULT_PROFILE_PICTURE_URL


@IgnoreExtraProperties
data class User(
    val uid : String = "" ,
    val username : String = "" ,
    val profilePictureUrl : String = DEFAULT_PROFILE_PICTURE_URL ,
    val description : String = "" ,
    var follows : List<String> = listOf() ,
    @get:Exclude  // not load this properties from firestore
    var isFollowing : Boolean = false
)