package com.alidaodao.app.config;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.region.Region;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @desc cos配置
 * @author bosong
 * @date 2020/2/23 21:34
 */
public class CosConfig {

    private String accessId;

    private String accessKey;

    private String appId;

    private String region;

    private String ossHost;

    private String cdnHost;

    private String bucketName;

    public String getAccessId() {
        return accessId;
    }

    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getOssHost() {
        return ossHost;
    }

    public void setOssHost(String ossHost) {
        this.ossHost = ossHost;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getCdnHost() {
        return cdnHost;
    }

    public void setCdnHost(String cdnHost) {
        this.cdnHost = cdnHost;
    }

    public COSClient generateClient() {
        if (StringUtils.isBlank(appId)) {
            return new COSClient(new BasicCOSCredentials(accessId, accessKey), new ClientConfig(new Region(region)));
        } else {
            return new COSClient(new BasicCOSCredentials(appId, accessId, accessKey), new ClientConfig(new Region(region)));
        }
    }
}
