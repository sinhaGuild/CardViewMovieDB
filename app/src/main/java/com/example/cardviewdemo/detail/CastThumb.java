
package com.example.cardviewdemo.detail;

import android.net.Uri;

import com.example.cardviewdemo.config.ConfigItem;

public class CastThumb {


    private String castId;
    private String character;
    private String creditId;
    private int id;
    private String name;
    private int order;
    private String profilePath;

    public CastThumb(String character, String name, String profilePath) {
        this.character = character;
        this.name = name;
        this.profilePath = profilePath;
    }

    public CastThumb(String character, String name, String profilePath, String castId) {
        this.castId = castId;
        this.character = character;
        this.name = name;
        this.profilePath = profilePath;
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
     * @return The castId
     */
    public String getCastId() {
        return castId;
    }

    /**
     * @param castId The cast_id
     */
    public void setCastId(String castId) {
        this.castId = castId;
    }

    /**
     * @return The character
     */
    public String getCharacter() {
        return character;
    }

    /**
     * @param character The character
     */
    public void setCharacter(String character) {
        this.character = character;
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
     * @return The order
     */
    public int getOrder() {
        return order;
    }

    /**
     * @param order The order
     */
    public void setOrder(int order) {
        this.order = order;
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
