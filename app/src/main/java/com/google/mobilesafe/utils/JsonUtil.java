package com.google.mobilesafe.utils;

import android.content.ContentValues;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

public class JsonUtil {
    public static void JSONCombine(JSONObject jSONObject, JSONObject jSONObject2) {
        if (jSONObject != null) {
            try {
                Iterator keys = jSONObject2.keys();
                while (keys.hasNext()) {
                    String str = (String) keys.next();
                    jSONObject.put(str, jSONObject2.get(str));
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    public static String PubInfoToJson(JSONObject jSONObject) {
        if (jSONObject == null) {
            return "";
        }
        try {
            jSONObject.put("id", jSONObject.optString("pubId"));
            jSONObject.put("name", jSONObject.optString("pubName"));
            jSONObject.put("classifyName", jSONObject.optString("pubType"));
            jSONObject.put("weiboName", jSONObject.optString("weiBoName"));
            jSONObject.put("weiboUrl", jSONObject.optString("weiBoUrl"));
            jSONObject.put("weixin", jSONObject.optString("weiXin"));
            jSONObject.put("logo", jSONObject.optString("rectLogoName"));
            jSONObject.put("logoc", jSONObject.optString("circleLogoName"));
            jSONObject.put("website", jSONObject.optString("webSite"));
            JSONArray optJSONArray = jSONObject.optJSONArray("pubNumInfolist");
            if (optJSONArray == null || optJSONArray.length() <= 0) {
                jSONObject.put("pubnum", "");
            } else {
                int length = optJSONArray.length();
                JSONArray jSONArray = new JSONArray();
                for (int i = 0; i < length; i++) {
                    JSONObject jSONObject2 = (JSONObject) optJSONArray.get(i);
                    if ("2".equals(jSONObject2.optString("type"))) {
                        JSONObject jSONObject3 = new JSONObject();
                        jSONObject3.put("purpose", jSONObject2.optString("purpose"));
                        jSONObject3.put("num", jSONObject2.optString("num"));
                        jSONObject3.put("areaCode", jSONObject2.optString("areaCode"));
                        jSONObject3.put("extend", jSONObject2.optString("extend"));
                        jSONArray.put(jSONObject3);
                    }
                }
                jSONObject.put("pubnum", jSONArray);
            }
            jSONObject.remove("pubId");
            jSONObject.remove("pubName");
            jSONObject.remove("pubType");
            jSONObject.remove("pubTypeCode");
            jSONObject.remove("weiXin");
            jSONObject.remove("weiBoName");
            jSONObject.remove("weiBoUrl");
            jSONObject.remove("introduce");
            jSONObject.remove("address");
            jSONObject.remove("faxNum");
            jSONObject.remove("webSite");
            jSONObject.remove("versionCode");
            jSONObject.remove("email");
            jSONObject.remove("parentPubId");
            jSONObject.remove("slogan");
            jSONObject.remove("rectLogoName");
            jSONObject.remove("circleLogoName");
            jSONObject.remove("extend");
            jSONObject.remove("pubNumInfolist");
            jSONObject.remove("loadMenuTime");
            jSONObject.remove("updateInfoTime");
            jSONObject.remove("hasmenu");
            return jSONObject.toString();
        } catch (Throwable th) {
            th.printStackTrace();
            return "";
        }
    }

    public static JSONObject getJsonObject(JSONObject jSONObject, String... strArr) {
        if (strArr == null || jSONObject == null) {
            return null;
        }
        int length = strArr.length;
        if (length == 0 || length % 2 != 0) {
            return null;
        }
        int i = 0;
        while (i < length) {
            try {
                if (!(strArr[i] == null || strArr[i + 1] == null)) {
                    jSONObject.put(strArr[i], strArr[i + 1]);
                }
                i += 2;
            } catch (Throwable e) {
                e.printStackTrace();
                return null;
            }
        }
        return jSONObject;
    }

    public static JSONObject getJsonObject(String... strArr) {
        if (strArr == null) {
            return null;
        }
        int length = strArr.length;
        if (length == 0 || length % 2 != 0) {
            return null;
        }
        try {
            JSONObject jSONObject = new JSONObject();
            int i = 0;
            while (i < length) {
                if (!(strArr[i] == null || strArr[i + 1] == null)) {
                    jSONObject.put(strArr[i], strArr[i + 1]);
                }
                i += 2;
            }
            return jSONObject;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object getValFromJsonObject(JSONObject jSONObject, String str) {
        if (!(str == null || jSONObject == null)) {
            try {
                if (jSONObject.has(str)) {
                    return jSONObject.get(str);
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        return "";
    }

    public static String getValueFromJson(JSONObject jSONObject, String str, String str2) {
        if (jSONObject == null) {
            return str2;
        }
        try {
            String optString = jSONObject.optString(str);
            return StringUtils.isNull(optString) ? str2 : optString;
        } catch (Throwable th) {
            th.printStackTrace();
            return str2;
        }
    }

    public static Object getValueFromJsonObject(JSONObject jSONObject, String str) {
        if (!(str == null || jSONObject == null)) {
            try {
                if (jSONObject.has(str)) {
                    return jSONObject.get(str);
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        return null;
    }

    public static Object getValueWithMap(Map<String, Object> map, String str) {
        if (map != null) {
            try {
                if (!(map.isEmpty() || StringUtils.isNull(str) || !map.containsKey(str))) {
                    return map.get(str);
                }
            } catch (Throwable th) {
            }
        }
        return "";
    }

    public static JSONArray parseStrToJsonArray(String str) {
        if (!StringUtils.isNull(str)) {
            try {
                return new JSONArray(str);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        return null;
    }

    public static JSONObject parseStrToJsonObject(String str) {
        if (!StringUtils.isNull(str)) {
            try {
                return new JSONObject(str.trim());
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        return null;
    }

    public static void putJsonToConV(ContentValues contentValues, JSONObject jSONObject, String str, String str2) {
        String optString = jSONObject.optString(str2);
        if (StringUtils.isNull(optString)) {
            contentValues.remove(str);
        } else {
            contentValues.put(str, optString);
        }
    }

    public static void putJsonToMap(JSONObject jSONObject, Map<String, String> map) {
        if (jSONObject != null && map != null) {
            try {
                Iterator keys = jSONObject.keys();
                while (keys.hasNext()) {
                    String str = (String) keys.next();
                    map.put(str, jSONObject.getString(str));
                }
            } catch (Throwable th) {
            }
        }
    }
}
