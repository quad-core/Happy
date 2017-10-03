package com.projects.diwanshusoni.happyapp.pojos;

import java.io.Serializable;

/**
 * Created by Diwanshu Soni on 30-09-2017.
 */

public class PojoTaskTodo implements Serializable{
    public String taskUid, taskHeading, taskDescription, level, createdBy, date, imageUri;

    public void setTaskUid(String taskUid) {
        this.taskUid = taskUid;
    }

    public String getTaskUid() {
        return taskUid;
    }



    public String getTaskHeading() {
        return taskHeading;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public String getLevel() {
        return level;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getDate() {
        return date;
    }

    public String getImageUri() {
        return imageUri;
    }
}
