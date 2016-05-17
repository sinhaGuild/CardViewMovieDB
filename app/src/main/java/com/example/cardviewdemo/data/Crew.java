
package com.example.cardviewdemo.data;


import android.net.Uri;

public class Crew {

    private String creditId;
    private String department;
    private int id;
    private String job;
    private String name;
    private String profilePath;

    public Crew(String job, String profilePath, String name) {
        this.job = job;
        this.profilePath = profilePath;
        this.name = name;
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
     * @return The id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(int id) {
        this.id = id;
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
