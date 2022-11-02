package com.example.leduchoang_2050531200150_baitapcuoiky;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

public class ListStatusActivity extends AppCompatActivity{
    RecyclerView recyclerView;
    StatusAdapter musicAdapter;

    FloatingActionButton floatingActionButton;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        bottomNavigationView=findViewById(R.id.bottom_navigtor);
        bottomNavigationView.setSelectedItemId(R.id.nav_list);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_profile:
                        startActivity(new Intent(getApplicationContext(),MainActivity4.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_list:
                        return true;
                }
                return false;
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Status> options =
                new FirebaseRecyclerOptions.Builder<Status>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Status"), Status.class)
                        .build();

        musicAdapter = new StatusAdapter(options);
        recyclerView.setAdapter(musicAdapter);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        musicAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        musicAdapter.stopListening();
    }

    private void txtSearch(String str) {
        FirebaseRecyclerOptions<Status> options =
                new FirebaseRecyclerOptions.Builder<Status>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Status").orderByChild("name").startAt(str).endAt(str+"~"), Status.class)
                        .build();

        musicAdapter = new StatusAdapter(options);
        musicAdapter.startListening();
        recyclerView.setAdapter(musicAdapter);
    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.search,menu);
        MenuItem item=menu.findItem(R.id.search);
        SearchView searchView=(SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                txtSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                txtSearch(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}