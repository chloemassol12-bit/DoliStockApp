package fr.dolistocks.app.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import fr.dolistocks.app.R;
import fr.dolistocks.app.ui.entry.EntryFragment;
import fr.dolistocks.app.ui.sync.SyncFragment;

/** Accueil du parcours connecté avec les exemples de listes du prototype. */
public final class HomeFragment extends Fragment {

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.refresh_warehouses).setOnClickListener(button ->
                getParentFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.page, SyncFragment.class, null)
                        .addToBackStack(null)
                        .commit());
        // TODO : relier les destinations à leurs fragments à mesure de leur livraison.
        View.OnClickListener showPreview = button ->
                Toast.makeText(requireContext(), R.string.home_destination_preview, Toast.LENGTH_SHORT).show();
        view.findViewById(R.id.home).setEnabled(false);
        view.findViewById(R.id.new_list).setOnClickListener(button ->
                getParentFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.page, EntryFragment.class, null)
                        .addToBackStack(null)
                        .commit());
        view.findViewById(R.id.pending_lists).setOnClickListener(showPreview);
        view.findViewById(R.id.lists).setOnClickListener(showPreview);
        view.findViewById(R.id.settings).setOnClickListener(showPreview);
    }
}
