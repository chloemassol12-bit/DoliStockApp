package fr.dolistocks.app.ui.workflow;

import android.app.AlertDialog;
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
            open(ArticlesFragment.class);
        });
        view.findViewById(R.id.delete_lists).setOnClickListener(button ->
                new AlertDialog.Builder(requireContext()).setMessage(R.string.delete_question)
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton(R.string.remove, (dialog, which) -> {
                            session.queue.removeAll(session.selected);
                            session.selected.clear();
                            render();
                        }).show());
        view.findViewById(R.id.send_lists).setOnClickListener(button -> open(ConfirmationFragment.class));
        render();
        observeNetwork();
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
            CheckBox check = new CheckBox(requireContext());
            check.setMinHeight((int) (48 * getResources().getDisplayMetrics().density));
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
