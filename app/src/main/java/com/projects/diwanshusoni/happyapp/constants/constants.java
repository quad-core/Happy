package com.projects.diwanshusoni.happyapp.constants;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Diwanshu Soni on 30-09-2017.
 */

public class constants {
    //constant headers for fdb node-names
    public static final FirebaseAuth AUTH = FirebaseAuth.getInstance();
    public static final FirebaseUser USER =  AUTH.getCurrentUser();

    private static final DatabaseReference REF_REALTIME_DB_INSTANCE = FirebaseDatabase.getInstance().getReference();

    public static final String USER_NODE = "USERS";
    public static final String USER_NODE_UID = "USERS_BY_UID";
    public static final String USER_NODE_EMAIL = "USERS_BY_EMAIL";
    public static final DatabaseReference REF_USER_NODE = REF_REALTIME_DB_INSTANCE.child(USER_NODE);
    public static final DatabaseReference REF_USER_UID_NODE = REF_USER_NODE.child(USER_NODE_UID);
    public static final DatabaseReference REF_USER_EMAIL_NODE = REF_USER_NODE.child(USER_NODE_EMAIL);

    public static final String TASKS_NODE = "TASKS";
    public static final String TASKS_UID_NODE = "TASKS_BY_UID";
    public static final String TASKS_EMAIL_NODE = "TASKS_BY_EMAIL";
    public static final String TASKS_LEVEL_NODE = "TASKS_BY_LEVEL";
    public static final String TASKS_LEVEL_easy_HEADING_NODE = "EASY";
    public static final String TASKS_LEVEL_medium_HEADING_NODE = "MEDIUM";
    public static final String TASKS_LEVEL_hard_HEADING_NODE = "HARD";
    public static final String SUBMITTED_TASKS_NODE = "SUBMITTED_TASKS";
    public static final String TASK_IMAGE_STORAGE_NODE = "TASK_IMAGES";
    public static final DatabaseReference REF_TASKS_DB = REF_REALTIME_DB_INSTANCE.child(TASKS_NODE);
    public static final DatabaseReference REF_ADDED_TASKS_UID = REF_TASKS_DB.child(TASKS_UID_NODE);
    public static final DatabaseReference REF_ADDED_TASKS_LEVEL = REF_TASKS_DB.child(TASKS_LEVEL_NODE);
    public static final DatabaseReference REF_SUBMITTED_TASKS_NODE = REF_TASKS_DB.child(SUBMITTED_TASKS_NODE);

    public static final StorageReference REF_TASK_IMAGE_STORAGE = FirebaseStorage.getInstance().getReference().child(TASK_IMAGE_STORAGE_NODE);
}
