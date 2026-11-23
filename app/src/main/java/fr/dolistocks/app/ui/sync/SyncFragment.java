package fr.dolistocks.app.ui.sync;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import fr.dolistocks.app.R;
import fr.dolistocks.app.MainActivity;

/** Présente les données nécessaires à la première utilisation hors ligne. */
public final class SyncFragment extends Fragment {

    public SyncFragment() {
        super(R.layout.fragment_sync);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) requireActivity()).connected.observe(getViewLifecycleOwner(),
                connected -> view.findViewById(R.id.sync_now).setEnabled(connected));
        view.findViewById(R.id.sync_now).setOnClickListener(button -> {
            requireContext().getSharedPreferences("tablet", 0).edit().putBoolean("initialized", true).apply();
            getParentFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.page, ReadyFragment.class, null)
                        .addToBackStack(null)
                        .commit();
        });
    }
}
