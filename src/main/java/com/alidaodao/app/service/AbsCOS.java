package com.alidaodao.app.service;

import com.alidaodao.app.ICOS;
import com.alidaodao.app.commons.DomainEnum;
import com.alidaodao.app.config.CosConfig;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.*;
import com.tencent.cloud.CosStsClient;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 *
 * @desc cos抽象方法
 * @author bosong
 * @date 2020/2/23 21:57
 */
public abstract class AbsCOS implements ICOS {

    protected CosConfig cosConfig;

    private COSClient cosClient;

    public AbsCOS() {
    }

    public AbsCOS(CosConfig cosConfig) {
        this.cosConfig = cosConfig;
        if (cosConfig != null) {
            this.cosClient = cosConfig.generateClient();
        }
    }

    @Override
    public CosConfig getCosConfig() {
        return cosConfig;
    }

    public COSClient getCosClient() {
        return cosClient;
    }


    public PutObjectResult putFile(PutObjectRequest putObjectRequest) {
        PutObjectResult putObjectResult = null;
        if (getCosClient() == null || putObjectRequest == null) {
            return putObjectResult;
        }
        try {
            putObjectResult = getCosClient().putObject(putObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                putObjectRequest.getInputStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return putObjectResult;
    }

    public AppendObjectResult appendFile(AppendObjectRequest appendObjectRequest) {
        AppendObjectResult appendObjectResult = null;
        if (cosClient == null || appendObjectRequest == null) {
            return appendObjectResult;
        }
        try {
            appendObjectResult = cosClient.appendObject(appendObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                appendObjectRequest.getInputStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return appendObjectResult;
    }

    public COSObject getFile(GetObjectRequest getObjectRequest) {
        COSObject oSSObject = null;
        if (cosClient == null || getObjectRequest == null) {
            return oSSObject;
        }

        try {
            oSSObject = cosClient.getObject(getObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return oSSObject;
    }

    public ObjectMetadata getObjectMetadata(GetObjectMetadataRequest request) {
        ObjectMetadata objectMetadata = null;
        if (cosClient == null || request == null) {
            return objectMetadata;
        }
        try {
            objectMetadata = cosClient.getObjectMetadata(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objectMetadata;
    }


    public boolean delFile(DeleteObjectRequest genericRequest) {
        if (cosClient == null || genericRequest == null) {
            return false;
        }
        try {
            cosClient.deleteObject(genericRequest);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public CopyObjectResult copyFile(CopyObjectRequest copyObjectRequest) {
        CopyObjectResult copyObjectResult = null;
        if (cosClient == null || copyObjectRequest == null) {
            return copyObjectResult;
        }
        try {
            copyObjectResult = cosClient.copyObject(copyObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return copyObjectResult;
    }

    public String getExpUrl(GeneratePresignedUrlRequest request, DomainEnum domainEnum) {
        if (cosClient == null || request == null || cosConfig == null) {
            return null;
        }
        URL url = cosClient.generatePresignedUrl(request);
        if (domainEnum != null) {
            url = domainEnum.replaceCOSUrlDomain(url, cosConfig);
        }
        return url.toString().replace(url.getProtocol() + ":", "");
    }

    public boolean doesObjectExist(String key) {
        if (cosClient == null || cosConfig == null) {
            return false;
        }
        try {
            return cosClient.doesObjectExist(cosConfig.getBucketName(), key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<COSObjectSummary> doListObjects(String bTypeName, String prefix) {
        String endPrefix = bTypeName + "/" + prefix;
        final int maxKeys = 30;
        String nextMarker = null;
        ObjectListing objectListing = null;
        ListObjectsRequest lsObjReq = new ListObjectsRequest();
        lsObjReq.setBucketName(cosConfig.getBucketName());
        lsObjReq.setPrefix(endPrefix);
        List<COSObjectSummary> lsSums = new ArrayList<COSObjectSummary>();
        List<COSObjectSummary> sums = null;
        do {
            objectListing = cosClient.listObjects(lsObjReq.withMarker(nextMarker).withMaxKeys(maxKeys));
            nextMarker = objectListing.getNextMarker();
            sums = objectListing.getObjectSummaries();
            if (sums != null && sums.size() > 0) {
                lsSums.addAll(sums);
            }

        } while (objectListing.isTruncated());
        return lsSums;
    }

    public String generatePresignedUrl(GeneratePresignedUrlRequest req) {
        if (cosClient == null || cosConfig == null) {
            return null;
        }
        try {
            return cosClient.generatePresignedUrl(req).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getStsToken(int expireSeconds, String allowPath) {
        if (cosConfig == null) {
            return null;
        }
        TreeMap<String, Object> config = new TreeMap<String, Object>();
        JSONObject credential = null;
        try {
            // 替换为您的 SecretId
            config.put("SecretId", cosConfig.getAccessId());
            // 替换为您的 SecretKey
            config.put("SecretKey", cosConfig.getAccessKey());

            // 临时密钥有效时长，单位是秒，默认1800秒，最长可设定有效期为7200秒
            config.put("durationSeconds", expireSeconds);

            // 换成您的 bucket
            config.put("bucket", cosConfig.getBucketName());
            // 换成 bucket 所在地区
            config.put("region", cosConfig.getRegion());

            // 这里改成允许的路径前缀，可以根据自己网站的用户登录态判断允许上传的具体路径，例子：a.jpg 或者 a/* 或者 * 。
            // 如果填写了“*”，将允许用户访问所有资源；除非业务需要，否则请按照最小权限原则授予用户相应的访问权限范围。
            config.put("allowPrefix", allowPath);

            // 密钥的权限列表。简单上传、表单上传和分片上传需要以下的权限，其他权限列表请看 https://cloud.tencent.com/document/product/436/31923
            String[] allowActions = new String[]{
                    // 简单上传
                    "name/cos:PutObject",
                    // 表单上传、小程序上传
                    "name/cos:PostObject",
                    // 分片上传
                    "name/cos:InitiateMultipartUpload",
                    "name/cos:ListMultipartUploads",
                    "name/cos:ListParts",
                    "name/cos:UploadPart",
                    "name/cos:CompleteMultipartUpload"
            };
            config.put("allowActions", allowActions);

            credential = CosStsClient.getCredential(config);
            //成功返回临时密钥信息，如下打印密钥信息
            System.out.println(credential);
        } catch (Exception e) {
            //失败抛出异常
            throw new IllegalArgumentException("no valid secret !");
        }
        return credential;
    }

}
