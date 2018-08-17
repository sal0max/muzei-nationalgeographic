
package de.msal.muzei.nationalgeographic.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Item {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("caption")
    @Expose
    private String caption;
    @SerializedName("credit")
    @Expose
    private String credit;
    @SerializedName("altText")
    @Expose
    private String altText;
    @SerializedName("full-path-url")
    @Expose
    private String fullPathUrl;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("originalUrl")
    @Expose
    private String originalUrl;
    @SerializedName("aspectRatio")
    @Expose
    private Double aspectRatio;
    @SerializedName("sizes")
    @Expose
    private Sizes sizes;
    @SerializedName("internal")
    @Expose
    private Boolean internal;
    @SerializedName("pageUrl")
    @Expose
    private String pageUrl;
    @SerializedName("publishDate")
    @Expose
    private String publishDate;
    @SerializedName("yourShot")
    @Expose
    private Boolean yourShot;
    @SerializedName("social")
    @Expose
    private Social social;
    @SerializedName("livefyre")
    @Expose
    private Livefyre livefyre;
    @SerializedName("guid")
    @Expose
    private String guid;
    @SerializedName("height")
    @Expose
    private Integer height;
    @SerializedName("width")
    @Expose
    private Integer width;

    /**
     * 
     * @return
     *     The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 
     * @return
     *     The caption
     */
    public String getCaption() {
        return caption;
    }

    /**
     * 
     * @param caption
     *     The caption
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * 
     * @return
     *     The credit
     */
    public String getCredit() {
        return credit;
    }

    /**
     * 
     * @param credit
     *     The credit
     */
    public void setCredit(String credit) {
        this.credit = credit;
    }

    /**
     * 
     * @return
     *     The altText
     */
    public String getAltText() {
        return altText;
    }

    /**
     * 
     * @param altText
     *     The altText
     */
    public void setAltText(String altText) {
        this.altText = altText;
    }

    /**
     * 
     * @return
     *     The fullPathUrl
     */
    public String getFullPathUrl() {
        return fullPathUrl;
    }

    /**
     * 
     * @param fullPathUrl
     *     The full-path-url
     */
    public void setFullPathUrl(String fullPathUrl) {
        this.fullPathUrl = fullPathUrl;
    }

    /**
     * 
     * @return
     *     The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * 
     * @param url
     *     The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 
     * @return
     *     The originalUrl
     */
    public String getOriginalUrl() {
        return originalUrl;
    }

    /**
     * 
     * @param originalUrl
     *     The originalUrl
     */
    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    /**
     * 
     * @return
     *     The aspectRatio
     */
    public Double getAspectRatio() {
        return aspectRatio;
    }

    /**
     * 
     * @param aspectRatio
     *     The aspectRatio
     */
    public void setAspectRatio(Double aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    /**
     * 
     * @return
     *     The sizes
     */
    public Sizes getSizes() {
        return sizes;
    }

    /**
     * 
     * @param sizes
     *     The sizes
     */
    public void setSizes(Sizes sizes) {
        this.sizes = sizes;
    }

    /**
     * 
     * @return
     *     The internal
     */
    public Boolean getInternal() {
        return internal;
    }

    /**
     * 
     * @param internal
     *     The internal
     */
    public void setInternal(Boolean internal) {
        this.internal = internal;
    }

    /**
     * 
     * @return
     *     The pageUrl
     */
    public String getPageUrl() {
        return pageUrl;
    }

    /**
     * 
     * @param pageUrl
     *     The pageUrl
     */
    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    /**
     * 
     * @return
     *     The publishDate
     */
    public String getPublishDate() {
        return publishDate;
    }

    /**
     * 
     * @param publishDate
     *     The publishDate
     */
    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    /**
     * 
     * @return
     *     The yourShot
     */
    public Boolean getYourShot() {
        return yourShot;
    }

    /**
     * 
     * @param yourShot
     *     The yourShot
     */
    public void setYourShot(Boolean yourShot) {
        this.yourShot = yourShot;
    }

    /**
     * 
     * @return
     *     The social
     */
    public Social getSocial() {
        return social;
    }

    /**
     * 
     * @param social
     *     The social
     */
    public void setSocial(Social social) {
        this.social = social;
    }

    /**
     * 
     * @return
     *     The livefyre
     */
    public Livefyre getLivefyre() {
        return livefyre;
    }

    /**
     * 
     * @param livefyre
     *     The livefyre
     */
    public void setLivefyre(Livefyre livefyre) {
        this.livefyre = livefyre;
    }

    /**
     * 
     * @return
     *     The guid
     */
    public String getGuid() {
        return guid;
    }

    /**
     * 
     * @param guid
     *     The guid
     */
    public void setGuid(String guid) {
        this.guid = guid;
    }

    /**
     * 
     * @return
     *     The height
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * 
     * @param height
     *     The height
     */
    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
     * 
     * @return
     *     The width
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * 
     * @param width
     *     The width
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

}
