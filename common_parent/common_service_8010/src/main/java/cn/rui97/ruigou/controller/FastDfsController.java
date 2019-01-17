package cn.rui97.ruigou.controller;

import cn.rui97.ruigou.util.AjaxResult;
import cn.rui97.ruigou.utils.FastDfsApiOpr;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Auther: rui
 * @Date: 2019/1/17 12:55
 * @Description:
 */
@RestController
public class FastDfsController {

    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public AjaxResult upload(@RequestParam(value = "file",required = true) MultipartFile file){
        try {
            //文件名
            String originalFilename = file.getOriginalFilename();
            System.out.println(originalFilename+":"+file.getSize());
            //后坠
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            //文件路径
            String filePath = FastDfsApiOpr.upload(file.getBytes(), extName);
            return AjaxResult.me().setResultObj(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("上传失败！"+e.getMessage());
        }
    }
    @RequestMapping(value = "/del",method = RequestMethod.DELETE)
    public AjaxResult delete(@RequestParam(value = "filePath",required = true) String filePath){
        //去掉第一个斜杠
        String pathTmp = filePath.substring(1);
        //拿到组名
        String groupName = pathTmp.substring(0, pathTmp.indexOf("/"));
        //拿到文件路径
        String remotePath = pathTmp.substring(pathTmp.indexOf("/")+1);
        System.out.println(groupName);
        System.out.println(remotePath);
        FastDfsApiOpr.delete(groupName,remotePath);
        return AjaxResult.me();
    }
}
