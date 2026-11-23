package fr.dolistocks.app.ui.login;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import fr.dolistocks.app.R;
import fr.dolistocks.app.ui.sync.SyncFragment;
import fr.dolistocks.app.MainActivity;
import fr.dolistocks.app.ui.home.HomeFragment;
import android.widget.Toast;

/** Présente le formulaire de première connexion à Dolibarr. */
public final class LoginFragment extends Fragment {

    public LoginFragment() {
        super(R.layout.fragment_login);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.connect).setOnClickListener(button -> {
            boolean online = Boolean.TRUE.equals(((MainActivity) requireActivity()).connected.getValue());
            boolean initialized = requireContext().getSharedPreferences("tablet", 0)
                    .getBoolean("initialized", false);
            if (!online && !initialized) {
                Toast.makeText(requireContext(), R.string.sync_required, Toast.LENGTH_SHORT).show();
                return;
            }
            getParentFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.page, online ? SyncFragment.class : HomeFragment.class, null)
                        .addToBackStack(null)
                        .commit();
        });
    }
}
