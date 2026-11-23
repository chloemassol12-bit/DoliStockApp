package fr.dolistocks.app.ui.entry;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import fr.dolistocks.app.ui.workflow.WorkflowFragment;
import fr.dolistocks.app.ui.workflow.ArticlesFragment;
import fr.dolistocks.app.data.demo.DemoStock.StockList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import fr.dolistocks.app.R;

/** Prépare l’en-tête de la liste avant la saisie des articles. */
public final class EntryFragment extends WorkflowFragment {
    public EntryFragment() {
        super(R.layout.fragment_entry);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Spinner warehouse = view.findViewById(R.id.entry_warehouse);
        if (savedInstanceState == null && defaultDepot() > 0) {
            warehouse.setSelection(defaultDepot() - 1);
        }
        view.findViewById(R.id.add_articles).setOnClickListener(button -> {
            EditText label = view.findViewById(R.id.entry_label);
            RadioButton replace = view.findViewById(R.id.entry_replace);
            session.start(new StockList(label.getText().toString(), warehouse.getSelectedItem().toString(),
                    replace.isChecked()));
            open(ArticlesFragment.class);
        });
    }
}
