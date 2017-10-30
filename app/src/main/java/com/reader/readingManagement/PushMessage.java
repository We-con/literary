package com.reader.readingManagement;

/**
 * Created by naver on 2017. 2. 26..
 */

public class PushMessage {


    public enum Type {
        COMMENT("포스트에 댓글이 달렸습니다"),
        SAME_BOOK("누군가 '어린왕자' 를 읽고 소감을 기록했어요. 확인해보세요."),
        REMIND_1("지칠때 보고 싶다고 했죠??"),
        REMIND_2("나태할때 보고 싶다고 하지 않았나요?");

        private final String msg;

        Type(String s) {
            msg = s;
        }

        public String getMsg() {
            return msg;
        }
    }

    PushMessage(Type type){

    }
}
