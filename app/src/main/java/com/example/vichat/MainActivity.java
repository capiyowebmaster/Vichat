package com.example.vichat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.vichat.Fragments.Videos;
import com.example.vichat.Fragments.Vishop;
//import com.example.chatline.Fragments.Friends;
import com.example.vichat.Fragments.Chats;
//import com.example.chatline.Fragments.Videos;
//import com.example.chatline.Models.statusModel;
//import com.example.chatline.Models.users;
import com.example.vichat.Models.statusModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity  implements  NavigationView.OnNavigationItemSelectedListener  {
    public TextView opener;
    public Toolbar toolbar;
    public FirebaseAuth firebaseAuth;
    public FirebaseUser firebaseUser;
    public FirebaseDatabase firebaseDatabase;
    public DrawerLayout drawerLayout;
    public CircularImageView navheaderImageView,toobarImage;
    public TextView navUsername,navEname,toolbarEname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseUser = firebaseAuth.getCurrentUser();
        toobarImage=findViewById(R.id.toolbarImage);
        toolbarEname=findViewById(R.id.toolbarEname);
        toolbar = findViewById(R.id.firstToolbar);
        setSupportActionBar(toolbar);


        drawerLayout = findViewById(R.id.drawerlayout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this,
                drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setDisplayShowTitleEnabled(true);
       // getSupportActionBar().setDisplayShowTitleEnabled(false);
        NavigationView navigationView = findViewById(R.id.navigationview);
        navigationView.setNavigationItemSelectedListener(this);
        View headerview = navigationView.getHeaderView(0);
        navheaderImageView = headerview.findViewById(R.id.navheaderImage);
        navUsername = headerview.findViewById(R.id.navheaderUsername);
        navEname = headerview.findViewById(R.id.navheaderEname);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Status").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                statusModel myModel = snapshot.getValue(statusModel.class);
                assert myModel != null;
                if (myModel.getStatusUrl().equals("statusUrl")){
                    navheaderImageView.setImageResource(R.mipmap.app_icon);
                }
                else {
                    Glide.with(MainActivity.this)
                            .load(myModel.getStatusUrl())
                            .into(navheaderImageView);

                }
                if (myModel.getStatusUrl().equals("statusUrl")){
                    toobarImage.setImageResource(R.mipmap.app_icon);
                }
                else {
                    Glide.with(MainActivity.this)
                            .load(myModel.getStatusUrl())
                            .into(toobarImage);

                }
                toolbarEname.setText(myModel.getEname());

                navUsername.setText(myModel.getUsername());
                navEname.setText(myModel.getEname());


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
        //ViewPager
        TabLayout tabLayout=findViewById(R.id.tabLayout);
        ViewPager viewPager=findViewById(R.id.viewpager);
        ViewpagerAdapter viewpagerAdapter= new ViewpagerAdapter(getSupportFragmentManager());
        viewpagerAdapter.addFragments(new Videos(),"Videos");
        viewpagerAdapter.addFragments(new Chats(),"Chats");
        viewpagerAdapter.addFragments(new Vishop(),"Vishop");

        viewPager.setAdapter(viewpagerAdapter);
        tabLayout.setupWithViewPager(viewPager);









    }
    //internal class for ViewPager Adapter
    public static class  ViewpagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments;
        private  ArrayList <String> titles;
        ViewpagerAdapter(FragmentManager fm){
            super(fm);
            this.fragments=new ArrayList<>();
            this.titles=new ArrayList<>();

        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
        public  void addFragments(Fragment fragment,String title){
            fragments.add(fragment);
            titles.add(title);

        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }



















    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                firebaseAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.postVideo:
                startActivity(new Intent(MainActivity.this, PostVideo.class));
                break;


        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if((drawerLayout) !=null && (drawerLayout.isDrawerOpen(GravityCompat.START))){
            closeDrawer(null);
        }
        else {
            super.onBackPressed();

        }




    }
    public  void closeDrawer(DrawerLayout.DrawerListener listener){
        drawerLayout.setDrawerListener(listener);
        drawerLayout.closeDrawers();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mystatus:
                startActivity(new Intent(MainActivity.this,PostStatus.class));
                break;
            case R.id.settings:
                Toast.makeText(getApplicationContext(), "You want to shop ?", Toast.LENGTH_LONG).show();
                break;
        }
        return  true;


    }
}