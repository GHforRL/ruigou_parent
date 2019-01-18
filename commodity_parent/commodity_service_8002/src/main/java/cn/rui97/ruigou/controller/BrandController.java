package cn.rui97.ruigou.controller;

import cn.rui97.ruigou.service.IBrandService;
import cn.rui97.ruigou.domain.Brand;
import cn.rui97.ruigou.query.BrandQuery;
import cn.rui97.ruigou.util.AjaxResult;
import cn.rui97.ruigou.util.PageList;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/brand")
public class BrandController {
    @Autowired
    public IBrandService brandService;

    /**
    * 保存和修改公用的
    * @param brand  传递的实体
    * @return Ajaxresult转换结果
    */
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public AjaxResult save(@RequestBody Brand brand){
        try {
            if(brand.getId()!=null){
                brandService.updateById(brand);
            }else{
                brandService.insert(brand);
            }
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setMessage("保存对象失败！"+e.getMessage());
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
//            brandService.deleteById(id);
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
                brandService.deleteById(id);
            }
            return AjaxResult.me();
        } catch (Exception e) {
        e.printStackTrace();
            return AjaxResult.me().setMessage("删除对象失败！"+e.getMessage());
        }
    }



    //获取用户
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Brand get(@PathVariable(value="id",required=true) Long id)
    {
        return brandService.selectById(id);
    }


    /**
    * 查看所有的员工信息
    * @return
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public List<Brand> list(){

        return brandService.selectList(null);
    }


    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @RequestMapping(value = "/json",method = RequestMethod.POST)
    public PageList<Brand> json(@RequestBody BrandQuery query)
    {
        return  brandService.selectPageList(query);
        //原生的mybatisplus不支持良好关联查询,还是要用传统方式,restultMap
//        Page<Brand> page = new Page<Brand>(query.getPage(),query.getRows());
//            page = brandService.selectPage(page);
//            return new PageList<Brand>(page.getTotal(),page.getRecords());
    }
}
