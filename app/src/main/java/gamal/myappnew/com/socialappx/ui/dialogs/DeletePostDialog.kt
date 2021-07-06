package gamal.myappnew.com.socialappx.ui.dialogs

import androidx.fragment.app.DialogFragment

import android.app.Dialog
import android.os.Bundle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import gamal.myappnew.com.socialappx.R

class DeletePostDialog : DialogFragment() {

    private var positiveListener: (() -> Unit)? = null

    fun setPositiveListener(listener: () -> Unit) {
        positiveListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_post_dialog_title)
            .setMessage(R.string.delete_post_dialog_message)
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton(R.string.delete_post_dialog_positive) { _, _ ->
                positiveListener?.let { click ->
                    click()
                }
            }
            .setNegativeButton(R.string.delete_post_dialog_negative) { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .create()
    }
}