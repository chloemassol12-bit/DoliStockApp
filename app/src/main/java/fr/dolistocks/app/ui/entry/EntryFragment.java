package fr.dolistocks.app.ui.entry;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import fr.dolistocks.app.R;

/** Prépare l’en-tête de la liste avant la saisie des articles. */
public final class EntryFragment extends Fragment {
    public EntryFragment() {
        super(R.layout.fragment_entry);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.home).setOnClickListener(button ->
                getParentFragmentManager().popBackStack());
        View.OnClickListener preview = button -> Toast.makeText(requireContext(),
                R.string.home_destination_preview, Toast.LENGTH_SHORT).show();
        view.findViewById(R.id.lists).setOnClickListener(preview);
        view.findViewById(R.id.settings).setOnClickListener(preview);
        // TODO : transmettre l’entrepôt, le libellé et le mode au futur écran des articles.
        view.findViewById(R.id.add_articles).setOnClickListener(button -> Toast.makeText(
                requireContext(), R.string.entry_preview, Toast.LENGTH_SHORT).show());
    }
}
