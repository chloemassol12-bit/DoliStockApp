package fr.dolistocks.app.ui.workflow;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Locale;
import java.util.Map;

import fr.dolistocks.app.R;
import fr.dolistocks.app.data.demo.DemoStock;
import fr.dolistocks.app.ui.entry.EntryFragment;

public final class ArticlesFragment extends WorkflowFragment {
    public ArticlesFragment() { super(R.layout.fragment_articles); }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle state) {
        super.onViewCreated(view, state);
        if (session.draft == null) {
            open(EntryFragment.class);
            return;
        }
        TextView heading = view.findViewById(R.id.draft_heading);
        heading.setText(session.draft.heading() + " · " + getString(session.draft.replace
                ? R.string.mode_replace : R.string.mode_add));
        EditText reference = view.findViewById(R.id.reference);
        EditText amount = view.findViewById(R.id.quantity);
        TextWatcher update = new TextWatcher() {
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {}
            public void onTextChanged(CharSequence text, int start, int before, int count) { updatePreview(); }
            public void afterTextChanged(Editable text) {}
        };
        reference.addTextChangedListener(update);
        amount.addTextChangedListener(update);
        View panel = view.findViewById(R.id.search_panel);
        if (panel != null) {
            panel.findViewById(R.id.return_entry).setVisibility(View.GONE);
            ArticleSearch.bind(panel, session, this::pick);
        }
        view.findViewById(R.id.search_articles).setOnClickListener(button -> open(SearchFragment.class));
        view.findViewById(R.id.add_line).setOnClickListener(button -> addLine());
        view.findViewById(R.id.save_list).setOnClickListener(button -> {
            if (session.draft.lines.isEmpty()) {
                Toast.makeText(requireContext(), R.string.empty_list, Toast.LENGTH_SHORT).show();
                return;
            }
            session.saveDraft();
            open(PendingFragment.class);
        });
        renderLines();
        observeNetwork();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (session.pickedReference != null) {
            pick(session.pickedReference);
            session.pickedReference = null;
        }
    }

    private void pick(String reference) {
        ((EditText) requireView().findViewById(R.id.reference)).setText(reference);
        requireView().findViewById(R.id.quantity).requestFocus();
    }

    private String reference() {
        EditText input = requireView().findViewById(R.id.reference);
        return input.getText().toString().trim().toUpperCase(Locale.ROOT);
    }

    private void updatePreview() {
        View root = requireView();
        TextView details = root.findViewById(R.id.article_details);
        String key = reference();
        boolean known = DemoStock.CATALOG.containsKey(key);
        int value = positiveQuantity(root.findViewById(R.id.quantity));
        boolean valid = value > 0 && !key.isEmpty() && (!online() || known);
        String preview = "";
        if (online() && known) {
            int before = session.stockAt(session.draft.warehouse).get(key);
            preview = getString(R.string.stock_format, key, DemoStock.CATALOG.get(key), before);
            if (value > 0) {
                long after = session.draft.replace ? value : (long) before + value;
                valid = after <= Integer.MAX_VALUE;
                preview += "\n" + (valid ? getString(R.string.projected_stock, after)
                        : getString(R.string.stock_limit));
            }
        } else if (online() && !key.isEmpty() && !isReferencePrefix(key)) {
            preview = getString(R.string.unknown_ref);
        }
        details.setVisibility(preview.isEmpty() ? View.GONE : View.VISIBLE);
        if (!preview.contentEquals(details.getText())) details.setText(preview);
        root.findViewById(R.id.add_line).setEnabled(valid);
    }

    private boolean isReferencePrefix(String prefix) {
        for (String key : DemoStock.CATALOG.keySet()) {
            if (key.startsWith(prefix)) return true;
        }
        return false;
    }

    private int positiveQuantity(EditText input) {
        try {
            int value = Integer.parseInt(input.getText().toString());
            if (value > 0) return value;
        } catch (NumberFormatException ignored) {
            // Une saisie vide ou trop grande reste modifiable.
        }
        return 0;
    }

    private int quantity(EditText input) {
        int value = positiveQuantity(input);
        if (value == 0) input.setError(getString(R.string.invalid_qty));
        return value;
    }

    private void addLine() {
        EditText input = requireView().findViewById(R.id.reference);
        String key = reference();
        if (key.isEmpty() || (online() && !DemoStock.CATALOG.containsKey(key))) {
            input.setError(getString(key.isEmpty() ? R.string.required_ref : R.string.unknown_ref));
            return;
        }
        EditText amount = requireView().findViewById(R.id.quantity);
        int value = quantity(amount);
        if (value == 0) return;
        session.draft.lines.put(key, value);
        input.setText("");
        amount.setText("");
        input.requestFocus();
        renderLines();
    }

    private void renderLines() {
        LinearLayout rows = requireView().findViewById(R.id.lines);
        requireView().findViewById(R.id.save_list).setEnabled(!session.draft.lines.isEmpty());
        rows.removeAllViews();
        for (Map.Entry<String, Integer> line : session.draft.lines.entrySet()) {
            Button edit = new Button(requireContext());
            edit.setAllCaps(false);
            edit.setText(getString(R.string.line_format, line.getKey(), line.getValue()));
            edit.setOnClickListener(button -> editQuantity(line.getKey()));
            rows.addView(edit);
        }
    }

    private void editQuantity(String key) {
        EditText amount = new EditText(requireContext());
        amount.setInputType(InputType.TYPE_CLASS_NUMBER);
        amount.setText(String.valueOf(session.draft.lines.get(key)));
        AlertDialog dialog = new AlertDialog.Builder(requireContext()).setTitle(R.string.edit_quantity)
                .setView(amount).setPositiveButton(R.string.save, null)
                .setNegativeButton(android.R.string.cancel, null)
                .setNeutralButton(R.string.remove, (current, which) -> {
                    session.draft.lines.remove(key);
                    renderLines();
                }).create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(button -> {
            int value = quantity(amount);
            if (value > 0) {
                session.draft.lines.put(key, value);
                renderLines();
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void networkChanged(boolean connected) {
        View root = requireView();
        ((TextView) root.findViewById(R.id.article_title)).setText(connected ? R.string.article_title : R.string.offline_entry);
        ((TextView) root.findViewById(R.id.stock_note)).setText(connected ? R.string.stock_note : R.string.offline_stock);
        View panel = root.findViewById(R.id.search_panel);
        if (panel != null) panel.setVisibility(connected ? View.VISIBLE : View.GONE);
        root.findViewById(R.id.search_articles).setVisibility(connected && panel == null ? View.VISIBLE : View.GONE);
        updatePreview();
    }
}
