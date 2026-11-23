package fr.dolistocks.app.data.demo;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Données de démonstration partagées pendant la session, y compris après rotation. */
public final class DemoStock extends ViewModel {
    public static final Map<String, String> CATALOG = new LinkedHashMap<>();
    static {
        CATALOG.put("ART001", "Vis inox");
        CATALOG.put("ART002", "Gants");
    }

    public static final class StockList {
        public final String label;
        public final String warehouse;
        public final boolean replace;
        public final Map<String, Integer> lines = new LinkedHashMap<>();

        public StockList(String label, String warehouse, boolean replace) {
            this.label = label;
            this.warehouse = warehouse;
            this.replace = replace;
        }

        public String heading() {
            return label + " · " + warehouse;
        }
    }

    public final List<StockList> queue = new ArrayList<>();
    public final Set<StockList> selected = new LinkedHashSet<>();
    public final Map<StockList, Map<String, String>> results = new LinkedHashMap<>();
    private final Map<String, Map<String, Integer>> warehouseStocks = new LinkedHashMap<>();
    public StockList draft;
    public StockList editing;
    public String pickedReference;
    public String latestLabel;
    public int latestLines;

    public DemoStock() {
        StockList first = new StockList("Liste 10/08/2026", "Rodez", false);
        first.lines.put("ART001", 8);
        first.lines.put("ART999", 1);
        StockList second = new StockList("Liste 22/08/2026", "Rodez", false);
        second.lines.put("ART002", 5);
        queue.add(first);
        queue.add(second);
    }

    public Map<String, Integer> stockAt(String warehouse) {
        Map<String, Integer> levels = warehouseStocks.get(warehouse);
        if (levels == null) {
            levels = new LinkedHashMap<>();
            levels.put("ART001", 42);
            levels.put("ART002", 15);
            warehouseStocks.put(warehouse, levels);
        }
        return levels;
    }

    public void start(StockList header) {
        editing = null;
        draft = header;
        pickedReference = null;
    }

    public void edit(StockList original) {
        start(new StockList(original.label, original.warehouse, original.replace));
        editing = original;
        draft.lines.putAll(original.lines);
    }

    public void saveDraft() {
        if (editing != null && queue.contains(editing)) {
            queue.set(queue.indexOf(editing), draft);
            selected.remove(editing);
        } else if (!queue.contains(draft)) {
            queue.add(draft);
        }
        latestLabel = draft.label;
        latestLines = draft.lines.size();
        editing = draft;
    }
}
