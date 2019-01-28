package cn.rui97.ruigou.controller;

import cn.rui97.ruigou.domain.Brand;
import cn.rui97.ruigou.service.ICommodityTypeService;
import cn.rui97.ruigou.domain.CommodityType;
import cn.rui97.ruigou.query.CommodityTypeQuery;
import cn.rui97.ruigou.util.AjaxResult;
import cn.rui97.ruigou.util.PageList;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/commodityType")
public class CommodityTypeController {
    @Autowired
    public ICommodityTypeService commodityTypeService;

    /**
    * 保存和修改公用的
    * @param commodityType  传递的实体
    * @return Ajaxresult转换结果
    */
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public AjaxResult save(@RequestBody CommodityType commodityType){
        try {
            if(commodityType.getId()!=null){
                commodityTypeService.updateById(commodityType);
            }else{
                commodityTypeService.insert(commodityType);
            }
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("保存对象失败！"+e.getMessage());
        }
    }

    /**
    * 删除对象信息
    * @param id
    * @return
    */
    @RequestMapping(value="/{id}",method=RequestMethod.DELETE)
    public AjaxResult delete(@PathVariable("id")Long id){
        try {
            commodityTypeService.deleteById(id);
            return AjaxResult.me();
        } catch (Exception e) {
        e.printStackTrace();
            return AjaxResult.me().setMessage("删除类型失败！"+e.getMessage());
        }
    }

    //获取用户
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public CommodityType get(@PathVariable("id")Long id)
    {
        return commodityTypeService.selectById(id);
    }


    /**
    * 查看所有的员工信息
    * @return
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public List<CommodityType> list(){

        return commodityTypeService.selectList(null);
    }

    /**
     * 查看所有的员工信息
     * @return
     */
    @RequestMapping(value = "/treeData",method = RequestMethod.GET)
    public List<CommodityType> treeData(){
        return commodityTypeService.treeData();
    }


    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @RequestMapping(value = "/json",method = RequestMethod.POST)
    public PageList<CommodityType> json(@RequestBody CommodityTypeQuery query) {
        Page<CommodityType> page = new Page<CommodityType>(query.getPage(),query.getRows());
            page = commodityTypeService.selectPage(page);
            return new PageList<CommodityType>(page.getTotal(),page.getRecords());
    }
    //获取面包屑

    //获取用户
    @RequestMapping(value = "/crumbs/{id}",method = RequestMethod.GET)
    public List<Map<String,Object>> getCrumbs(@PathVariable("id")Long commodityTypeId) {
        return commodityTypeService.getCrumbs(commodityTypeId);
    }

    //获取品牌
    @RequestMapping(value = "/brands/{id}",method = RequestMethod.GET)
    public List<Brand> getBrands(@PathVariable("id")Long commodityTypeId) {
        return commodityTypeService.getBrands(commodityTypeId);
    }

    //获取首字母
    @RequestMapping(value = "/brands/letters/{id}",method = RequestMethod.GET)
    public Set<String> getLetters(@PathVariable("id")Long commodityTypeId) {
        return commodityTypeService.getLetters(commodityTypeId);
    }
}
