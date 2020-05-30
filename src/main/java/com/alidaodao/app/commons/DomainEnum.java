package com.alidaodao.app.commons;

import com.alidaodao.app.config.CosConfig;

import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @desc cos域名枚举
 * @author Jack
 * @date 2020/2/23 23:04
 */
public enum DomainEnum {
    CDN("COS-CDN域名"),
    OSS("COS-原域名");

    private String desc;

    public String getDesc() {
        return desc;
    }

    DomainEnum(String desc) {
        this.desc = desc;
    }

    public URL replaceCOSUrlDomain(URL url, CosConfig cosConfig) {
        String targetDomain = null;
        if (DomainEnum.CDN.equals(this)) {
            targetDomain = cosConfig.getCdnHost();
        } else if (DomainEnum.OSS.equals(this)) {
            targetDomain = cosConfig.getOssHost();
        }
        if (targetDomain != null) {
            try {
                url = new URL(url.getProtocol(), targetDomain, url.getPort(), url.getFile());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return url;
    }
}
