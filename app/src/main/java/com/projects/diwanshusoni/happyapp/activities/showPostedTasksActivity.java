package com.projects.diwanshusoni.happyapp.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.projects.diwanshusoni.happyapp.R;
import com.projects.diwanshusoni.happyapp.constants.constants;
import com.projects.diwanshusoni.happyapp.pojos.PojoSubmitedTask;
import com.projects.diwanshusoni.happyapp.viewHolders.PostviewHolder;

public class showPostedTasksActivity extends AppCompatActivity {


    private RecyclerView mrecycle;
    private DatabaseReference mdata;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_posted_tasks);

        fab = (FloatingActionButton) findViewById(R.id.dash_fab_id);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(showPostedTasksActivity.this, DashBoardActivity.class);
                startActivity(intent);
            }
        });
        mrecycle = (RecyclerView)findViewById(R.id.blog_list);
        mrecycle.setHasFixedSize(true);
        mrecycle.setLayoutManager(new LinearLayoutManager(this));
        mdata= constants.REF_SUBMITTED_TASKS_NODE;
        mdata.keepSynced(true);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        FirebaseRecyclerAdapter<PojoSubmitedTask,PostviewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<PojoSubmitedTask, PostviewHolder>(PojoSubmitedTask.class, R.layout.post, PostviewHolder.class, mdata) {
            @Override
            protected void populateViewHolder(PostviewHolder viewHolder, PojoSubmitedTask model, int position) {
                viewHolder.setData(model);
            }
        };
        mrecycle.setAdapter(recyclerAdapter);
    }
}
