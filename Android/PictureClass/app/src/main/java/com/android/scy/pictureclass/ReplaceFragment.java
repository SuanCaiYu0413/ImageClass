package com.android.scy.pictureclass;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

/**
 * Created by Administrator on 2017/4/27.
 */

public  class ReplaceFragment {
    private MainActivity main;
    private Fragment fr;
    private int x;
    final public static int END = 0;
    final public static int YUXU = 1;
    final public static int DELLOGIN = 2;
    public ReplaceFragment(MainActivity mainActivity, Fragment fragment,int x) {
        main = mainActivity;
        fr = fragment;
        this.x = x;
    }
    public void load(){
        FragmentManager fragmentManager = main.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if(x == DELLOGIN){
            ;Fragment oldFr = fragmentManager.findFragmentById(R.id.fragment);
            if(oldFr != null){
                fragmentManager.popBackStack(null,1);
            }
        }else if(x == END) {
            transaction.addToBackStack(null);
        }
        transaction.replace(R.id.fragment,fr);
        transaction.commit();
    }
}
