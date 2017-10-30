package com.reader.readingManagement;

import android.util.Log;

import com.reader.readingManagement.model.Book;

import java.util.ArrayList;

/**
 * Created by loll_ on 2017-05-15.
 */

public class CheerManager {
    static ArrayList<String> cheers;
    public static CheerManager instance = null;

    public static CheerManager getInstance() {
        if (instance == null) {
            instance = new CheerManager();
        }
        return instance;
    }

    //TODO: 나중에 서버연동후 삭제
    CheerManager() {
        cheers = new ArrayList<>();
        cheers.add("얼마나 읽었는지 궁금해요");
        cheers.add("시작이 반!");
        cheers.add("슬슬 책이 재밌어요");
        cheers.add("어떤 내용인지 파악되시나요?");
        cheers.add("곧 있으면 절반이에요");
        cheers.add("벌써 반이나 읽었어요");
        cheers.add("몰입도 최강! 이대로 쭉 읽어볼까요?");
        cheers.add("조금만 더 힘내요!");
        cheers.add("이제 결론만 남았어요!");
        cheers.add("책 읽기 완료! 이 책은 어떠셨나요?");
    }
    public String getCheer(Book book){
        int index = book.getRecentIndexedPage();
        int total = Integer.parseInt(book.getTotalPage());
        int idx = Math.round(((float) index) / ((float) total) * 10);

        Log.d("CHEER", "index : " + String.valueOf(index) + ", total : " + String.valueOf(total) + ", idx : " + String.valueOf(idx));
        return cheers.get(idx);
    }
}
