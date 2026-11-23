package fr.dolistocks.app.ui.workflow;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import fr.dolistocks.app.ui.entry.EntryFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import fr.dolistocks.app.R;
import fr.dolistocks.app.data.demo.DemoStock.StockList;

public final class PendingFragment extends WorkflowFragment {
    public PendingFragment() { super(R.layout.fragment_pending); }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle state) {
        super.onViewCreated(view, state);
        view.findViewById(R.id.edit_list).setOnClickListener(button -> {
            session.edit(session.selected.iterator().next());
            Bundle args = new Bundle();
            args.putBoolean(EntryFragment.EDIT_MODE, true);
            getParentFragmentManager().beginTransaction().setReorderingAllowed(true)
                    .replace(R.id.page, EntryFragment.class, args).addToBackStack(null).commit();
        });
        getChildFragmentManager().setFragmentResultListener(ConfirmationFragment.REQUEST,
                getViewLifecycleOwner(), (key, result) -> {
                    if (session.selected.isEmpty()) return;
                    if (result.getBoolean(ConfirmationFragment.DELETE)) {
                        session.queue.removeAll(session.selected);
                        session.selected.clear();
                        render();
                    } else if (online()) {
                        simulateSend();
                        open(ResultFragment.class);
                    }
                });
        view.findViewById(R.id.delete_lists).setOnClickListener(button -> confirm(true));
        view.findViewById(R.id.send_lists).setOnClickListener(button -> confirm(false));
        render();
        observeNetwork();
    }

    private void confirm(boolean delete) {
        if (session.selected.isEmpty() || getChildFragmentManager().findFragmentByTag("confirm") != null) return;
        StringBuilder message = new StringBuilder(getString(delete ? R.string.delete_question : R.string.confirm_question));
        for (StockList list : session.selected) {
            message.append("\n\n").append(list.heading());
        }
        ConfirmationFragment.create(delete, message.toString()).show(getChildFragmentManager(), "confirm");
    }

    /** TODO : remplacer les résultats illustratifs par la réponse du module Dolibarr. */
    private void simulateSend() {
        session.results.clear();
        for (StockList list : session.selected) {
            Map<String, String> report = new LinkedHashMap<>();
            Map<String, Integer> levels = session.stockAt(list.warehouse);
            Iterator<Map.Entry<String, Integer>> lines = list.lines.entrySet().iterator();
            while (lines.hasNext()) {
                Map.Entry<String, Integer> line = lines.next();
                String reference = line.getKey();
                Integer before = levels.get(reference);
                if (before == null) {
                    report.put(reference, getString(R.string.sent_error));
                    continue;
                }
                long after = list.replace ? line.getValue() : (long) before + line.getValue();
                if (after > Integer.MAX_VALUE) {
                    report.put(reference, getString(R.string.invalid_qty));
                    continue;
                }
                levels.put(reference, (int) after);
                report.put(reference, getString(R.string.sent_ok) + "\n"
                        + getString(R.string.stock_change, before, after));
                lines.remove();
            }
            session.results.put(list, report);
            if (list.lines.isEmpty()) session.queue.remove(list);
        }
        session.selected.clear();
    }

    private void render() {
        LinearLayout rows = requireView().findViewById(R.id.queued);
        rows.removeAllViews();
        if (session.queue.isEmpty()) {
            TextView empty = new TextView(requireContext());
            empty.setText(R.string.empty_queue);
            rows.addView(empty);
        }
        for (StockList list : session.queue) {
            CheckBox check = (CheckBox) getLayoutInflater().inflate(R.layout.row_select_list, rows, false);
            check.setText(list.heading() + "\n" + getResources().getQuantityString(
                    R.plurals.article_count, list.lines.size(), list.lines.size()));
            check.setChecked(session.selected.contains(list));
            check.setOnCheckedChangeListener((button, selected) -> {
                if (selected) session.selected.add(list);
                else session.selected.remove(list);
                updateActions();
            });
            rows.addView(check);
        }
        updateActions();
    }

    private void updateActions() {
        View view = requireView();
        int count = session.selected.size();
        view.findViewById(R.id.edit_list).setEnabled(count == 1);
        view.findViewById(R.id.delete_lists).setEnabled(count > 0);
        TextView send = view.findViewById(R.id.send_lists);
        send.setEnabled(online() && count > 0);
        send.setText(online() ? getString(R.string.send_count, count) : getString(R.string.offline_send));
    }

    @Override
    protected void networkChanged(boolean connected) {
        ((TextView) requireView().findViewById(R.id.queue_title)).setText(connected
                ? R.string.queue_title : R.string.offline_queue);
        ((TextView) requireView().findViewById(R.id.queue_help)).setText(connected
                ? R.string.queue_help : R.string.offline_send);
        updateActions();
    }
}
