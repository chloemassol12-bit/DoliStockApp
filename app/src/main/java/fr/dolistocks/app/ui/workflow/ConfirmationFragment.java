package fr.dolistocks.app.ui.workflow;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import fr.dolistocks.app.MainActivity;
import fr.dolistocks.app.R;

public final class ConfirmationFragment extends DialogFragment {
    static final String REQUEST = "confirm_selection";
    static final String DELETE = "delete";
    private static final String MESSAGE = "message";

    static ConfirmationFragment create(boolean delete, String message) {
        Bundle args = new Bundle();
        args.putBoolean(DELETE, delete);
        args.putString(MESSAGE, message);
        ConfirmationFragment dialog = new ConfirmationFragment();
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle state) {
        Bundle args = requireArguments();
        boolean delete = args.getBoolean(DELETE);
        return new AlertDialog.Builder(requireContext())
                .setTitle(delete ? R.string.delete_lists : R.string.confirm_title)
                .setMessage(args.getString(MESSAGE))
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(delete ? R.string.remove : R.string.confirm_send,
                        (dialog, which) -> getParentFragmentManager().setFragmentResult(REQUEST, args))
                .create();
    }

    @Override
    public void onCreate(@Nullable Bundle state) {
        super.onCreate(state);
        if (!requireArguments().getBoolean(DELETE)) {
            ((MainActivity) requireActivity()).connected.observe(this, connected -> {
                AlertDialog dialog = (AlertDialog) getDialog();
                if (dialog != null && dialog.getButton(AlertDialog.BUTTON_POSITIVE) != null) {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(connected);
                }
            });
        }
    }
}
