package fr.dolistocks.app.ui.workflow;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import fr.dolistocks.app.R;
import fr.dolistocks.app.data.demo.DemoStock.StockList;

public final class ConfirmationFragment extends WorkflowFragment {
    public ConfirmationFragment() { super(R.layout.fragment_confirmation); }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle state) {
        super.onViewCreated(view, state);
        LinearLayout rows = view.findViewById(R.id.selected_lists);
        for (StockList list : session.selected) {
            TextView item = (TextView) getLayoutInflater().inflate(R.layout.row_stock_summary, rows, false);
            item.setText(list.heading() + " · " + getString(list.replace ? R.string.mode_replace : R.string.mode_add));
            rows.addView(item);
        }
        view.findViewById(R.id.cancel_send).setOnClickListener(button -> getParentFragmentManager().popBackStack());
        view.findViewById(R.id.confirm_send).setOnClickListener(button -> {
            if (!online() || session.selected.isEmpty()) return;
            button.setEnabled(false);
            simulateSend();
            open(ResultFragment.class);
        });
        observeNetwork();
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

    @Override
    protected void networkChanged(boolean connected) {
        requireView().findViewById(R.id.confirm_send).setEnabled(connected && !session.selected.isEmpty());
    }
}
