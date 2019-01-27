package cn.rui97.ruigou.controller;

import cn.rui97.ruigou.client.CommodityDocClient;
import cn.rui97.ruigou.index.CommodityDoc;
import cn.rui97.ruigou.service.ICommodityDocService;
import cn.rui97.ruigou.util.AjaxResult;
import cn.rui97.ruigou.util.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Auther: rui
 * @Date: 2019/1/22 13:20
 * @Description:
 */
@RestController
@RequestMapping("/commodityDoc")
public class CommodityDocController implements CommodityDocClient {

    @Autowired
    private ICommodityDocService commodityDocService;

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public AjaxResult save(CommodityDoc commodityDoc) {
        try {
            commodityDocService.add(commodityDoc);
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("保存失败！"+e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public AjaxResult del(@PathVariable("id")Long id) {
        try {
            commodityDocService.del(id);
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("删除失败！"+e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public CommodityDoc get(@PathVariable("id") Long id) {
        return commodityDocService.get(id);
    }

    @RequestMapping(value = "/batchSave",method = RequestMethod.POST)
    public AjaxResult batchSave(@RequestBody List<CommodityDoc> commodityDocs) {
        try {
            commodityDocService.batchAdd(commodityDocs);
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("保存失败！"+e.getMessage());
        }
    }

    @RequestMapping(value = "/batchDel",method = RequestMethod.DELETE)
    public AjaxResult batchDel(@RequestBody List<Long> ids) {
        try {
            commodityDocService.batchDel(ids);
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("删除失败！"+e.getMessage());
        }
    }

    @RequestMapping(value = "/search",method = RequestMethod.POST)
    public PageList<Map<String,Object>> search(@RequestBody Map<String, Object> params) {
        return commodityDocService.search(params);
    }
}
