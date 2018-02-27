package com.example.xy.dentist.utils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by XY on 2017/9/25.
 */
public class PartMapUtils {
    public static RequestBody getTextRequestBody(String body) {
        return RequestBody.create(MediaType.parse("text/plan"), body);
    }

    public static RequestBody getImageRequestBody(String filePath) {
        return RequestBody.create(MediaType.parse("image/png"), new File(filePath));
    }
    public static RequestBody getRequestBody(String filePath) {
        return RequestBody.create(MediaType.parse("audio/mp3"), new File(filePath));
    }
}
