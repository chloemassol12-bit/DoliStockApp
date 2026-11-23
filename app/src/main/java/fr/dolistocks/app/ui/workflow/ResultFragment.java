package fr.dolistocks.app.ui.workflow;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Map;
import fr.dolistocks.app.R;
import fr.dolistocks.app.data.demo.DemoStock.StockList;
import fr.dolistocks.app.ui.home.HomeFragment;

public final class ResultFragment extends WorkflowFragment {
    public ResultFragment() { super(R.layout.fragment_result); }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle state) {
        super.onViewCreated(view, state);
        LinearLayout rows = view.findViewById(R.id.sent_lists);
        for (Map.Entry<StockList, Map<String, String>> result : session.results.entrySet()) {
            TextView header = new TextView(requireContext());
            header.setTextAppearance(requireContext(), android.R.style.TextAppearance_Medium);
            header.setText(result.getKey().heading());
            rows.addView(header);
            for (Map.Entry<String, String> line : result.getValue().entrySet()) {
                TextView detail = new TextView(requireContext());
                detail.setText(line.getKey() + " · " + line.getValue());
                rows.addView(detail);
            }
        }
        view.findViewById(R.id.result_home).setOnClickListener(button -> open(HomeFragment.class));
    }
}
