package com.reader.readingManagement.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.reader.readingManagement.model.Book;
import com.reader.readingManagement.utils.CollectionsUtils;

import io.realm.RealmResults;

public class CardFragmentPagerAdapter extends FragmentPagerAdapter {
    private RealmResults<Book> mBooks;

    public CardFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return CollectionsUtils.isEmpty(mBooks) ? BookFragment.newInstance(new Book(null)) : BookFragment.newInstance(mBooks.get(position));
    }

    @Override
    public int getCount() {
        return CollectionsUtils.isEmpty(mBooks) ? 1 : mBooks.size();
    }


    @Override
    public BookFragment instantiateItem(final ViewGroup container, final int position) {

        BookFragment bookFragment = (BookFragment) super.instantiateItem(container, position);
        if (bookFragment != null) {
            bookFragment.putParcelable(CollectionsUtils.isEmpty(mBooks) ? new Book(null) : mBooks.get(position));
        }
        return bookFragment;
    }

    public void updateList(RealmResults<Book> mybooks) {
        mBooks = mybooks;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
