package cn.rui97.ruigou.controller;

import cn.rui97.ruigou.service.ICommodityExtService;
import cn.rui97.ruigou.domain.CommodityExt;
import cn.rui97.ruigou.query.CommodityExtQuery;
import cn.rui97.ruigou.util.AjaxResult;
import cn.rui97.ruigou.util.PageList;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/commodityExt")
public class CommodityExtController {
    @Autowired
    public ICommodityExtService commodityExtService;

    /**
    * 保存和修改公用的
    * @param commodityExt  传递的实体
    * @return Ajaxresult转换结果
    */
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public AjaxResult save(@RequestBody CommodityExt commodityExt){
        try {
            if(commodityExt.getId()!=null){
                commodityExtService.updateById(commodityExt);
            }else{
                commodityExtService.insert(commodityExt);
            }
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setMessage("保存对象失败！"+e.getMessage());
        }
    }

    /**
    * 删除对象信息
    * @param id
    * @return
    */
    @RequestMapping(value="/{id}",method=RequestMethod.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id){
        try {
            commodityExtService.deleteById(id);
            return AjaxResult.me();
        } catch (Exception e) {
        e.printStackTrace();
            return AjaxResult.me().setMessage("删除对象失败！"+e.getMessage());
        }
    }

    //获取用户
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public CommodityExt get(@RequestParam(value="id",required=true) Long id)
    {
        return commodityExtService.selectById(id);
    }


    /**
    * 查看所有的员工信息
    * @return
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public List<CommodityExt> list(){

        return commodityExtService.selectList(null);
    }


    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @RequestMapping(value = "/json",method = RequestMethod.POST)
    public PageList<CommodityExt> json(@RequestBody CommodityExtQuery query)
    {
        Page<CommodityExt> page = new Page<CommodityExt>(query.getPage(),query.getRows());
            page = commodityExtService.selectPage(page);
            return new PageList<CommodityExt>(page.getTotal(),page.getRecords());
    }
}
