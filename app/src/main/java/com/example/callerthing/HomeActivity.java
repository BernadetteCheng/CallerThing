package com.example.callerthing;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.ContactsContract;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private ArrayList<Item> mList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton button;

    public ServiceReceiver serviceReceiver;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String[] permissions = {Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_PHONE_STATE};
        List<String> permsNeeded = new ArrayList<String>();
        for (String s : permissions) {
            if (ContextCompat.checkSelfPermission(this, s) != PackageManager.PERMISSION_GRANTED) {
                permsNeeded.add(s);
            }
        }
        if (permsNeeded.size() > 0) {
            ActivityCompat.requestPermissions(this, permsNeeded.toArray(new String[permsNeeded.size()]), 99);
        }

        if (Settings.canDrawOverlays(this)) {
            //launchMainService();
        } else {
            checkDrawOverlayPermission();
        }

        serviceReceiver = new ServiceReceiver();

        createList();
        buildRecyclerView();

        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = 0;
                insertItem(position);
            }
        });
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(100);
        mRecyclerView.setItemAnimator(itemAnimator);
      
        FloatingActionButton showOverlay = findViewById(R.id.showOverlay);
        showOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchMainService();
            }
        });
    }

    private void launchMainService() {
        Intent svc = new Intent(this, MainService.class);
        svc.putExtra("number", "2551954495");
        stopService(svc);
        startService(svc);        
    }

    public void insertItem(int position) {
        mList.add(position, new Item(R.mipmap.five, "New Description", "New Number"));
        mAdapter.notifyItemInserted(position);
        LinearLayoutManager llm = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        llm.scrollToPosition(0);
    }

    public void createList() {
        mList = new ArrayList<>();
        mList.add(new Item(R.mipmap.pizza, "Pizza Pizza", "(416) 967-1111"));
        mList.add(new Item(R.mipmap.sushi, "Jennifer Chen", "(416) 321-9797"));
        mList.add(new Item(R.mipmap.dog, "Aayan Berger", "(647) 982-2001"));
        mList.add(new Item(R.mipmap.dental, "Dev Dental", "(416) 292-4333"));
        mList.add(new Item(R.mipmap.one, "Turkey", "(444) 53-98"));
        mList.add(new Item(R.mipmap.two, "Unknown", "(905) 754-1020"));
        mList.add(new Item(R.mipmap.three, "California", "(202) 555-0171"));
        mList.add(new Item(R.mipmap.uw, "University of Waterloo", "(519) 888-4567"));
        mList.add(new Item(R.mipmap.four, "Toronto", "(437) 862-8864"));
    }
  
    public final static int REQUEST_CODE = 10101;
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkDrawOverlayPermission() {

        // Checks if app already has permission to draw overlays
        if (!Settings.canDrawOverlays(this)) {

            // If not, form up an Intent to launch the permission request
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));

            // Launch Intent, with the su
          
          
         pplied request code
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {

        // Check if a request code is received that matches that which we provided for the overlay draw request
        if (requestCode == REQUEST_CODE) {

            // Double-check that the user granted it, and didn't just dismiss the request
            if (Settings.canDrawOverlays(this)) {

            }
            else {

                Toast.makeText(this, "Sorry. Can't draw overlays without permission...", Toast.LENGTH_SHORT).show();
            }
        } 
    }
    public void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.backlog);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new Adapter(mList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
