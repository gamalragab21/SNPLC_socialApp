package gamal.myappnew.com.socialappx.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import gamal.myappnew.com.socialappx.R
import gamal.myappnew.com.socialappx.ui.adapters.UserAdapter

class LikedByDialog constructor(
    private val userAdapter : UserAdapter
):DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val rvLikedBy = RecyclerView(requireContext()).apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.liked_by_dialog_title)
            .setView(rvLikedBy)
            .create()
    }
}