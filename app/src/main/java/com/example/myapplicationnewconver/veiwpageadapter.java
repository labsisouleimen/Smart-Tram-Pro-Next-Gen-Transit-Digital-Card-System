package com.example.myapplicationnewconver;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class veiwpageadapter extends FragmentPagerAdapter {
    public veiwpageadapter(FragmentManager fm) {
        super(fm);
    }
    // String username, regNumber, lastname, imageUri,rawData;
   /* public veiwpageadapter(FragmentManager fm,String username, String regNumber, String lastname, String imageUri,String rawData) {
        super(fm);
        this.username = username;
        this.regNumber = regNumber;
        this.lastname = lastname;
        this.imageUri = imageUri;
        this.rawData=rawData;
    }*/


    @Override
    public Fragment getItem(int position) {
        switch (position){

            case 0: return new card();
            case 1:return  new list_station();
            case 2:return  new setting_up();

            default:return new Fragment();
        }

    }

    @Override
    public int getCount() {
        return 3;
    }
}
