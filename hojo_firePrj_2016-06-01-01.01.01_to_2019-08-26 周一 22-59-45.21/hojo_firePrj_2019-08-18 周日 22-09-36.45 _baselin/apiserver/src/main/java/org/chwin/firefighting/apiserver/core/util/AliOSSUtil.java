package org.chwin.firefighting.apiserver.core.util;


import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import org.chwin.firefighting.apiserver.core.constants.OSSClientConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class AliOSSUtil {
    private static final Logger logger = LoggerFactory.getLogger(AliOSSUtil.class);

    //阿里云API的内或外网域名
    private static String ENDPOINT;
    //阿里云API的密钥Access Key ID
    private static String ACCESS_KEY_ID;
    //阿里云API的密钥Access Key Secret
    private static String ACCESS_KEY_SECRET;
    //阿里云API的bucket名称
    public static String BACKET_NAME;
    //阿里云API的文件夹名称
    public static String FOLDER;
    //上传后的url
    public static String IMAGE_URL;
    //初始化属性
    static{
        ENDPOINT = OSSClientConstants.ENDPOINT;
        ACCESS_KEY_ID = OSSClientConstants.ACCESS_KEY_ID;
        ACCESS_KEY_SECRET = OSSClientConstants.ACCESS_KEY_SECRET;
        BACKET_NAME = OSSClientConstants.BACKET_NAME;
        FOLDER = OSSClientConstants.FOLDER;
        IMAGE_URL = OSSClientConstants.IMAGE_URL;
    }

    /**
     * 获取阿里云OSS客户端对象
     * @return ossClient
     */
    public static  OSSClient getOSSClient(){
        return new OSSClient(ENDPOINT,ACCESS_KEY_ID, ACCESS_KEY_SECRET);
    }

    /**
     * 创建存储空间
     * @param ossClient      OSS连接
     * @param bucketName 存储空间
     * @return
     */
    public  static String createBucketName(OSSClient ossClient,String bucketName){
        //存储空间
        final String bucketNames=bucketName;
        if(!ossClient.doesBucketExist(bucketName)){
            //创建存储空间
            Bucket bucket=ossClient.createBucket(bucketName);
            return bucket.getName();
        }
        return bucketNames;
    }

    /**
     * 删除存储空间buckName
     * @param ossClient  oss对象
     * @param bucketName  存储空间
     */
    public static  void deleteBucket(OSSClient ossClient, String bucketName){
        ossClient.deleteBucket(bucketName);
    }

    /**
     * 创建模拟文件夹
     * @param ossClient oss连接
     * @param bucketName 存储空间
     * @return  文件夹名
     */
    public  static String createFolder(OSSClient ossClient,String bucketName,String folder){
        //文件夹名
        final String keySuffixWithSlash =folder;
        //判断文件夹是否存在，不存在则创建
        if(!ossClient.doesObjectExist(bucketName, keySuffixWithSlash)){
            //创建文件夹
            ossClient.putObject(bucketName, keySuffixWithSlash, new ByteArrayInputStream(new byte[0]));
            //得到文件夹名
            OSSObject object = ossClient.getObject(bucketName, keySuffixWithSlash);
            String fileDir=object.getKey();
            return fileDir;
        }
        return keySuffixWithSlash;
    }

    /**
     * 根据key删除OSS服务器上的文件
     * @param ossClient  oss连接
     * @param bucketName  存储空间
     */
    public static void deleteFile(OSSClient ossClient, String bucketName, String folder, String key){
        ossClient.deleteObject(bucketName, folder + key);
    }

    /**
     * 上传图片至OSS
     * @param ossClient  oss连接
     * @param file 上传文件（文件全路径如：D:\\image\\cake.jpg）
     * @param bucketName  存储空间
     * @param folder 模拟文件夹名 如"qj_nanjing/"
     * @return String 返回的唯一MD5数字签名
     * */
   public static  String uploadObject2OSS(OSSClient ossClient, File file, String bucketName, String folder) {
        String resultStr = null;
        try {
            //以输入流的形式上传文件
            InputStream is = new FileInputStream(file);
            //文件名
            String fileName = file.getName();
            //文件大小
            //Long fileSize = file.length();
            //创建上传Object的Metadata
            ObjectMetadata metadata = new ObjectMetadata();
            //上传的文件的长度
            metadata.setContentLength(is.available());
            //指定该Object被下载时的网页的缓存行为
            metadata.setCacheControl("no-cache");
            //指定该Object下设置Header
            metadata.setHeader("Pragma", "no-cache");
            //指定该Object被下载时的内容编码格式
            metadata.setContentEncoding("utf-8");
            //文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，
            //如果没有扩展名则填默认值application/octet-stream
            metadata.setContentType(getContentType(fileName));
            //指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）
            //setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte.");
            //上传文件   (上传文件流的形式)
            // 设置文件路径和名称
            String fileUrl = folder +"/"+ fileName;
            // 上传文件
//            PutObjectResult putResult = ossClient.putObject(new PutObjectRequest(bucketName, fileUrl, file));
            PutObjectResult putResult = ossClient.putObject(bucketName, fileUrl, is, metadata);
            //解析结果
            resultStr = putResult.getETag();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultStr;
    }
    public static  String uploadObject2OSS(OSSClient ossClient, InputStream inputStream,String name,String bucketName, String folder) {
        String resultStr = null;
        try {
            //以输入流的形式上传文件
            InputStream is = inputStream;
            //文件名
            String fileName = name;
            //文件大小
            //Long fileSize = size;
            //创建上传Object的Metadata
            ObjectMetadata metadata = new ObjectMetadata();
            //上传的文件的长度
            metadata.setContentLength(is.available());
            //指定该Object被下载时的网页的缓存行为
            metadata.setCacheControl("no-cache");
            //指定该Object下设置Header
            metadata.setHeader("Pragma", "no-cache");
            //指定该Object被下载时的内容编码格式
            metadata.setContentEncoding("utf-8");
            //文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，
            //如果没有扩展名则填默认值application/octet-stream
            metadata.setContentType(getContentType(fileName));
            //指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）
            //metadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte.");
            //上传文件   (上传文件流的形式)
            logger.info("开始调用上传文件接口bucketName............."+bucketName);
            logger.info("开始调用上传文件接口folder + fileName............."+folder + fileName);
            PutObjectResult putResult = ossClient.putObject(bucketName, folder + fileName, is, metadata);
            logger.info("上传返回结果resultStr........."+putResult.getETag());
            //解析结果
            resultStr = putResult.getETag();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultStr;
    }

    public static  void loadObjectFromOSS(OSSClient ossClient,String objectName,String bucketName){
        // ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
        OSSObject ossObject = ossClient.getObject(bucketName, objectName);
        // 读取文件内容。
                System.out.println("Object content:");
                BufferedReader reader = new BufferedReader(new InputStreamReader(ossObject.getObjectContent()));
                while (true) {
                    String line = null;
                    try {
                        line = reader.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (line == null) break;
                    System.out.println("\n" + line);
                }
        // 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 关闭OSSClient。
            ossClient.shutdown();
    }

    /**
     * 通过文件名判断并获取OSS服务文件上传时文件的contentType
     * @param fileName 文件名
     * @return 文件的contentType
     */
    public static  String getContentType(String fileName){
        //文件的后缀名
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        if(".bmp".equalsIgnoreCase(fileExtension)) {
            return "image/bmp";
        }
        if(".gif".equalsIgnoreCase(fileExtension)) {
            return "image/gif";
        }
        if(".jpeg".equalsIgnoreCase(fileExtension) || ".jpg".equalsIgnoreCase(fileExtension)  || ".png".equalsIgnoreCase(fileExtension) ) {
            return "image/jpeg";
        }
        if(".html".equalsIgnoreCase(fileExtension)) {
            return "text/html";
        }
        if(".txt".equalsIgnoreCase(fileExtension)) {
            return "text/plain";
        }
        if(".vsd".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.visio";
        }
        if(".ppt".equalsIgnoreCase(fileExtension) || ".pptx".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.ms-powerpoint";
        }
        if(".doc".equalsIgnoreCase(fileExtension) || ".docx".equalsIgnoreCase(fileExtension)) {
            return "application/msword";
        }
        if(".xml".equalsIgnoreCase(fileExtension)) {
            return "text/xml";
        }
        if(".pdf".equalsIgnoreCase(fileExtension)) {
            return "application/pdf";
        }
        //默认返回类型
        return "image/jpeg";
    }

    //测试
    public static void main(String[] args) {
        //初始化OSSClient
        OSSClient ossClient = AliOSSUtil.getOSSClient();
        //上传文件
        String files="C:\\Users\\Administrator\\Desktop\\1111.png";
        /*for(String filename:file){
            //System.out.println("filename:"+filename);
            File filess=new File(filename);
            String md5key = AliOSSUtil.uploadObject2OSS(ossClient, filess, BACKET_NAME, FOLDER);
            //上传后的文件MD5数字唯一签名:40F4131427068E08451D37F02021473A
        }*/
        File file=new File(files);
        Date curTime = new Date();
        SimpleDateFormat folder = new SimpleDateFormat("yyyyMMddhh");
        //String md5key = AliOSSUtil.uploadObject2OSS(ossClient, file, BACKET_NAME, FOLDER);
        String md5key = AliOSSUtil.uploadObject2OSS(ossClient,file,AliOSSUtil.BACKET_NAME,AliOSSUtil.FOLDER+folder.format(curTime));
//        System.out.print(AliOSSUtil.IMAGE_URL +AliOSSUtil.FOLDER+folder.format(curTime)+ id + ext;);
        //AliOSSUtil.loadObjectFromOSS(ossClient,ACCESS_KEY_ID,BACKET_NAME);
        deleteFile(ossClient,BACKET_NAME,FOLDER,"1111.png");
    }
}

