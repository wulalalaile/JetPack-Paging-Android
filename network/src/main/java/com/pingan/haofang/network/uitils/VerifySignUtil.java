package com.pingan.haofang.network.uitils;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * 校验工具类
 */
class VerifySignUtil {

    private static final String TAG = "GenSignUtil";

    /**
     * 签名算法id目前为固定100
     **/
    private final static int SIGNFUNC_ID = 100;
    private static final String API_KEY = "797z3it2mdh44weikz4x513irjq22pu9y292k246";
    private static final String PRIVATE_KEY = "7bae14bab42629ee01e323db934d6a060b60b634";

    public static Map<String, String> getSignatureUrl(Map<String, Object> needSigParams,
                                                      String hostUrl, String url) {
        // 计算sig等
        long time = System.currentTimeMillis() / 1000L;

        //参数签名
        String signature = VerifySignUtil.getSignature(needSigParams,
                PRIVATE_KEY, time, url);

        //apiSequence:api标识，该api在注册时的身份标识, 对域名之后的segment到url结束不包括参数部分做md5校验
        String apiSequence = EncryptUtils.Md5(url);

        Map<String, String> map = new HashMap<>();
        map.put("apiKey", API_KEY);
        map.put("apiSequence", apiSequence);
        map.put("time", time + "");
        map.put("signFuncID", SIGNFUNC_ID + "");
        map.put("signature", signature);

        return map;
    }

    private static String getSignature(Map<String, Object> needSigParams, String privateKey, long time, String url) {
        List<String> keyList = new ArrayList<>();
        //对参数列表进行统一排序，避免服务端解析顺序问题
        for (Entry<String, Object> entry : needSigParams.entrySet()) {
            String key = entry.getKey();

            //跳过签名参数的签名
            if (key.equals("apiKey") || key.equals("apiSequence") || key.equals("signature") || key.equals("signFuncID") || key.equals("time")) {
                continue;
            }
            //跳过空对象的签名
            Object value = entry.getValue();
            if (value == null) {
                continue;
            }
            keyList.add(key);
        }
        Collections.sort(keyList);

        //拼接业务参数参与校验
        StringBuilder sb = new StringBuilder();
        for (String string : keyList) {
            String key = string;
            Object o = needSigParams.get(key);
            string = String.format("\"%s\":\"%s\"", key, o != null ? o.toString().replaceAll("[\r\t\n]", "") : "");

            if (sb.toString().length() == 0) {
                sb.append(string);
            } else {
                sb.append(",").append(string);
            }
        }

        //对业务路径进行校验
        String apiSequence = EncryptUtils.Md5(url);

        //拼接所有校验参数
        String signStr = String.format(
                "data={%s}&time=%s&apiSequence=%s&signFuncID=%s%s",
                sb.toString(), time, apiSequence, SIGNFUNC_ID, privateKey);
        Log.v(TAG, "sign string:" + signStr);

        return EncryptUtils.Md5(signStr);
    }
}
