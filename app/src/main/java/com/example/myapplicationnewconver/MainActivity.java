package com.example.myapplicationnewconver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.mahfa.dnswitch.DayNightSwitch;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    MeowBottomNavigation meowBottomNavigation;
    veiwpageadapter veiwpageadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
  /*    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
            window.setStatusBarColor(Color.TRANSPARENT);  }*/
        getSupportActionBar().hide();
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String regNumber = intent.getStringExtra("regNumber");
        String lastname = intent.getStringExtra("lastname");
        String imageUri = intent.getStringExtra("imageUri");
        String rawdata=intent.getStringExtra("rawData");

            viewPager=findViewById(R.id.vpger);
          //  veiwpageadapter=new veiwpageadapter(getSupportFragmentManager(),username, regNumber, lastname, imageUri,rawdata);
           veiwpageadapter=new veiwpageadapter(getSupportFragmentManager());
            viewPager.setAdapter(veiwpageadapter);
            meowBottomNavigation = findViewById(R.id.mewo);
            meowBottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.account_circle_24));
            meowBottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_baseline_map_24));

        meowBottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.settings_24));
            meowBottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
                @Override
                public void onShowItem(MeowBottomNavigation.Model model) {
                    int posi=model.getId()-1;
                    viewPager.setCurrentItem(posi);
                }
            });
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                   meowBottomNavigation.show(position+1,true);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        meowBottomNavigation.setCount(1,"");
        meowBottomNavigation.setCount(2,"station");
        meowBottomNavigation.setCount(3,"Setting");
        meowBottomNavigation.show(1,true);
        meowBottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model model) {

            }
        });
        meowBottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model model) {

            }
        });
        viewPager.setCurrentItem(0);


    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}