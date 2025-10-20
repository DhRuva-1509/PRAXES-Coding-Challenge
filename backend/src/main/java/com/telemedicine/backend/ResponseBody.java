package com.telemedicine.backend;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseBody {
    private Integer statusCode;
    private Object body;
    private String url;
    private String timestamp;

    public static ResponseBody success(Object data, String url) {
        return ResponseBody.builder()
                .statusCode(200)
                .body(data)
                .url(url)
                .timestamp(getCurrentTimestamp())
                .build();
    }

    public static ResponseBody created(Object data, String url){
        return ResponseBody.builder()
                .statusCode(201)
                .body(data)
                .url(url)
                .timestamp(getCurrentTimestamp())
                .build();
    }

    public static ResponseBody error(Integer statusCode, String message, String url){
        return ResponseBody.builder()
                .statusCode(statusCode)
                .body(message)
                .url(url)
                .timestamp(getCurrentTimestamp())
                .build();
    }

    private static String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
