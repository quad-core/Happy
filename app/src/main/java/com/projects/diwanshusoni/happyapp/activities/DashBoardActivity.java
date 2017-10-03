package com.projects.diwanshusoni.happyapp.activities;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projects.diwanshusoni.happyapp.R;
import com.projects.diwanshusoni.happyapp.adapters.CAdapterViewpagerTasktodo;
import com.projects.diwanshusoni.happyapp.constants.constants;
import com.projects.diwanshusoni.happyapp.pojos.PojoTaskTodo;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashBoardActivity extends AppCompatActivity {

    //debug
    String TAG = "1234";

    private LinearLayout linearLayout;
    private ImageView circleImageView;

    //taskstodo
    private ArrayList<PojoTaskTodo> taskTodoArrayList = new ArrayList<>();
    private boolean isTasksAvail = false;

    private TextView textViewttd_head, textViewttd_createdBy, textViewttd_createdOn, textViewttd_totalSubmissions, textViewttd_averageScore, textViewttd_taskDescription;

    //taskToEval
    private boolean evalTasksAvail = false;
    private TextView textViewtte_head, textViewtte_createdBy, textViewtte_createdOn, textViewtte_totalSubmissions, textViewtte_averageScore;

    private ImageButton btnShowAvailableTasks, btnShowAvailableEvaluations, btnAutoRefresh;

    private int numberOfTasks =0 ;
    private int taskChoice = 1;
    private boolean isTaskAvailable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        link();
        setListeners();

        setScreenWidth();   //get screen size here to adjust the tiles properly ;; maintain ratio

        getAvailableTasks();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setListeners() {
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoardActivity.this, MainTaskUploadActivity.class);
                intent.putExtra("pojoTaskTodo", taskTodoArrayList.get(taskChoice-1));
                startActivity(intent);
            }
        });

        btnAutoRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoYo.with(Techniques.RotateIn)
                        .duration(500)
                        .onStart(new YoYo.AnimatorCallback() {
                            @Override
                            public void call(Animator animator) {
                                if (isTaskAvailable){
                                    taskChoice++;
                                    if (taskChoice > numberOfTasks){
                                        taskChoice=1;
                                    }
                                    setTasksAvailableData(taskTodoArrayList);
                                }
                                else {
                                    Toast.makeText(DashBoardActivity.this, "No tasks avilable try later", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .playOn(btnAutoRefresh);
            }
        });
    }

    private void getAvailableTasks() {
        Log.d(TAG, "getAvailableTasks: in");
        taskTodoArrayList = new ArrayList<>();
        constants.REF_ADDED_TASKS_UID.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numberOfTasks = (int) dataSnapshot.getChildrenCount();
                Log.d(TAG, "onDataChange:1 "+dataSnapshot.getChildrenCount());
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange:2 "+snapshot.getChildrenCount());
                    PojoTaskTodo taskTodo = snapshot.getValue(PojoTaskTodo.class);
                    taskTodo.setTaskUid(snapshot.getKey());
                    taskTodoArrayList.add(taskTodo);
                }
                isTaskAvailable = true;
                setTasksAvailableData(taskTodoArrayList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
               isTaskAvailable = false;
                Toast.makeText(DashBoardActivity.this, "Tasks could not be found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setTasksAvailableData(final ArrayList<PojoTaskTodo> availableData) {
        //yoyo animates things .. https://github.com/daimajia/AndroidViewAnimations
        //onend calls the function to set the data on the views

        if (taskChoice <=0){
            taskChoice=1;
        }
        Log.d(TAG, "task no. "+taskChoice);
        if (isTaskAvailable){
            final PojoTaskTodo pojoTaskTodo = availableData.get(taskChoice-1);
            YoYo.with(Techniques.Shake)
                    .onEnd(new YoYo.AnimatorCallback() {
                        @Override
                        public void call(Animator animator) {
                            textViewttd_head.setText(pojoTaskTodo.getTaskHeading());
                            textViewttd_createdOn.setText(pojoTaskTodo.getDate().split(" ")[1]+" "+availableData.get(0).getDate().split(" ")[2]+" "+availableData.get(0).getDate().split(" ")[5]);
                            textViewttd_createdBy.setText(pojoTaskTodo.getCreatedBy());
                            textViewttd_taskDescription.setText(pojoTaskTodo.getTaskDescription());
                        }
                    })
                    .duration(500)
                    .playOn(linearLayout);
        }
        else {
            Toast.makeText(DashBoardActivity.this, "Tasks could not be found", Toast.LENGTH_SHORT).show();
        }
    }

    private void setScreenWidth() {
        Point point = new Point();
        (getWindowManager().getDefaultDisplay()).getSize(point);
        linearLayout.setMinimumHeight((point.x) /2);
        circleImageView.setMinimumHeight((point.x)/2);
        circleImageView.setMaxHeight((point.x)/2);
    }

    private void link() {
        btnAutoRefresh = (ImageButton) findViewById(R.id.btn_autorefresh_id);
        linearLayout = (LinearLayout) findViewById(R.id.linearlay_taskinfo_tasktodo_dash_id);
        circleImageView = (ImageView) findViewById(R.id.civ_dash_gloabalrank_id);

        textViewttd_averageScore = (TextView) findViewById(R.id.tv_dash_tasktodo_taskaveragescore_id);
        textViewttd_createdBy = (TextView) findViewById(R.id.tv_dash_tasktodo_createdby_id);
        textViewttd_createdOn = (TextView) findViewById(R.id.tv_dash_tasktodo_createdon_id);
        textViewttd_head = (TextView) findViewById(R.id.tv_dash_tasktodo_taskhead_id);
        textViewttd_totalSubmissions = (TextView) findViewById(R.id.tv_dash_tasktodo_tasktotalsubmissions_id);
        textViewttd_taskDescription = (TextView) findViewById(R.id.tv_dash_tasktodo_taskdescription_id);

        textViewtte_averageScore = (TextView) findViewById(R.id.tv_dash_tasktoevaluate_taskaveragescore_id);
        textViewtte_createdBy = (TextView) findViewById(R.id.tv_dash_tasktoevaluate_createdby_id);
        textViewtte_createdOn = (TextView) findViewById(R.id.tv_dash_tasktoevaluate_createdon_id);
        textViewtte_head = (TextView) findViewById(R.id.tv_dash_tasktoevaluate_taskhead_id);
        textViewtte_totalSubmissions = (TextView) findViewById(R.id.tv_dash_tasktoevaluate_tasktotalsubmissions_id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_dashboardui_logout :
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            default:
                Toast.makeText(this, "No Action Found", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
