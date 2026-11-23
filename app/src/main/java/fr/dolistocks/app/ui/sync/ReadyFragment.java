package fr.dolistocks.app.ui.sync;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import fr.dolistocks.app.R;
import fr.dolistocks.app.ui.home.HomeFragment;

/** Présente la confirmation de synchronisation du parcours de démonstration. */
public final class ReadyFragment extends Fragment {

    public ReadyFragment() {
        super(R.layout.fragment_ready);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // TODO : afficher le nombre d’entrepôts et la date de la dernière synchronisation réussie.
        view.findViewById(R.id.open_home).setOnClickListener(button ->
                getParentFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.page, HomeFragment.class, null)
                        .addToBackStack(null)
                        .commit());
    }
}
