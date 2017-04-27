package com.android.scy.pictureclass;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by Administrator on 2017/4/27.
 */

public  class ReplaceFragment {
    private MainActivity main;
    private Fragment fr;
    private int x;
    public ReplaceFragment(MainActivity mainActivity, Fragment fragment,int x) {
        main = mainActivity;
        fr = fragment;
        this.x = x;
    }
    public void load(){
        FragmentManager fragmentManager = main.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.fragment,fr);
        if(x == 0) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }
}
