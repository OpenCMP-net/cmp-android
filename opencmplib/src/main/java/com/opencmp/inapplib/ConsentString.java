package com.opencmp.inapplib;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class ConsentString {
    public String tcf;
    public String google;
    public String custom;
    public JSONObject meta;

    @NonNull
    @Override
    public String toString() {
        return tcf;
    }

    public static ConsentString fromJson(JSONObject json) throws JSONException {
        ConsentString cs = new ConsentString();
        cs.tcf = json.getString("tcf");
        cs.google = json.getString("google");
        cs.custom = json.getString("custom");
        cs.meta = json.getJSONObject("meta");
        return cs;
    }

    public JSONObject toJson() throws JSONException {
        return new JSONObject() {{
            put("tcf", tcf);
            put("google", google);
            put("custom", custom);
            put("meta", meta);
        }};
    }
}
