package fr.dolistocks.app.ui.workflow;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import fr.dolistocks.app.MainActivity;
import fr.dolistocks.app.R;
import fr.dolistocks.app.data.demo.DemoStock;

/** Navigation commune aux écrans accessibles après la première synchronisation. */
public abstract class WorkflowFragment extends Fragment {
    protected DemoStock session;

    protected WorkflowFragment(int layout) {
        super(layout);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle state) {
        super.onViewCreated(view, state);
        session = new ViewModelProvider(requireActivity()).get(DemoStock.class);
    }

    protected void open(Class<? extends Fragment> screen) {
        getParentFragmentManager().beginTransaction().setReorderingAllowed(true)
                .setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.page, screen, null).addToBackStack(null).commit();
    }

    protected MainActivity host() {
        return (MainActivity) requireActivity();
    }

    protected void observeNetwork() {
        host().connected.observe(getViewLifecycleOwner(), this::networkChanged);
    }

    protected void networkChanged(boolean connected) {
    }

    protected boolean online() {
        return Boolean.TRUE.equals(host().connected.getValue());
    }

    protected int defaultDepot() {
        return requireContext().getSharedPreferences("tablet", 0).getInt("default_depot", 0);
    }
}
