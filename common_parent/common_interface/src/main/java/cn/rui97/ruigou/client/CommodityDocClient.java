package cn.rui97.ruigou.client;

import cn.rui97.ruigou.index.CommodityDoc;
import cn.rui97.ruigou.util.AjaxResult;
import cn.rui97.ruigou.util.PageList;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * @Auther: rui
 * @Date: 2019/1/22 13:04
 * @Description:
 */
@FeignClient(value = "RUIGOU-COMMON",fallbackFactory = CommodityDocClientFallbackFactory.class )//服务提供者的名称
@RequestMapping("/commodityDoc")
public interface CommodityDocClient {
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    AjaxResult save(CommodityDoc commodityDoc); //添加和修改

    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    AjaxResult del(@PathVariable("id") Long id); //删除

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    CommodityDoc get(@PathVariable("id") Long id); //获取一个
    //批量操作
    @RequestMapping(value = "/batchSave",method = RequestMethod.POST)
    AjaxResult batchSave(@RequestBody List<CommodityDoc> commodityDocs); //批量添加
    @RequestMapping(value = "/batchDel",method = RequestMethod.DELETE)
    AjaxResult batchDel(@RequestBody List<Long> ids); //批量上传
    //分页搜索
    @RequestMapping(value = "/search",method = RequestMethod.POST)
    PageList<Map<String,Object>> search(@RequestBody Map<String,Object> params); //搜索
}
