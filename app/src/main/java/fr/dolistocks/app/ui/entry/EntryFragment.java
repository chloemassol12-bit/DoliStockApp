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
import android.widget.TextView;
import java.util.Arrays;

import fr.dolistocks.app.R;

/** Prépare l’en-tête de la liste avant la saisie des articles. */
public final class EntryFragment extends WorkflowFragment {
    public static final String EDIT_MODE = "edit_mode";

    public EntryFragment() {
        super(R.layout.fragment_entry);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Spinner warehouse = view.findViewById(R.id.entry_warehouse);
        boolean edit = getArguments() != null && getArguments().getBoolean(EDIT_MODE);
        if (edit && (session.editing == null || session.draft == null)) {
            getParentFragmentManager().popBackStack();
            return;
        }
        if (edit) {
            ((TextView) view.findViewById(R.id.entry_heading)).setText(R.string.modify_heading);
            ((TextView) view.findViewById(R.id.add_articles)).setText(R.string.modify_articles);
            view.findViewById(R.id.save_header).setVisibility(View.VISIBLE);
            if (savedInstanceState == null) {
                StockList draft = session.draft;
                ((EditText) view.findViewById(R.id.entry_label)).setText(draft.label);
                ((RadioButton) view.findViewById(draft.replace ? R.id.entry_replace : R.id.entry_add)).setChecked(true);
                warehouse.setSelection(Arrays.asList(getResources().getStringArray(R.array.entry_warehouses))
                        .indexOf(draft.warehouse));
            }
        } else if (savedInstanceState == null && defaultDepot() > 0) {
            warehouse.setSelection(defaultDepot() - 1);
        }
        view.findViewById(R.id.add_articles).setOnClickListener(button -> {
            updateDraft(view, edit);
            open(ArticlesFragment.class);
        });
        view.findViewById(R.id.save_header).setOnClickListener(button -> {
            updateDraft(view, true);
            session.saveDraft();
            getParentFragmentManager().popBackStack();
        });
    }

    private void updateDraft(View view, boolean edit) {
        EditText label = view.findViewById(R.id.entry_label);
        Spinner warehouse = view.findViewById(R.id.entry_warehouse);
        RadioButton replace = view.findViewById(R.id.entry_replace);
        StockList header = new StockList(label.getText().toString(), warehouse.getSelectedItem().toString(),
                replace.isChecked());
        if (edit) {
            header.lines.putAll(session.draft.lines);
            session.draft = header;
        } else {
            session.start(header);
        }
    }
}
