package com.pingan.haofang.network.uitils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.annotations.Nullable;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Util {

    public static Map<String, String> getVerifyedMap(HttpUrl originalUrl, Map<String, Object> parameterMap) {
        return VerifySignUtil.getSignatureUrl(parameterMap, originalUrl.scheme() + "://" + originalUrl.host(), originalUrl.encodedPath());
    }

    public static <T> T checkNotNull(@Nullable T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }

    public static String getNotNullString(String str) {
        return str == null ? "" : str;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 声明上传文件类型
     *
     * @return MultipartBody
     */
    public static MultipartBody filesToMultipartBody(List<File> files) {
        MultipartBody.Builder builder = new MultipartBody.Builder();

        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            // TODO 这里为了简单起见，没有判断file的类型
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
            builder.addFormDataPart("uploadfile" + i, file.getName(), requestBody);
        }
        builder.setType(MultipartBody.FORM);
        return builder.build();
    }

    /**
     * 声明上传文件类型
     *
     * @return MultipartBody.Part
     */
    public static List<MultipartBody.Part> filesToMultipartBodyParts(List<File> files) {
        List<MultipartBody.Part> parts = new ArrayList<>(files.size());
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            // TODO 这里为了简单起见，没有判断file的类型
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("uploadfile" + i, file.getName(), requestBody);
            parts.add(part);
        }
        return parts;
    }
}
