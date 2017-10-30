/*
 * @(#)ApiMessage.class $version 2014. 4. 14.
 *
 * Copyright 2014 Naver Corp. All rights Reserved.
 * NAVER PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.reader.readingManagement.network;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Json 응답 데이터에서 message property에 해당하는 클래스.
 */
@JsonRootName("message")
public class ResponseMessage<T> {
    private T result;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
