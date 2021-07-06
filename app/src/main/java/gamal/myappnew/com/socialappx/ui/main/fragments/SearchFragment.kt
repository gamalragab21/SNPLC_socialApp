package gamal.myappnew.com.socialappx.ui.main.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import gamal.myappnew.com.socialappx.R
import gamal.myappnew.com.socialappx.other.Constants.SEARCH_TIME_DELAY
import gamal.myappnew.com.socialappx.other.EventObserver
import gamal.myappnew.com.socialappx.ui.adapters.UserAdapter
import gamal.myappnew.com.socialappx.ui.auth.snackbar
import gamal.myappnew.com.socialappx.ui.dialogs.CommentDialogDirections
import gamal.myappnew.com.socialappx.ui.viewmodel.HomeViewModel
import gamal.myappnew.com.socialappx.ui.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {


    private val viewModel: SearchViewModel by viewModels()
    @Inject
    lateinit var userAdapter : UserAdapter
    override fun onViewCreated(view : View , savedInstanceState : Bundle?) {
        super.onViewCreated(view , savedInstanceState)

        setupRecyclerView()
        subscribeToObservers()

        var job : Job? = null
        etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = lifecycleScope.launch {
                delay(SEARCH_TIME_DELAY)
                editable?.let {
                    viewModel.searchUser(it.toString())
                }
            }
        }

        userAdapter.setOnUserClickListener { user ->
                if(FirebaseAuth.getInstance().uid!! == user.uid) {
                    requireActivity().bottomNavigationView.selectedItemId = R.id.profileFragment
                    return@setOnUserClickListener
                }

            findNavController()
                .navigate(
                    SearchFragmentDirections.globalActionOtherFragmentProfile(user.uid)
                )
        }
    }

    private fun subscribeToObservers() {
        viewModel.searchResults.observe(viewLifecycleOwner , EventObserver(
            onError = {
                searchProgressBar.isVisible = false
                snackbar(it)
            } ,
            onLoading = {
                searchProgressBar.isVisible = true
            }
        ) { users ->
            searchProgressBar.isVisible = false
            userAdapter.users = users
        })
    }

    private fun setupRecyclerView() = rvSearchResults.apply {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = userAdapter
        itemAnimator = null
    }

}