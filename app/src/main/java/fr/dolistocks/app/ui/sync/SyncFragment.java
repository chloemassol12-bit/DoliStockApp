package fr.dolistocks.app.ui.sync;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import fr.dolistocks.app.R;

/** Présente les données nécessaires à la première utilisation hors ligne. */
public final class SyncFragment extends Fragment {

    public SyncFragment() {
        super(R.layout.fragment_sync);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.sync_now).setOnClickListener(button ->
                Toast.makeText(requireContext(), R.string.sync_preview, Toast.LENGTH_SHORT).show());
    }
}
