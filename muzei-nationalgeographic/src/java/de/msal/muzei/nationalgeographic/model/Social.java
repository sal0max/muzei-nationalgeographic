
package de.msal.muzei.nationalgeographic.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Social {

    @SerializedName("og:title")
    @Expose
    private String ogTitle;
    @SerializedName("og:description")
    @Expose
    private String ogDescription;
    @SerializedName("twitter:site")
    @Expose
    private String twitterSite;

    /**
     * 
     * @return
     *     The ogTitle
     */
    public String getOgTitle() {
        return ogTitle;
    }

    /**
     * 
     * @param ogTitle
     *     The og:title
     */
    public void setOgTitle(String ogTitle) {
        this.ogTitle = ogTitle;
    }

    /**
     * 
     * @return
     *     The ogDescription
     */
    public String getOgDescription() {
        return ogDescription;
    }

    /**
     * 
     * @param ogDescription
     *     The og:description
     */
    public void setOgDescription(String ogDescription) {
        this.ogDescription = ogDescription;
    }

    /**
     * 
     * @return
     *     The twitterSite
     */
    public String getTwitterSite() {
        return twitterSite;
    }

    /**
     * 
     * @param twitterSite
     *     The twitter:site
     */
    public void setTwitterSite(String twitterSite) {
        this.twitterSite = twitterSite;
    }

}
