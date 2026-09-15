package com.jianghu.offline;

import java.util.LinkedHashMap;
import java.util.Map;

public class Inventory {
    private final Map<String, Integer> items = new LinkedHashMap<>();

    public void add(String id, int count) {
        if (count <= 0) return;
        items.put(id, getCount(id) + count);
    }

    public boolean remove(String id, int count) {
        if (count <= 0 || getCount(id) < count) return false;
        int left = getCount(id) - count;
        if (left == 0) items.remove(id); else items.put(id, left);
        return true;
    }

    public int getCount(String id) { return items.getOrDefault(id, 0); }
    public Map<String, Integer> getItems() { return new LinkedHashMap<>(items); }
    public void clear() { items.clear(); }

    public String toSaveString() {
        StringBuilder out = new StringBuilder();
        for (Map.Entry<String, Integer> e : items.entrySet()) {
            if (out.length() > 0) out.append(';');
            out.append(e.getKey()).append('=').append(e.getValue());
        }
        return out.toString();
    }

    public void loadSaveString(String value) {
        clear();
        if (value == null || value.isEmpty()) return;
        for (String entry : value.split(";")) {
            String[] pair = entry.split("=", 2);
            if (pair.length == 2) {
                try { add(pair[0], Integer.parseInt(pair[1])); }
                catch (NumberFormatException ignored) { }
            }
        }
    }
}
