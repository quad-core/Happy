package com.projects.diwanshusoni.happyapp.pojos;

import java.io.Serializable;

/**
 * Created by Diwanshu Soni on 02-10-2017.
 */

public class PojoSubmitedTask {
    public String getDateSubmittedOn() {
        return dateSubmittedOn;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserProfilePicImageUrl() {
        return userProfilePicImageUrl;
    }

    public String getTaskImageUrl() {
        return taskImageUrl;
    }

    public String getTaskVideoUrl() {
        return taskVideoUrl;
    }

    public String getTaskAudioUrl() {
        return taskAudioUrl;
    }

    public String getTaskExperience() {
        return taskExperience;
    }

    public String getTaskSubmissionUid() {
        return taskSubmissionUid;
    }

    public String getTaskUid() {
        return taskUid;
    }

    private String dateSubmittedOn;
    private String userName;
    private String userProfilePicImageUrl;
    private String taskImageUrl;
    private String taskVideoUrl;
    private String taskAudioUrl;
    private String taskExperience;
    private String taskSubmissionUid, taskUid;

    public void setTaskExperience(String taskExperience) {
        this.taskExperience = taskExperience;
    }


    public void setTaskUid(String taskUid) {
        this.taskUid = taskUid;
    }

    public void setDateSubmittedOn(String dateSubmittedOn) {
        this.dateSubmittedOn = dateSubmittedOn;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserProfilePicImageUrl(String userProfilePicImageUrl) {
        this.userProfilePicImageUrl = userProfilePicImageUrl;
    }

    public void setTaskImageUrl(String taskImageUrl) {
        this.taskImageUrl = taskImageUrl;
    }

    public void setTaskVideoUrl(String taskVideoUrl) {
        this.taskVideoUrl = taskVideoUrl;
    }

    public void setTaskAudioUrl(String taskAudioUrl) {
        this.taskAudioUrl = taskAudioUrl;
    }

    public void setTaskSubmissionUid(String taskSubmissionUid) {
        this.taskSubmissionUid = taskSubmissionUid;
    }
}
