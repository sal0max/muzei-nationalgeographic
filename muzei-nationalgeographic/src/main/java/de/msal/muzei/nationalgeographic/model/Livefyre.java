
package de.msal.muzei.nationalgeographic.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Livefyre {

    @SerializedName("pageGuid")
    @Expose
    private String pageGuid;
    @SerializedName("checksum")
    @Expose
    private String checksum;
    @SerializedName("lfMetadata")
    @Expose
    private String lfMetadata;
    @SerializedName("siteSecret")
    @Expose
    private String siteSecret;
    @SerializedName("lfSiteId")
    @Expose
    private String lfSiteId;
    @SerializedName("lfNetworkName")
    @Expose
    private String lfNetworkName;
    @SerializedName("lfElement")
    @Expose
    private String lfElement;

    /**
     * 
     * @return
     *     The pageGuid
     */
    public String getPageGuid() {
        return pageGuid;
    }

    /**
     * 
     * @param pageGuid
     *     The pageGuid
     */
    public void setPageGuid(String pageGuid) {
        this.pageGuid = pageGuid;
    }

    /**
     * 
     * @return
     *     The checksum
     */
    public String getChecksum() {
        return checksum;
    }

    /**
     * 
     * @param checksum
     *     The checksum
     */
    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    /**
     * 
     * @return
     *     The lfMetadata
     */
    public String getLfMetadata() {
        return lfMetadata;
    }

    /**
     * 
     * @param lfMetadata
     *     The lfMetadata
     */
    public void setLfMetadata(String lfMetadata) {
        this.lfMetadata = lfMetadata;
    }

    /**
     * 
     * @return
     *     The siteSecret
     */
    public String getSiteSecret() {
        return siteSecret;
    }

    /**
     * 
     * @param siteSecret
     *     The siteSecret
     */
    public void setSiteSecret(String siteSecret) {
        this.siteSecret = siteSecret;
    }

    /**
     * 
     * @return
     *     The lfSiteId
     */
    public String getLfSiteId() {
        return lfSiteId;
    }

    /**
     * 
     * @param lfSiteId
     *     The lfSiteId
     */
    public void setLfSiteId(String lfSiteId) {
        this.lfSiteId = lfSiteId;
    }

    /**
     * 
     * @return
     *     The lfNetworkName
     */
    public String getLfNetworkName() {
        return lfNetworkName;
    }

    /**
     * 
     * @param lfNetworkName
     *     The lfNetworkName
     */
    public void setLfNetworkName(String lfNetworkName) {
        this.lfNetworkName = lfNetworkName;
    }

    /**
     * 
     * @return
     *     The lfElement
     */
    public String getLfElement() {
        return lfElement;
    }

    /**
     * 
     * @param lfElement
     *     The lfElement
     */
    public void setLfElement(String lfElement) {
        this.lfElement = lfElement;
    }

}
