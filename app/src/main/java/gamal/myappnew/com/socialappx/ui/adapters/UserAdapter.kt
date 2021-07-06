package gamal.myappnew.com.socialappx.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.google.firebase.auth.FirebaseAuth
import gamal.myappnew.com.socialappx.R
import gamal.myappnew.com.socialappx.data.entities.User
import kotlinx.android.synthetic.main.item_post.view.*
import kotlinx.android.synthetic.main.item_user.view.*
import javax.inject.Inject

class UserAdapter @Inject constructor(
    private val glide : RequestManager
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {


    var users : List<User>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    private val diffCallback = object : DiffUtil.ItemCallback<User>() {
        override fun areContentsTheSame(oldItem : User , newItem : User) : Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areItemsTheSame(oldItem : User , newItem : User) : Boolean {
            return oldItem.uid== newItem.uid
        }
    }
    private val differ = AsyncListDiffer(this , diffCallback)

    class UserViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val ivProfilePicture: ImageView = itemView.ivProfileImage
        val tvUsername: TextView = itemView.tvUsername
    }

    override fun onCreateViewHolder(parent : ViewGroup , viewType : Int) : UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_user ,
                parent ,
                false
            )
        )
    }

    override fun getItemCount() : Int {
        return users.size
    }


    override fun onBindViewHolder(holder : UserViewHolder , position : Int) {
        val user = users[position]
        holder.apply {
            glide.load(user.profilePictureUrl).into(ivProfilePicture)
            tvUsername.text = user.username


            itemView.setOnClickListener {
                onUserClickListener?.let { click ->
                    click(user)
                }
            }

            }
        }


    private var onUserClickListener: ((User) -> Unit)? = null


    fun setOnUserClickListener(listener: (User) -> Unit) {
        onUserClickListener = listener
    }


}
