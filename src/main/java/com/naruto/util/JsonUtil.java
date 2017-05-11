package com.naruto.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonParseException;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yue.yuan on 2017/4/14.
 */
public class JsonUtil {

    /**
     * 该字符串可能转为 JSONObject 或 JSONArray
     * @param string
     * @return
     */
    public static boolean mayBeJSON(String string) {
        return ((string != null) && ((("null".equals(string))
                || ((string.startsWith("[")) && (string.endsWith("]"))) || ((string
                .startsWith("{")) && (string.endsWith("}"))))));
    }

    /**
     * 该字符串可能转为JSONObject
     * @param string
     * @return
     */
    public static boolean mayBeJSONObject(String string) {
        return ((string != null) && ((("null".equals(string))
                || ((string.startsWith("{")) && (string.endsWith("}"))))));
    }

    /**
     * 该字符串可能转为 JSONArray
     * @param string
     * @return
     */
    public static boolean mayBeJSONArray(String string) {
        return ((string != null) && ((("null".equals(string))
                || ((string.startsWith("[")) && (string.endsWith("]"))))));
    }

    /**
     *函数注释：parseJSON2Map()<br>
     *
     *用途：该方法用于json数据转换为<Map<String, Object>
     *@param jsonStr
     *@return
     */
    public static Map<String, Object> parseJSON2Map(byte[] jsonStr){
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<String, Object>();
        if (null == jsonStr || jsonStr.length < 1) {
            return null;
        }
        try {
            map = mapper.readValue(jsonStr, 0, jsonStr.length, Map.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     *函数注释：parseJSON2Object()<br>
     *
     *用途：该方法用于json数据转换为Object
     *@param jsonStr
     *@return
     */
    public static Object parseJsonStr2Object(byte[] jsonStr, Class pojoClass){
        if(null!=jsonStr &&jsonStr.length>0 ) {
            return JSON.parseObject(jsonStr, pojoClass, Feature.SupportArrayToBean);
        }
        return null;
    }

    /**
     * Json字符串转Object
     * @param jsonStr
     * @param pojoClass
     * @return
     */
    public static Object parseJsonStr2Object(String jsonStr,Class pojoClass){
        if (StringUtils.isEmpty(jsonStr)) {
            return null;
        }
        return JSON.parseObject(jsonStr,pojoClass);
    }

}
