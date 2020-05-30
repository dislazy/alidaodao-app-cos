package com.alidaodao.app;

import com.alidaodao.app.commons.DomainEnum;
import com.alidaodao.app.config.CosConfig;
import com.qcloud.cos.model.*;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**  
 * 
 * @desc tencent-cos-helper
 * @author Jack  
 * @date 2020/2/23 22:00
 */
public interface ICOS {
    /**
     * @desc 获取cos配置
     * @author Jack
     * @date 2020/2/24 21:55
     * @return cos配置
     */
    CosConfig getCosConfig();
    /**
     * @desc 上传文件
     * @author Jack
     * @date 2020/2/24 21:55
     * @param finalKey 文件名
     * @param is 文件流
     * @return 上传文件结果
     */
    PutObjectResult putFile(String finalKey, InputStream is);
    /**
     * @desc 上传文件
     * @author Jack
     * @date 2020/2/24 21:55
     * @param finalKey 文件名
     * @param is 文件流
     * @param contentType contentType
     * @return 上传文件结果
     */
    PutObjectResult putFile(String finalKey, InputStream is, String contentType);
    /**
     * @desc 上传文件
     * @author Jack
     * @date 2020/2/24 21:55
     * @param finalKey 文件名
     * @param bytes 字节数组
     * @return 上传文件结果
     */
    PutObjectResult putFile(String finalKey, byte[] bytes);
    /**
     * @desc 上传文件
     * @author Jack
     * @date 2020/2/24 21:55
     * @param finalKey 文件名
     * @param bytes 字节数组
     * @param contentType contentType
     * @return 上传文件结果
     */
    PutObjectResult putFile(String finalKey, byte[] bytes, String contentType);
    /**
     * @desc 上传文件
     * @author Jack
     * @date 2020/2/24 21:55
     * @param finalKey 文件名
     * @param is 文件流
     * @param meta 元信息
     * @return 上传文件结果
     */
    PutObjectResult putFile(String finalKey, InputStream is, ObjectMetadata meta);
    /**
     * @desc 上传文件
     * @author Jack
     * @date 2020/2/24 21:55
     * @param finalKey 文件名
     * @param bytes byte
     * @param meta 元信息
     * @return 上传文件结果
     */
    PutObjectResult putFile(String finalKey, byte[] bytes, ObjectMetadata meta);
    /**
     * @desc 增量写入文件
     * @author Jack
     * @date 2020/2/24 21:55
     * @param finalKey 文件名
     * @param is 文件流
     * @param position 位置
     * @return 增量写入结果
     */
    AppendObjectResult appendFile(String finalKey, InputStream is, long position);
    /**
     * @desc 增量写入文件
     * @author Jack
     * @date 2020/2/24 21:55
     * @param finalKey 文件名
     * @param is 文件流
     * @param contentType contentType
     * @param position 位置
     * @return 增量写入结果
     */
    AppendObjectResult appendFile(String finalKey, InputStream is, String contentType, long position);
    /**
     * @desc 增量写入文件
     * @author Jack
     * @date 2020/2/24 21:55
     * @param finalKey 文件名
     * @param bytes 字节
     * @param position 位置
     * @return 增量写入结果
     */
    AppendObjectResult appendFile(String finalKey, byte[] bytes, long position);
    /**
     * @desc 增量写入文件
     * @author Jack
     * @date 2020/2/24 21:55
     * @param finalKey 文件名
     * @param bytes 字节
     * @param contentType 类型
     * @param position 位置
     * @return 增量写入结果
     */
    AppendObjectResult appendFile(String finalKey, byte[] bytes, String contentType, long position);
    /**
     * @desc 增量写入文件
     * @author Jack
     * @date 2020/2/24 21:55
     * @param finalKey 文件名
     * @param is 文件流
     * @param meta meta
     * @param position 位置
     * @return 增量写入结果
     */
    AppendObjectResult appendFile(String finalKey, InputStream is, ObjectMetadata meta, long position);
    /**
     * @desc 增量写入文件
     * @author Jack
     * @date 2020/2/24 21:55
     * @param finalKey 文件名
     * @param bytes 文件byte
     * @param meta meta
     * @param position 位置
     * @return 增量写入结果
     */
    AppendObjectResult appendFile(String finalKey, byte[] bytes, ObjectMetadata meta, long position);
    /**
     * @desc 获取文件
     * @author Jack
     * @date 2020/2/24 21:55
     * @param finalKey 文件名
     * @return 文件数据
     */
    COSObject getFile(String finalKey);
    /**
     * @desc 获取文件片段
     * @author Jack
     * @date 2020/2/24 21:55
     * @param finalKey 文件名
     * @param rangeFrom 开头
     * @param rangeTo 结尾
     * @return 文件数据
     */
    COSObject getFile(String finalKey, long rangeFrom, long rangeTo);
    /**
     * @desc 获取文件的元信息
     * @author Jack
     * @date 2020/2/24 21:55
     * @param finalKey 文件名
     * @return 元信息
     */
    ObjectMetadata getObjectMetadata(String finalKey);
    /**
     * @desc 删除文件
     * @author Jack
     * @date 2020/2/24 21:55
     * @param finalKey 文件名
     * @return 删除结果
     */
    boolean delFile(String finalKey);
    /**
     * @desc copy文件
     * @author Jack
     * @date 2020/2/24 21:55
     * @param  fromKey source
     * @param toKey target
     * @return copy结果
     */
    CopyObjectResult copyFile(String fromKey, String toKey);
    /**
     * @desc copy文件
     * @author Jack
     * @date 2020/2/24 21:55
     * @param  fromKey source
     * @param targetOss 目标bucket
     * @param toKey target
     * @return copy结果
     */
    CopyObjectResult copyFile2Bucket(String fromKey, ICOS targetOss, String toKey);
    /**
     * @desc 获取cdn域名下的临时地址
     * @author Jack
     * @date 2020/2/24 21:54
     * @param  finalKey 文件名
     * @param expiration 过期时间  s
     * @return url
     */
    String getDownloadExpUrl(String finalKey, int expiration);
    /**
     * @desc 获取cdn域名下的临时地址
     * @author Jack
     * @date 2020/2/24 21:54
     * @param  finalKey 文件名
     * @param expiration 过期日期
     * @return url
     */
    String getDownloadExpUrl(String finalKey, Date expiration);
    /**
     * @desc 临时获取文件的下载地址
     * @author Jack
     * @date 2020/2/24 21:52
     * @param  finalKey 文件名
     * @param expiration 链接过期时间
     * @param domainType 返回的连接类型
     * @return 下载地址
     */
    String getDownloadExpUrl(String finalKey, Date expiration, DomainEnum domainType);
    /**
     * @desc 获取cdn域名下的文件地址
     * @author Jack
     * @date 2020/2/24 21:53
     * @param  finalKey 文件名
     * @return cdn域名/文件名
     */
    String getPathUrl(String finalKey);
    /**
     * @desc 获取临时上传文件的链接，前端使用put直接上传文件即可
     * @author Jack
     * @date 2020/2/24 21:49
     * @param  finalKey 文件名
     * @param md5 文件的MD5
     * @param expireTime 连接过期时间
     * @return  上传的连接
     */
    String getTempPutFileUrl(String finalKey, String md5, long expireTime);
   /**
    * @desc 获取文件列表
    * @author Jack
    * @date 2020/2/24 21:51
    * @param  bTypeName 文件的一级地址
    * @param prefix 文件的二级地址
    * @return 文件列表
    */
    List<COSObjectSummary> listObjects(String bTypeName, String prefix);
    /**
     * @desc 判断文件是否存在
     * @author Jack
     * @date 2020/2/24 21:52
     * @param  key 文件名
     * @return 是否存在
     */
    boolean fileIsExist(String key);
    /**
     * @desc 获取sts临时授权信息
     * @author Jack
     * @date 2020/2/24 22:18
     * @param  allowPath 允许上传文件格式：1.jpg;a/*;*
     * @param expire 有效时间 秒
     * @return 授权信息
     */
    JSONObject getSTSToken(String allowPath,int expire);

}
