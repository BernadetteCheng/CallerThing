package com.example.callerthing;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private ArrayList<Item> mList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createList();
        buildRecyclerView();

        button =findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position = 0;
                insertItem(position);
            }
        });

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(100);
        mRecyclerView.setItemAnimator(itemAnimator);
    }

    public void insertItem(int position){
        mList.add(position, new Item(R.drawable.gradient, "New Description", "New Number"));
        mAdapter.notifyItemInserted(position);
        LinearLayoutManager llm =(LinearLayoutManager) mRecyclerView.getLayoutManager();
        llm.scrollToPosition(0);
    }

    public void createList(){
        mList = new ArrayList<>();
        mList.add(new Item(R.drawable.gradient, "Pizza Pizza", "(416) 967-1111"));
        mList.add(new Item(R.drawable.gradient, "Jennifer Chen", "(416) 321-9797"));
        mList.add(new Item(R.drawable.gradient, "Aayan Berger", "(647) 982-2001"));
        mList.add(new Item(R.drawable.gradient, "Dev Dental", "(416) 292-4333"));
        mList.add(new Item(R.drawable.gradient, "Turkey", "(444) 53-98"));
        mList.add(new Item(R.drawable.gradient, "Billy Oneill", "(905) 754-1020"));
        mList.add(new Item(R.drawable.gradient, "California", "(202) 555-0171"));
        mList.add(new Item(R.drawable.gradient, "University of Waterloo", "(519) 888-4567"));
        mList.add(new Item(R.drawable.gradient, "Toronto", "(437) 862-8864"));

    }

    public void buildRecyclerView(){
        mRecyclerView=findViewById(R.id.backlog);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager=new LinearLayoutManager(this);
        mAdapter=new Adapter(mList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
