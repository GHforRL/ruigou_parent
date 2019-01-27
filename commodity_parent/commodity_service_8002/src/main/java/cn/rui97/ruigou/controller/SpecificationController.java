package cn.rui97.ruigou.controller;

import cn.rui97.ruigou.domain.Commodity;
import cn.rui97.ruigou.service.ICommodityService;
import cn.rui97.ruigou.service.ISpecificationService;
import cn.rui97.ruigou.domain.Specification;
import cn.rui97.ruigou.query.SpecificationQuery;
import cn.rui97.ruigou.util.AjaxResult;
import cn.rui97.ruigou.util.PageList;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/specification")
public class SpecificationController {
    @Autowired
    public ISpecificationService specificationService;

    @Autowired
    private ICommodityService commodityService;

    /**
    * 保存和修改公用的
    * @param specification  传递的实体
    * @return Ajaxresult转换结果
    */
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public AjaxResult save(@RequestBody Specification specification){
        try {
            if(specification.getId()!=null){
                specificationService.updateById(specification);
            }else{
                specificationService.insert(specification);
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
    public AjaxResult delete(@PathVariable("id") Long id){
        try {
            specificationService.deleteById(id);
            return AjaxResult.me();
        } catch (Exception e) {
        e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("删除对象失败！"+e.getMessage());
        }
    }
//    //删除方法优化（可以删一条也可以删多条）
//    @RequestMapping(value="/{ids}",method=RequestMethod.DELETE)
//    public AjaxResult deleteMany(@PathVariable("ids") Long[] ids){
//        try {
//            for (Long id : ids) {
//                specificationService.deleteById(id);
//            }
//            return AjaxResult.me();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return AjaxResult.me().setMessage("删除对象失败！"+e.getMessage());
//        }
//    }

    //获取属性
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Specification get(@RequestParam(value="id",required=true) Long id){
        return specificationService.selectById(id);
    }


    /**
    * 查看所有的属性信息
    * @return
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public List<Specification> list(){

        return specificationService.selectList(null);
    }


    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @RequestMapping(value = "/json",method = RequestMethod.POST)
    public PageList<Specification> json(@RequestBody SpecificationQuery query){
        Page<Specification> page = new Page<Specification>(query.getPage(),query.getRows());
            page = specificationService.selectPage(page);
            return new PageList<Specification>(page.getTotal(),page.getRecords());
    }

    /**
     * 根据id查询所有的显示属性
     *    查询条件: id, isSku
     */
// /specification/commodity/{id}
    @RequestMapping(value = "/commodity/{commodityId}",method = RequestMethod.GET)
    public List<Specification> queryViewProperties(@PathVariable("commodityId") Long commodityId){
        Commodity commodity = commodityService.selectById(commodityId);
        String viewProperties = commodity.getViewProperties();
        //商品已经设置显示属性,直接从product表中获取(里面就有属性,又有数据)
        if (StringUtils.isNotBlank(viewProperties)){
            return JSONArray.parseArray(viewProperties, Specification.class);
        }else{
            EntityWrapper<Specification> wrapper = new EntityWrapper<>();
            wrapper.eq("commodity_type_id", commodity.getCommodityTypeId());
            wrapper.eq("isSku", 0);
            return  specificationService.selectList(wrapper);
        }
    }

    /**
     * 根据id查询所有的sku属性
     *    查询条件: id, isSku
     */
// /specification/commodity/{id}
    @RequestMapping(value = "/commodity/skuProperties/{commodityId}",method = RequestMethod.GET)
    public List<Specification> querySkuProperties(@PathVariable("commodityId") Long commodityId){

        //获取商品,尝试从里面获取sku_template
        Commodity commodity = commodityService.selectById(commodityId);
        String skuTemplate = commodity.getSkuTemplate();
        if (StringUtils.isNotBlank(skuTemplate)){
            return JSONArray.parseArray(skuTemplate, Specification.class);
        }
        //如果有直接转换返回,否则从属性表中查询
        //参数 类型ID 是否是sku
        EntityWrapper<Specification> w = new EntityWrapper<>();
        w.eq("commodity_type_id", commodity.getCommodityTypeId());
        w.eq("isSku", 1);
        return  specificationService.selectList(w);
    }

    /**
     * 根据类型Id该类型的获取配置属性
     */
    @RequestMapping(value = "/queryById/{commodityTypeId}",method = RequestMethod.GET)
    public List<Specification> querySpecifications(@PathVariable("commodityTypeId") Long commodityTypeId){
        return specificationService.getSpecificationsByTypeId(commodityTypeId);
    }

}
