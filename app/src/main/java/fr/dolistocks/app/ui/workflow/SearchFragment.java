package fr.dolistocks.app.ui.workflow;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import fr.dolistocks.app.R;

public final class SearchFragment extends WorkflowFragment {
    public SearchFragment() { super(R.layout.fragment_search); }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle state) {
        super.onViewCreated(view, state);
        if (session.draft == null) {
            open(fr.dolistocks.app.ui.entry.EntryFragment.class);
            return;
        }
        ArticleSearch.bind(view, session, reference -> {
            session.pickedReference = reference;
            getParentFragmentManager().popBackStack();
        });
        view.findViewById(R.id.return_entry).setOnClickListener(button -> getParentFragmentManager().popBackStack());
        observeNetwork();
    }

    @Override
    protected void networkChanged(boolean connected) {
        if (!connected) getParentFragmentManager().popBackStack();
    }
}
