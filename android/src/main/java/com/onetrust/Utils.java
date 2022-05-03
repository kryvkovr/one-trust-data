package com.rnonetrust;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;

public class Utils {  
  public static WritableMap convertJsonToMap(JSONObject jsonObject) throws JSONException {
    WritableMap map = new WritableNativeMap();
  
    Iterator<String> iterator = jsonObject.keys();
    while (iterator.hasNext()) {
      String key = iterator.next();
      Object value = jsonObject.get(key);
      if (value instanceof JSONObject) {
        map.putMap(key, convertJsonToMap((JSONObject) value));
      } else if (value instanceof JSONArray) {
        map.putArray(key, convertJsonToArray((JSONArray) value));
        if(("option_values").equals(key)) {
          map.putArray("options", convertJsonToArray((JSONArray) value));
        }
      } else if (value instanceof Boolean) {
        map.putBoolean(key, (Boolean) value);
      } else if (value instanceof Integer) {
        map.putInt(key, (Integer) value);
      } else if (value instanceof Double) {
        map.putDouble(key, (Double) value);
      } else if (value instanceof String)  {
        map.putString(key, (String) value);
      } else {
        map.putString(key, value.toString());
      }
    }
    return map;
  }

  public static WritableArray convertJsonToArray(JSONArray jsonArray) throws JSONException {
    WritableArray array = new WritableNativeArray();
  
    for (int i = 0; i < jsonArray.length(); i++) {
      Object value = jsonArray.get(i);
      if (value instanceof JSONObject) {
        array.pushMap(convertJsonToMap((JSONObject) value));
      } else if (value instanceof JSONArray) {
        array.pushArray(convertJsonToArray((JSONArray) value));
      } else if (value instanceof Boolean) {
        array.pushBoolean((Boolean) value);
      } else if (value instanceof Integer) {
        array.pushInt((Integer) value);
      } else if (value instanceof Double) {
        array.pushDouble((Double) value);
      } else if (value instanceof String)  {
        array.pushString((String) value);
      } else {
        array.pushString(value.toString());
      }
    }
    return array;
  }

  public static Object[] toArray(ReadableArray readableArray) {
    Object[] array = new Object[readableArray.size()];

    for (int i = 0; i < readableArray.size(); i++) {
      ReadableType type = readableArray.getType(i);

      switch (type) {
        case Null:
          array[i] = null;
          break;
        case Boolean:
          array[i] = readableArray.getBoolean(i);
          break;
        case Number:
          array[i] = readableArray.getDouble(i);
          break;
        case String:
          array[i] = readableArray.getString(i);
          break;
        case Map:
          array[i] = toMap(readableArray.getMap(i));
          break;
        case Array:
          array[i] = toArray(readableArray.getArray(i));
          break;
      }
    }

    return array;
  }

  public static Map<String, Object> toMap(ReadableMap readableMap) {
    Map<String, Object> map = new HashMap<>();
    ReadableMapKeySetIterator iterator = readableMap.keySetIterator();

    while (iterator.hasNextKey()) {
      String key = iterator.nextKey();
      ReadableType type = readableMap.getType(key);

      switch (type) {
        case Null:
          map.put(key, null);
          break;
        case Boolean:
          map.put(key, readableMap.getBoolean(key));
          break;
        case Number:
          map.put(key, readableMap.getDouble(key));
          break;
        case String:
          map.put(key, readableMap.getString(key));
          break;
        case Map:
          map.put(key, toMap(readableMap.getMap(key)));
          break;
        case Array:
          map.put(key, toArray(readableMap.getArray(key)));
          break;
      }
    }
    return map;
  }
}