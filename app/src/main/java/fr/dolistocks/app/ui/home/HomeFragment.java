package fr.dolistocks.app.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import fr.dolistocks.app.ui.workflow.WorkflowFragment;
import fr.dolistocks.app.ui.workflow.PendingFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import fr.dolistocks.app.R;
import fr.dolistocks.app.ui.entry.EntryFragment;
import fr.dolistocks.app.ui.sync.SyncFragment;

/** Accueil du parcours connecté avec les exemples de listes du prototype. */
public final class HomeFragment extends WorkflowFragment {

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
        view.findViewById(R.id.new_list).setOnClickListener(button ->
                getParentFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.page, EntryFragment.class, null)
                        .addToBackStack(null)
                        .commit());
        ((TextView) view.findViewById(R.id.pending_lists)).setText(getString(R.string.queue_count, session.queue.size()));
        view.findViewById(R.id.pending_lists).setOnClickListener(button -> open(PendingFragment.class));
        if (session.latestLabel != null) {
            ((TextView) view.findViewById(R.id.last_list)).setText(session.latestLabel);
            ((TextView) view.findViewById(R.id.last_detail)).setText(getResources().getQuantityString(
                    R.plurals.article_count, session.latestLines, session.latestLines));
        }
        observeNetwork();
    }

    @Override
    protected void networkChanged(boolean connected) {
        ((TextView) requireView().findViewById(R.id.dashboard_title)).setText(
                connected ? R.string.home_title : R.string.offline_home);
        requireView().findViewById(R.id.offline_hint).setVisibility(connected ? View.GONE : View.VISIBLE);
        requireView().findViewById(R.id.refresh_warehouses).setVisibility(connected ? View.VISIBLE : View.GONE);
    }
}
