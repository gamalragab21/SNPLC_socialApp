package gamal.myappnew.com.socialappx.ui.main.fragments

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import gamal.myappnew.com.socialappx.R
import gamal.myappnew.com.socialappx.ui.viewmodel.AuthViewModel
import gamal.myappnew.com.socialappx.ui.viewmodel.BasePostViewModel
import gamal.myappnew.com.socialappx.ui.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*


@AndroidEntryPoint
class HomeFragment :BasePostFragment(R.layout.fragment_home) {

    override val postProgressBar : ProgressBar
        get() = allPostsProgressBar

    override val basePostViewModel : BasePostViewModel
        get() {
           val  vm :HomeViewModel by viewModels()
            return vm
        }

    override fun onViewCreated(view : View , savedInstanceState : Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() =rvAllPosts.apply {
        adapter=postAdapter
        itemAnimator=null
        layoutManager=LinearLayoutManager(requireContext())
    }

}