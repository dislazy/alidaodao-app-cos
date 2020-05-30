package com.alidaodao.app.service;

import com.alidaodao.app.ICOS;
import com.alidaodao.app.commons.DomainEnum;
import com.alidaodao.app.config.CosConfig;
import com.alidaodao.app.util.DateTimeUtils;
import com.alidaodao.app.util.IOUtils;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.*;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 *
 * @desc cos接口实现
 * @author Jack
 * @date 2020/2/23 22:05
 */
public class COS extends AbsCOS {
    public COS() {
    }

    public COS(CosConfig cosConfig) {
        super(cosConfig);
    }

    @Override
    public PutObjectResult putFile(String finalKey, InputStream is) {
        return putFile(finalKey, is, (ObjectMetadata) null);
    }

    @Override
    public PutObjectResult putFile(String finalKey, InputStream is, String contentType) {
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentType(contentType);
        return putFile(finalKey, is, meta);
    }

    @Override
    public PutObjectResult putFile(String finalKey, byte[] bytes) {
        return putFile(finalKey, bytes, (ObjectMetadata) null);
    }

    @Override
    public PutObjectResult putFile(String finalKey, byte[] bytes, String contentType) {
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentType(contentType);
        return putFile(finalKey, bytes, meta);
    }

    @Override
    public PutObjectResult putFile(String finalKey, InputStream is, ObjectMetadata meta) {
        PutObjectResult putObjectResult = null;
        if (finalKey == null || is == null) {
            return putObjectResult;
        }
        if (meta == null) {
            meta = new ObjectMetadata();
        }
        if (meta.getContentLength() <= 0) {
            Long l = null;
            try {
                l = (long) is.available();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (l == null) {
                return putObjectResult;
            }
            meta.setContentLength(l);
        }
        PutObjectRequest putObjectRequest = new PutObjectRequest(cosConfig.getBucketName(), finalKey, is, meta);
        return putFile(putObjectRequest);
    }

    @Override
    public PutObjectResult putFile(String finalKey, byte[] bytes, ObjectMetadata meta) {
        PutObjectResult putObjectResult = null;
        try (InputStream is = IOUtils.byteToInputStream(bytes)) {
            putObjectResult = putFile(finalKey, is, meta);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return putObjectResult;
    }

    @Override
    public AppendObjectResult appendFile(String finalKey, InputStream is, long position) {
        return appendFile(finalKey, is, (ObjectMetadata) null, position);
    }

    @Override
    public AppendObjectResult appendFile(String finalKey, InputStream is, String contentType, long position) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);
        return appendFile(finalKey, is, metadata, position);
    }

    @Override
    public AppendObjectResult appendFile(String finalKey, byte[] bytes, long position) {
        return appendFile(finalKey, bytes, (ObjectMetadata) null, position);
    }

    @Override
    public AppendObjectResult appendFile(String finalKey, byte[] bytes, String contentType, long position) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);
        return appendFile(finalKey, bytes, metadata, position);
    }

    @Override
    public AppendObjectResult appendFile(String finalKey, InputStream is, ObjectMetadata meta, long position) {
        if (finalKey == null || is == null) {
            return null;
        }
        if (meta == null) {
            meta = new ObjectMetadata();
        }
        if (position < 0) {
            position = 0;
        }
        AppendObjectRequest appendObjectRequest = new AppendObjectRequest(cosConfig.getBucketName(), finalKey, is, meta);
        appendObjectRequest.setPosition(position);
        return appendFile(appendObjectRequest);
    }

    @Override
    public AppendObjectResult appendFile(String finalKey, byte[] bytes, ObjectMetadata meta, long position) {
        try (InputStream in = IOUtils.byteToInputStream(bytes)) {
            return appendFile(finalKey, in, meta, position);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public COSObject getFile(String finalKey) {
        return getFile(finalKey, 0, 0);
    }

    @Override
    public COSObject getFile(String finalKey, long rangeFrom, long rangeTo) {
        GetObjectRequest getObjectRequest = new GetObjectRequest(cosConfig.getBucketName(), finalKey);
        if (rangeFrom >= 0 && rangeTo > 0) {
            getObjectRequest.setRange(rangeFrom, rangeTo);
        }
        return getFile(getObjectRequest);
    }

    @Override
    public ObjectMetadata getObjectMetadata(String finalKey) {
        GetObjectMetadataRequest request = new GetObjectMetadataRequest(cosConfig.getBucketName(), finalKey);
        return getObjectMetadata(request);
    }

    @Override
    public boolean delFile(String finalKey) {
        DeleteObjectRequest request = new DeleteObjectRequest(cosConfig.getBucketName(), finalKey);
        return delFile(request);
    }

    @Override
    public CopyObjectResult copyFile(String sourceKey, String targetKey) {
        return copyFile2Bucket(sourceKey, this, targetKey);
    }

    @Override
    public CopyObjectResult copyFile2Bucket(String sourceKey, ICOS targetOss, String targetKey) {
        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(cosConfig.getBucketName(), sourceKey, targetOss.getCosConfig().getBucketName(), targetKey);
        return copyFile(copyObjectRequest);
    }

    @Override
    public String getDownloadExpUrl(String finalKey, int expiration) {
        return getDownloadExpUrl(finalKey, DateTimeUtils.changeMinute(new Date(), expiration));
    }

    @Override
    public String getDownloadExpUrl(String finalKey, Date expiration) {
        return getDownloadExpUrl(finalKey, expiration, DomainEnum.CDN);
    }

    @Override
    public String getDownloadExpUrl(String finalKey, Date expiration, DomainEnum domainType) {
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(cosConfig.getBucketName(), finalKey);
        request.setExpiration(expiration);
        return getExpUrl(request, domainType);
    }


    @Override
    public String getPathUrl(String finalKey) {
        return String.format("//%s/%s", getCosConfig().getCdnHost(), finalKey);
    }

    @Override
    public String getTempPutFileUrl(String finalKey, String md5, long expireTime) {
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(cosConfig.getBucketName(), finalKey, HttpMethodName.PUT);
        request.setContentMd5(md5);
        request.setExpiration(DateTimeUtils.changeMinute(new Date(), (int) expireTime));
        return generatePresignedUrl(request);
    }


    @Override
    public List<COSObjectSummary> listObjects(String bTypeName, String prefix) {
        return doListObjects(bTypeName, prefix);
    }

    @Override
    public boolean fileIsExist(String key) {
        return doesObjectExist(key);
    }

    @Override
    public JSONObject getSTSToken(String allowPath, int expire) {
        return getStsToken(expire, allowPath);
    }

}
