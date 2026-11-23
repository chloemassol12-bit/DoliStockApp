package fr.dolistocks.app.ui.login;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import fr.dolistocks.app.R;
import fr.dolistocks.app.ui.sync.SyncFragment;

/** Présente le formulaire de première connexion à Dolibarr. */
public final class LoginFragment extends Fragment {

    public LoginFragment() {
        super(R.layout.fragment_login);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.connect).setOnClickListener(button ->
                getParentFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.page, SyncFragment.class, null)
                        .addToBackStack(null)
                        .commit());
    }
}
