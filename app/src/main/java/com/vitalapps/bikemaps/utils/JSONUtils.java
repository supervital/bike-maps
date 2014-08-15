package com.vitalapps.bikemaps.utils;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtils {

    private static final int DEFAULT_INT_VALUE = -1;
    private static final String DEFAULT_STRING_VALUE = "null";

    public static String getString(JSONObject jsonObj, String param, String defaultValue) {
        try {
            return jsonObj.getString(param);
        } catch (JSONException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static String getString(JSONArray jsonArray, int index, String defaultValue) {
        try {
            return jsonArray.getString(index);
        } catch (JSONException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static String getString(JSONObject jsonObj, String param) {
        try {
            return jsonObj.getString(param);
        } catch (JSONException e) {
            e.printStackTrace();
            return DEFAULT_STRING_VALUE;
        }
    }


    public static int getInt(JSONObject jsonObj, String param) {
        try {
            return jsonObj.getInt(param);
        } catch (JSONException e) {
            e.printStackTrace();
            return DEFAULT_INT_VALUE;
        }
    }

    public static int getInt(JSONObject jsonObj, String param, int defaultValue) {
        try {
            return jsonObj.getInt(param);
        } catch (JSONException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static long getLong(JSONObject jsonObj, String param) {
        try {
            return jsonObj.getLong(param);
        } catch (JSONException e) {
            e.printStackTrace();
            return DEFAULT_INT_VALUE;
        }
    }

    public static long getLong(JSONObject jsonObj, String param, long defaultValue) {
        try {
            return jsonObj.getLong(param);
        } catch (JSONException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static double getDouble(JSONObject jsonObj, String param, double defaultValue) {
        try {
            return jsonObj.getDouble(param);
        } catch (JSONException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static boolean getBool(JSONObject jsonObj, String param, boolean defaultValue) {
        try {
            return jsonObj.getBoolean(param);
        } catch (JSONException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static JSONObject getJSONObject(JSONObject jsonObj, String param) {
        try {
            return jsonObj.getJSONObject(param);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject getJSONObject(String jsonString) {
        if (jsonString == null) return null;
        try {
            return new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject getJsonObject(JSONArray jsonArray, int index) {
        try {
            return jsonArray.getJSONObject(index);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONArray getJSONArray(JSONObject jsonObj, String param) {
        try {
            return jsonObj.getJSONArray(param);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONArray getJSONArray(String jsonString) {
        if (jsonString == null) return null;
        try {
            return new JSONArray(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
