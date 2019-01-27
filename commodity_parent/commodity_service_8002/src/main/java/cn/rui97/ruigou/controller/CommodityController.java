package cn.rui97.ruigou.controller;

import cn.rui97.ruigou.client.CommodityDocClient;
import cn.rui97.ruigou.domain.Sku;
import cn.rui97.ruigou.domain.Specification;
import cn.rui97.ruigou.service.ICommodityService;
import cn.rui97.ruigou.domain.Commodity;
import cn.rui97.ruigou.query.CommodityQuery;
import cn.rui97.ruigou.util.AjaxResult;
import cn.rui97.ruigou.util.PageList;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/commodity")
public class CommodityController {
    @Autowired
    public ICommodityService commodityService;

    @Autowired
    public CommodityDocClient commodityDocClient;

    @RequestMapping(value="/queryCommoditys",method= RequestMethod.POST)
    public PageList<Map<String,Object>> query(@RequestBody Map<String,Object> query){
        //从es中查询
        //query  keyword productyType brandId priceMin priceMax sortField sortType page rows
        //使用Map后,不用拷贝productDoc和Product直接转换
        return commodityDocClient.search(query);
    }

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

    @RequestMapping(value="/onSale",method= RequestMethod.POST)
    public AjaxResult onSale(@RequestBody Map<String,Object> params){
        try {
            String ids = (String) params.get("ids");
            System.out.println(ids);
            Integer onSale = Integer.valueOf(params.get("onSale").toString());
            System.out.println(onSale);
            commodityService.onSale(ids,onSale);
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("上下架失败！"+e.getMessage());
        }
    }

    /**
    * 删除对象信息
    * @param ids
    * @return
    */
//    @RequestMapping(value="/{id}",method=RequestMethod.DELETE)
//    public AjaxResult delete(@PathVariable("id") Long id){
//        try {
//            commodityService.deleteById(id);
//            return AjaxResult.me();
//        } catch (Exception e) {
//        e.printStackTrace();
//            return AjaxResult.me().setMessage("删除对象失败！"+e.getMessage());
//        }
//    }
    //删除方法优化（可以删一条也可以删多条）
    @RequestMapping(value="/{ids}",method=RequestMethod.DELETE)
    public AjaxResult deleteMany(@PathVariable("ids") Long[] ids){
        try {
            for (Long id : ids) {
                commodityService.deleteById(id);
            }
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setMessage("删除对象失败！"+e.getMessage());
        }
    }

    //获取用户
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Commodity get(@PathVariable(value="id",required=true) Long id)
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
    public PageList<Commodity> json(@RequestBody CommodityQuery query){
        return commodityService.selectPageList(query);
    }

    /**
     * 保存和修改公用的
     * @param params  传递的实体
     * @return Ajaxresult转换结果
     */
    @RequestMapping(value="/addViewProperties",method= RequestMethod.POST)
    public AjaxResult save(@RequestBody Map<String,Object> params){
        try {
            Integer tmp = (Integer) params.get("commodityId"); //Integer
            Long commodityId = Long.parseLong(tmp.toString());
            List<Specification> specifications = (List<Specification>) params.get("specifications");
            System.out.println(specifications);
            commodityService.addViewProperties(commodityId,specifications);
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setMessage("保存显示属性失败！"+e.getMessage());
        }
    }

    /**
     * 添加sku
     * @param params  传递的实体
     * @return Ajaxresult转换结果
     */
    @RequestMapping(value="/addSkus",method= RequestMethod.POST)
    public AjaxResult addSkus(@RequestBody Map<String,Object> params){
        try {
            Integer tmp = (Integer) params.get("commodityId"); //Integer
            Long commodityId = Long.parseLong(tmp.toString());
            //[{id:1,name:xxx,skuValus[]},id:1,name:xxx,skuValus[]}]
            List<Map<String,Object>> skuProperties = (List<Map<String,Object>>) params.get("skuProperties");
            //skuDatas [{'身高':18,"price":18},{'身高':18,"price":18}]
            List<Map<String,Object>> skuDatas = (List<Map<String,Object>>) params.get("skuDatas");
            //sku{id:xx,price:xxx,skuValues:[{id,key,value},{}],indexs:1_2_3}
            commodityService.addSkus(commodityId,skuProperties,skuDatas);
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("保存sku属性失败！"+e.getMessage());
        }
    }


    @RequestMapping(value = "/skus/{commodityId}",method = RequestMethod.GET)
    public List<Map<String,Object>> querySkus(@PathVariable("commodityId") Long commodityId){
        List<Sku> skus = commodityService.querySkus(commodityId);
        List<Map<String,Object>> result = new ArrayList<>();
        //把skuValues要进行转换
        for (Sku sku : skus) {
            result.add(sku2map(sku));
        }
        return result;
    }

    private Map<String,Object> sku2map(Sku sku) {
        Map<String,Object> result = new LinkedHashMap<>();
//        result.put("id",sku.getId() );
//        result.put("commodityId",sku.getcommodityId() );
//        result.put("indexs",sku.getIndexs() );
        //[{"id":37,"key":"三维","value":"d"},{"id":36,"key":"身高","value":"c"},{"id":35,"key":"肤色","value":"a"}]
        String skuValues = sku.getSkuValues();
        List<Map> maps = JSONArray.parseArray(skuValues, Map.class);
//        List<Map> maps = JSONArray.parseArray(skuValues, Map.class);
        //{"id":37,"key":"三维","value":"d"}
        for (Map map : maps) {
            String key = (String) map.get("key");
            String value = (String) map.get("value");
            result.put(key, value);
        }
        result.put("price",sku.getPrice() );
        result.put("stock",sku.getStock() );
        result.put("state",sku.getState() );
        return result;
    }
}
