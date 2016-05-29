
package com.example.cardviewdemo.detail;


import android.net.Uri;

import com.example.cardviewdemo.config.ConfigItem;

public class CrewThumb {

    private String creditId;
    private String department;
    private String crewID;
    private String job;
    private String name;
    private String profilePath;
    private String media_type;

    public CrewThumb(String job, String profilePath, String name, String crewID, String media_type) {
        this.crewID = crewID;

        this.job = job;
        this.media_type = media_type;
        this.name = name;
        this.profilePath = profilePath;
    }

    public CrewThumb(String job, String profilePath, String name) {
        this.job = job;
        this.profilePath = profilePath;
        this.name = name;
    }

    public CrewThumb(String job, String profilePath, String name, String crewID) {
        this.crewID = crewID;
        this.job = job;
        this.profilePath = profilePath;
        this.name = name;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public String buildURL(String path) {
        Uri.Builder uri = new Uri.Builder();
        uri.scheme("http").authority(ConfigItem.TAG_IMAGE_URL_BUILDER).
                appendPath("t").
                appendPath("p").
                appendPath("w500").appendPath(path.substring(1));
        return uri.build().toString();
    }

    /**
     * @return The creditId
     */
    public String getCreditId() {
        return creditId;
    }

    /**
     * @param creditId The credit_id
     */
    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    /**
     * @return The department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * @param department The department
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * @return The crewID
     */
    public String getCrewID() {
        return crewID;
    }

    /**
     * @param crewID The crewID
     */
    public void setCrewID(String crewID) {
        this.crewID = crewID;
    }

    /**
     * @return The job
     */
    public String getJob() {
        return job;
    }

    /**
     * @param job The job
     */
    public void setJob(String job) {
        this.job = job;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The profilePath
     */
    public String getProfilePath() {

        return buildURL(profilePath);
    }

    /**
     * @param profilePath The profile_path
     */
    public void setProfilePath(String profilePath) {

        this.profilePath = profilePath;
    }

    @Override
    public String toString() {
        return "";
    }

}
