package gamal.myappnew.com.socialappx.ui.main.fragments

import android.graphics.Color
import android.os.Bundle
import android.provider.SyncStateContract
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import dagger.hilt.android.AndroidEntryPoint
import gamal.myappnew.com.socialappx.R
import gamal.myappnew.com.socialappx.data.entities.User
import gamal.myappnew.com.socialappx.other.EventObserver
import gamal.myappnew.com.socialappx.ui.auth.snackbar
import kotlinx.android.synthetic.main.fragment_profile.*


@AndroidEntryPoint
class OtherProfileFragment :ProfileFragment() {

    private val args:OtherProfileFragmentArgs by navArgs()
    override val uid : String
        get() = args.uid
    private var curUser: User?=null

    override fun onViewCreated(view : View , savedInstanceState : Bundle?) {
        super.onViewCreated(view , savedInstanceState)

        subscribeToObserveVers()

        btnToggleFollow.setOnClickListener {
            viewModel.toggleFollowForUser(uid)
        }
    }

    private fun subscribeToObserveVers() {
        viewModel.profileMeta.observe(viewLifecycleOwner, EventObserver{
            btnToggleFollow.isVisible=true
            setUpToggleButtonFollow(it)
            curUser=it
        })
        viewModel.followsStatus.observe(viewLifecycleOwner, EventObserver{
            curUser?.isFollowing=it
            setUpToggleButtonFollow(curUser?:return@EventObserver)
        })
    }
    private fun   setUpToggleButtonFollow(user : User){
        btnToggleFollow.apply {
            val changeBounds=ChangeBounds().apply {
                duration=300
                interpolator=OvershootInterpolator()
            }
            val set1=ConstraintSet()
            val set2=ConstraintSet()

            set1.clone(requireContext(), R.layout.fragment_profile)
            set2.clone(requireContext(), R.layout.fragment_profile_anim)

            TransitionManager.beginDelayedTransition(clProfile,changeBounds)

            if (user.isFollowing){
              text=requireContext().getString(R.string.unfollow)
                setBackgroundColor(Color.RED)
                setTextColor(Color.WHITE)
                set1.applyTo(clProfile)
            }else{
                text=requireContext().getString(R.string.follow)
                setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.colorAccent))
                setTextColor(ContextCompat.getColor(requireContext(),R.color.darkBackground))
                set2.applyTo(clProfile)
            }
        }
    }
}