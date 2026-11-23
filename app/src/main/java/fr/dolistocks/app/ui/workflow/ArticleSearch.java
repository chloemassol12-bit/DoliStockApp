package fr.dolistocks.app.ui.workflow;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;
import java.util.Map;

import fr.dolistocks.app.R;
import fr.dolistocks.app.data.demo.DemoStock;

/** Recherche commune au téléphone et au panneau latéral de la tablette. */
final class ArticleSearch {
    interface Selection {
        void select(String reference);
    }

    static void bind(View root, DemoStock session, Selection selection) {
        EditText query = root.findViewById(R.id.query);
        LinearLayout matches = root.findViewById(R.id.matches);
        Runnable filter = () -> {
            String term = query.getText().toString().trim().toLowerCase(Locale.ROOT);
            matches.removeAllViews();
            if (term.length() >= 3) {
                for (Map.Entry<String, String> article : DemoStock.CATALOG.entrySet()) {
                    String key = article.getKey();
                    if (!(key + " " + article.getValue()).toLowerCase(Locale.ROOT).contains(term)) continue;
                    Button choice = new Button(root.getContext());
                    choice.setAllCaps(false);
                    choice.setText(root.getResources().getString(R.string.stock_format, key,
                            article.getValue(), session.stockAt(session.draft.warehouse).get(key)));
                    choice.setOnClickListener(button -> selection.select(key));
                    matches.addView(choice);
                }
            }
            if (matches.getChildCount() == 0) {
                TextView hint = new TextView(root.getContext());
                hint.setText(term.length() < 3 ? R.string.search_min : R.string.no_match);
                matches.addView(hint);
            }
        };
        query.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {}
            public void onTextChanged(CharSequence text, int start, int before, int count) { filter.run(); }
            public void afterTextChanged(Editable value) {}
        });
        filter.run();
    }
}
