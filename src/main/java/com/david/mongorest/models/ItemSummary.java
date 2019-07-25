package com.david.mongorest.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class ItemSummary {

    private HashMap<Integer, Integer> summary;
    public ItemSummary setSummary(HashMap<Integer, Integer> summary) {
        this.summary = summary;
        return this;
    }
    public HashMap<Integer, Integer> getSummary() {
        return this.summary;
    }

    public ItemSummary() {
        this.summary = new HashMap<>();
    }

    public ItemSummary(HashMap<Integer, Integer> summary) {
        this.summary = summary;
    }

    public ItemSummary loadFromJSON(JSONObject rawJSONObject) {
        JSONArray keys = rawJSONObject.optJSONArray("keys");
        JSONArray values = rawJSONObject.optJSONArray("values");
        HashMap<Integer, Integer> summary = new HashMap<>();
        for (int i = 0; i < keys.length(); i++) {
            int key = keys.getInt(i);
            int value = values.getInt(i);
            summary.put(key, value);
        }
        this.summary = summary;
        return this;
    }

    public JSONObject createJSONObject() {
        JSONObject obj = new JSONObject();
        JSONArray keys = new JSONArray();
        JSONArray values = new JSONArray();
        for (Integer key : this.summary.keySet()) {
            keys.put(key);
            values.put(this.summary.get(key));
        }
        obj.put("keys", keys);
        obj.put("values", values);
        return obj;
    }

    public ItemSummary addItem(int lookupID, int qty) {
        this.summary.put(lookupID, qty);
        return this;
    }
}
