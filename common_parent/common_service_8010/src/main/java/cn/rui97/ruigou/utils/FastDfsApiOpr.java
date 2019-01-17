package cn.rui97.ruigou.utils;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;

/**
 * @Auther: rui
 * @Date: 2019/1/17 11:10
 * @Description:
 */
public class FastDfsApiOpr {

    public static String CONF_FILENAME = FastDfsApiOpr.class.getClassLoader().getResource("fast_client.conf").getFile();

    /**
     * 上传文件
     * @param extName
     * @return
     */
    public static  String upload(String path,String extName) {

        try {
            StorageClient storageClient = getStorageClient();
            String fileIds[] = storageClient.upload_file(path, extName,null);

            System.out.println(fileIds.length);
            System.out.println("组名：" + fileIds[0]);
            System.out.println("路径: " + fileIds[1]);
            return  "/"+fileIds[0]+"/"+fileIds[1];

        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }
    /**
     * 上传文件
     * @param extName
     * @return
     */
    public static  String upload(byte[] file,String extName) {

        try {
            StorageClient storageClient = getStorageClient();
//            NameValuePair nvp [] = new NameValuePair[]{
//                    new NameValuePair("Auther", "rui"),
//            };
            String fileIds[] = storageClient.upload_file(file, extName,null);

            System.out.println(fileIds.length);
            System.out.println("组名：" + fileIds[0]);
            System.out.println("路径: " + fileIds[1]);
            return  "/"+fileIds[0]+"/"+fileIds[1];

        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }

    /**
     * 下载文件
     * @param groupName
     * @param fileName
     * @return
     */
    public static byte[] download(String groupName,String fileName) {
        try {

            StorageClient storageClient = getStorageClient();
            byte[] b = storageClient.download_file(groupName, fileName);
            return  b;
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }

    /**
     * 删除文件
     * @param groupName
     * @param fileName
     */
    public static void delete(String groupName,String fileName){
        try {
            StorageClient storageClient = getStorageClient();
            int i = storageClient.delete_file(groupName,fileName);
            System.out.println( i==0 ? "删除成功" : "删除失败:"+i);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("删除异常,"+e.getMessage());
        }
    }

    private static StorageClient getStorageClient() throws Exception {
        ClientGlobal.init(CONF_FILENAME);

        TrackerClient tracker = new TrackerClient();
        TrackerServer trackerServer = tracker.getConnection();
        StorageServer storageServer = null;
        return new StorageClient(trackerServer, storageServer);
    }
}
