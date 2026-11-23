package fr.dolistocks.app.ui.workflow;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import fr.dolistocks.app.R;
import fr.dolistocks.app.ui.login.LoginFragment;
import fr.dolistocks.app.ui.sync.SyncFragment;

public final class SettingsFragment extends WorkflowFragment {
    public SettingsFragment() { super(R.layout.fragment_settings); }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle state) {
        super.onViewCreated(view, state);
        Spinner depot = view.findViewById(R.id.default_depot);
        List<String> choices = new ArrayList<>();
        choices.add(getString(R.string.no_depot));
        choices.addAll(Arrays.asList(getResources().getStringArray(R.array.entry_warehouses)));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, choices);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        depot.setAdapter(adapter);
        depot.setSelection(defaultDepot());
        depot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> parent) {}
            public void onItemSelected(AdapterView<?> parent, View item, int position, long id) {
                requireContext().getSharedPreferences("tablet", 0).edit()
                        .putInt("default_depot", position).apply();
            }
        });
        view.findViewById(R.id.settings_sync).setOnClickListener(button -> open(SyncFragment.class));
        view.findViewById(R.id.change_account).setOnClickListener(button -> {
            session.selected.clear();
            requireContext().getSharedPreferences("tablet", 0).edit().remove("initialized").apply();
            FragmentManager manager = getParentFragmentManager();
            manager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            manager.beginTransaction().replace(R.id.page, LoginFragment.class, null).commit();
        });
        observeNetwork();
    }

    @Override
    protected void networkChanged(boolean connected) {
        for (int id : new int[]{R.id.account_label, R.id.account_name, R.id.key_label,
                R.id.masked_key, R.id.settings_sync}) {
            requireView().findViewById(id).setVisibility(connected ? View.VISIBLE : View.GONE);
        }
    }
}
