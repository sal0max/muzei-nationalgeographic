
package de.msal.muzei.nationalgeographic.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Feed {

    @SerializedName("galleryTitle")
    @Expose
    private String galleryTitle;
    @SerializedName("previousEndpoint")
    @Expose
    private String previousEndpoint;
    @SerializedName("nextEndpoint")
    @Expose
    private String nextEndpoint;
    @SerializedName("items")
    @Expose
    private List<Item> items = new ArrayList<Item>();

    /**
     * 
     * @return
     *     The galleryTitle
     */
    public String getGalleryTitle() {
        return galleryTitle;
    }

    /**
     * 
     * @param galleryTitle
     *     The galleryTitle
     */
    public void setGalleryTitle(String galleryTitle) {
        this.galleryTitle = galleryTitle;
    }

    /**
     *
     * @return
     *     The previousEndpoint
     */
    public String getPreviousEndpoint() {
        return previousEndpoint;
    }

    /**
     *
     * @param previousEndpoint
     *     The previousEndpoint
     */
    public void setPreviousEndpoint(String previousEndpoint) {
        this.previousEndpoint = previousEndpoint;
    }


    /**
     *
     * @return
     *     The nextEndpoint
     */
    public String getNextEndpoint() {
        return nextEndpoint;
    }

    /**
     *
     * @param nextEndpoint
     *     The nextEndpoint
     */
    public void setNextEndpoint(String nextEndpoint) {
        this.nextEndpoint = nextEndpoint;
    }

    /**
     * 
     * @return
     *     The items
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * 
     * @param items
     *     The items
     */
    public void setItems(List<Item> items) {
        this.items = items;
    }

}
