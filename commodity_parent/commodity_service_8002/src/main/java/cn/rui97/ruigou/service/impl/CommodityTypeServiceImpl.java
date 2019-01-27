package cn.rui97.ruigou.service.impl;

import cn.rui97.ruigou.client.PageClient;
import cn.rui97.ruigou.client.RedisClient;
import cn.rui97.ruigou.domain.Brand;
import cn.rui97.ruigou.domain.CommodityType;
import cn.rui97.ruigou.mapper.BrandMapper;
import cn.rui97.ruigou.mapper.CommodityTypeMapper;
import cn.rui97.ruigou.service.ICommodityTypeService;
import cn.rui97.ruigou.util.StrUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

/**
 * <p>
 * 商品目录 服务实现类
 * </p>
 *
 * @author lurui
 * @since 2019-01-13
 */
@Service
public class CommodityTypeServiceImpl extends ServiceImpl<CommodityTypeMapper, CommodityType> implements ICommodityTypeService {

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private PageClient pageClient;

    @Autowired
    private CommodityTypeMapper commodityTypeMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Override
    public List<CommodityType> treeData() {
        String commodityTypeInRedis = redisClient.get("commodityType_in_redis");
        if (StringUtils.isNotBlank(commodityTypeInRedis)) {
            System.out.println("cache....");
            return JSONArray.parseArray(commodityTypeInRedis, CommodityType.class);
        } else {

            System.out.println("db....");
            // 1 递归方案效率低,要发多次sql
            //return getTreeDataRecursion(0L);
            // 2 循环方案,只需发一条sql
            List<CommodityType> treeDataByDb = getTreeDataLoop(0L);
            redisClient.set("commodityType_in_redis", JSONArray.toJSONString(treeDataByDb, SerializerFeature.WriteMapNullValue));
            return treeDataByDb;
        }
    }

    @Override
    public List<Map<String, Object>> getCrumbs(Long commodityTypeId) {
        List<Map<String,Object>> result = new ArrayList<>();
        //1 通过commodityTypeId查commodityType,从里面获取path .1.2.3.
        CommodityType commodityType = commodityTypeMapper.selectById(commodityTypeId);
        String path = commodityType.getPath();
        System.out.println(path);
        //2 分割path获取层级id集合,遍历每一级的id 1 2 3
        path = path.substring(1, path.length()-1); //1.2.3
        List<Long> ids = StrUtils.splitStr2LongArr(path,"\\.");
        System.out.println(ids);
        for (Long id : ids) {
            Map<String,Object> node = new HashMap<>();
            //3 使用遍历id构造一个节点
            //3.1 获取自己 通过id查询就ok
            CommodityType owner = commodityTypeMapper.selectById(id);
            node.put("ownerCommodityType", owner);
            //3.2 通过自己pid,查询所有的儿子,再删除自己
            Long pid = owner.getPid();
            List<CommodityType> parentAllChildren = commodityTypeMapper
                    .selectList(new EntityWrapper<CommodityType>().eq("pid", pid));
            Iterator<CommodityType> iterator = parentAllChildren.iterator();
            while (iterator.hasNext()){
                CommodityType curent = iterator.next();
                if (curent.getId().longValue()== owner.getId().longValue()){
                    //父亲儿子中我,要干掉.
                    iterator.remove();
                    break;
                }
            }
            node.put("otherCommodityTypes", parentAllChildren);
            result.add(node);
        }

        return result;
    }

    @Override
    public List<Brand> getBrands(Long commodityTypeId) {
        return brandMapper.selectList(
                new EntityWrapper<Brand>().eq("commodity_type_id", commodityTypeId));
    }

    @Override
    public Set<String> getLetters(Long commodityTypeId) {
        Set<String> result = new TreeSet<>(); //不重复且排序集合
        List<Brand> brands = getBrands(commodityTypeId);
        for (Brand brand : brands) {
            result.add(brand.getFirstLetter());
        }
        return result;
    }

    //增删改已经不是传统的了,要做同步redis-清空缓存,下次查询时,自动查询数据库
    @Override
    public boolean insert(CommodityType entity) {

        //方案1:
        //redisClient.set("commodityType_in_redis", "");
        //方案2:为了配置页面静态,这种方案还要好一点
        super.insert(entity); //先做增删改,再做同步
        Long id = entity.getId();
        CommodityType pType = commodityTypeMapper.selectById(entity.getPid());
        String path=pType.getPath()+id+".";
        entity.setPath(path);
        super.updateById(entity);
        synchronizedOpr();
        return true;
    }

    @Override
    public boolean deleteById(Serializable id) {
        //redisClient.set("commodityType_in_redis", "");
        super.deleteById(id);
        synchronizedOpr();
        return true;
    }

    @Override
    public boolean updateById(CommodityType entity) {
        //redisClient.set("commodityType_in_redis", "");
        super.updateById(entity);
        synchronizedOpr();
        return true;
    }


    //增删改让商品类型发生改变,都要重新生成静态页面还要更新缓存
    private void synchronizedOpr(){
        //更新缓存
        List<CommodityType> allCommodityType = getTreeDataLoop(0L);
        redisClient.set("commodityType_in_redis", JSONArray.toJSONString(allCommodityType));
        //是否每次都要从数据库查询---是的,就可以优化原来代码,同步修改缓存. 就不用查询的时候从数据库获取
        //先静态化类型
        Map<String,Object> commodityTypeParams = new HashMap<>();
        Object model = null;
        commodityTypeParams.put("model",allCommodityType );
        commodityTypeParams.put("tmeplatePath","F:\\Github\\ruigou_parent\\commodity_parent\\commodity_service_8002\\src\\main" +
                "\\resources\\template\\commodityType\\commodity.type.vm" );
        commodityTypeParams.put("staticPagePath","F:\\Github\\ruigou_parent\\commodity_parent\\commodity_service_8002\\src\\main" +
                "\\resources\\template\\commodityType\\commodity.type.vm.html" );
        pageClient.genStaticPage(commodityTypeParams);
        //在静态化主页
        Map<String,Object> IndexParams = new HashMap<>();
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("staticRoot", "F:\\Github\\ruigou_parent\\commodity_parent\\commodity_service_8002\\src\\main\\resources\\");
        IndexParams.put("model",modelMap );
        IndexParams.put("tmeplatePath","F:\\Github\\ruigou_parent\\commodity_parent\\commodity_service_8002\\src\\main\\resources\\template\\home.vm" );
        IndexParams.put("staticPagePath","F:\\Github\\ruigou_web_parent\\ruigou_shopping\\home.html" );
        pageClient.genStaticPage(IndexParams);

    }

    private List<CommodityType> getTreeDataLoop(long l) {
        //返回数据 一级类型,下面挂了子子孙孙类型
        List<CommodityType> result = new ArrayList<>();
        //1 获取所有的类型
        List<CommodityType> commodityTypes = commodityTypeMapper.selectList(null);
        //2)遍历所有的类型
        Map<Long, CommodityType> commodityTypesDto = new HashMap<>();
        for (CommodityType commodityType : commodityTypes) {
            commodityTypesDto.put(commodityType.getId(), commodityType);
        }
        for (CommodityType commodityType : commodityTypes) {
            Long pid = commodityType.getPid();
            // ①如果没有父亲就是一级类型 放入返回列表中
            if (pid.longValue() == 0) {
                result.add(commodityType);
            } else {
                // ②如果有父亲就是把自己当做一个儿子就ok
                //方案1:遍历比较所有所有里面获取 两层for 10*10
//                for (CommodityType commodityTypeTmp : commodityTypes) { 1 10 2 10 310 40 10
//                    if (commodityTypeTmp.getId()==pid){
//                        commodityTypeTmp.getChildren().add(commodityType);
//                    }
//                }
                //方案2:通过Map建立id和类型直接关系,以后通过pid直接获取父亲 10+10
                CommodityType parent = commodityTypesDto.get(pid);
                parent.getChildren().add(commodityType);
            }

        }
        return result;
    }
}
