package cn.rui97.ruigou.controller;

import cn.rui97.ruigou.service.ICommodityService;
import cn.rui97.ruigou.domain.Commodity;
import cn.rui97.ruigou.query.CommodityQuery;
import cn.rui97.ruigou.util.AjaxResult;
import cn.rui97.ruigou.util.PageList;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/commodity")
public class CommodityController {
    @Autowired
    public ICommodityService commodityService;

    /**
    * 保存和修改公用的
    * @param commodity  传递的实体
    * @return Ajaxresult转换结果
    */
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public AjaxResult save(@RequestBody Commodity commodity){
        try {
            if(commodity.getId()!=null){
                commodityService.updateById(commodity);
            }else{
                commodityService.insert(commodity);
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
    @RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id){
        try {
            commodityService.deleteById(id);
            return AjaxResult.me();
        } catch (Exception e) {
        e.printStackTrace();
            return AjaxResult.me().setMessage("删除对象失败！"+e.getMessage());
        }
    }

    //获取用户
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Commodity get(@RequestParam(value="id",required=true) Long id)
    {
        return commodityService.selectById(id);
    }


    /**
    * 查看所有的员工信息
    * @return
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public List<Commodity> list(){

        return commodityService.selectList(null);
    }


    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @RequestMapping(value = "/json",method = RequestMethod.POST)
    public PageList<Commodity> json(@RequestBody CommodityQuery query)
    {
        Page<Commodity> page = new Page<Commodity>(query.getPage(),query.getRows());
            page = commodityService.selectPage(page);
            return new PageList<Commodity>(page.getTotal(),page.getRecords());
    }
}
