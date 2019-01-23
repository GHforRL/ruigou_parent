package cn.rui97.ruigou.service.impl;

import cn.rui97.ruigou.client.CommodityDocClient;
import cn.rui97.ruigou.domain.*;
import cn.rui97.ruigou.index.CommodityDoc;
import cn.rui97.ruigou.mapper.*;
import cn.rui97.ruigou.query.CommodityQuery;
import cn.rui97.ruigou.service.ICommodityService;
import cn.rui97.ruigou.util.PageList;
import cn.rui97.ruigou.util.StrUtils;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

/**
 * <p>
 * 商品 服务实现类
 * </p>
 *
 * @author lurui
 * @since 2019-01-18
 */
@Service
public class CommodityServiceImpl extends ServiceImpl<CommodityMapper, Commodity> implements ICommodityService {

    @Autowired
    private CommodityMapper commodityMapper;

    @Autowired
    private CommodityExtMapper commodityExtMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private CommodityTypeMapper commodityTypeMapper;

    @Autowired
    private CommodityDocClient commodityDocClient;

    @Override
    public PageList<Commodity> selectPageList(CommodityQuery query) {
        Page<Commodity> page = new Page<>(query.getPage(),query.getRows());
        List<Commodity> data = commodityMapper.loadPageData(page, query);
        long total = page.getTotal();
        return new PageList<>(total,data);
    }

    @Override
    public void addViewProperties(Long commodityId, List<Specification> specifications) {
        String viewProperties = JSONArray.toJSONString(specifications);
        System.out.println(viewProperties);
        Commodity commodity = commodityMapper.selectById(commodityId);
        commodity.setViewProperties(viewProperties);
        commodityMapper.updateById(commodity);
    }

    @Override
    public void addSkus(Long commodityId, List<Map<String, Object>> skuProperties, List<Map<String, Object>> skuDatas) {
        Commodity commodity = commodityMapper.selectById(commodityId);
        //skuProperties修改到commodity
        commodity.setSkuTemplate(JSONArray.toJSONString(skuProperties));
        commodityMapper.updateById(commodity);
        //skuDatas放入sku表
        //1 删除原来的
        EntityWrapper<Sku> wapper = new EntityWrapper<>();
        wapper.eq("commodityId", commodityId);
        skuMapper.delete(wapper);

        //2 插入新的
        Map<String,Object> otherProp = new HashMap<>();
        for (Map<String, Object> skuData : skuDatas) {
            Sku sku = new Sku();
            //处理了四个字段
            sku.setCommodityId(commodityId);
            for (String key : skuData.keySet()) {
                //price,stock,state是直接传递进来
                if ("price".equals(key)){
                    sku.setPrice(Integer.valueOf(skuData.get(key).toString()));
                }
                else  if ("stock".equals(key)){
                    sku.setStock(Integer.valueOf(skuData.get(key).toString()));
                }
                else if ("state".equals(key)){
                    Integer state = (Integer) skuData.get(key);
                    sku.setState(state==1?true:false);
                }else{
                    //others 升高 三维
                    otherProp.put(key, skuData.get(key));
                }

            }
            //处理其他值
            List<Map<String,Object>> tmps = new ArrayList<>();
            for (String key : otherProp.keySet()) {
                Map<String,Object> map = new HashMap<>();
                String properName = key;
                Long properId = getProId(skuProperties,properName);//TODO
                Object proValue = otherProp.get(key);//TODO
                map.put("id", properId);
                map.put("key", properName);
                map.put("value", proValue);
                tmps.add(map);
            }
            String skuValues = JSONArray.toJSONString(tmps);
            sku.setSkuValues(skuValues);

            //indexDexs 1_2_3
            StringBuilder sb = new StringBuilder();
            for (Map<String, Object> map : tmps) {
                Long id = (Long) map.get("id"); //1 定位是哪个属性
                String value = String.valueOf(map.get("value").toString()) ; //定位是哪个选项
                Integer index = getIndex(skuProperties,id,value);
                sb.append(index).append("_");
            }
            String indexDexs =  sb.toString();
            indexDexs=  indexDexs.substring(0, indexDexs.lastIndexOf("_"));
            sku.setIndexs(indexDexs);
            skuMapper.insert(sku);
        }
    }

    @Override
    public List<Sku> querySkus(Long commodityId) {
        Wrapper<Sku> w = new EntityWrapper<>();
        w.eq("commodityId", commodityId);
        return   skuMapper.selectList(w);
    }

    @Override
    public void onSale(String ids, Integer onSale) {
        List<Long> idsLong = StrUtils.splitStr2LongArr(ids);
        if (1==onSale.intValue()) {
//            上架
//            根据id修改上架时间和数据库状态
            HashMap<String, Object> params = new HashMap<>();
            params.put("ids",idsLong);
            params.put("timeStamp",new Date().getTime());
            commodityMapper.onSale(params);
            List<CommodityDoc> commodityDocs = commodity2commodityDocs(idsLong);
            commodityDocClient.batchSave(commodityDocs);
        }else {
            HashMap<String, Object> params = new HashMap<>();
            params.put("ids",idsLong);
            params.put("timeStamp",new Date().getTime());
            commodityMapper.offSale(params);
            commodityDocClient.batchDel(idsLong);
        }
    }

    /**
     *  从所有的属性(里面包含选项)拿到某个属性某个选项索引值.
     * @param skuProperties 所有的属性(里面包含选项)
     * @param proId 要获取哪个属性
     * @param value 哪个选项
     * @return
     */
    private Integer getIndex(List<Map<String, Object>> skuProperties, Long proId, String value) {

        for (Map<String, Object> skuProperty : skuProperties) {
            Long proIdTmp = Long.valueOf(skuProperty.get("id").toString());
            if (proIdTmp.longValue() == proId.longValue()){
                //找到属性的选项
                List<String> skuValues = (List<String>) skuProperty.get("skuValues");
                int index = 0;
                for (String skuValue : skuValues) {
                    if(skuValue.equals(value)){
                        return index;
                    }
                    index++;
                }
            }

        }
        return null;
    }

    private Long getProId(List<Map<String,Object>> skuProperties, String properName) {
        for (Map<String,Object> skuProperty : skuProperties) {
            Long spId = Long.valueOf(skuProperty.get("id").toString()) ;
            String spName = (String) skuProperty.get("name");
            if (properName.equals(spName)){
                return spId;
            }
        }
        return null;
    }
    @Override
    public boolean insert(Commodity entity) {
        //添加本表信息以外,还要存放关联表
        entity.setCreateTime(new Date().getTime());
        commodityMapper.insert(entity);
        System.out.println(entity.getId());
        if (entity.getCommodityExt() != null) {
            entity.getCommodityExt().setCommodityId(entity.getId());
            commodityExtMapper.insert(entity.getCommodityExt());
        }
        return true;
    }

    @Override
    public boolean updateById(Commodity entity) {
        //添加本表信息以外,还要存放关联表
        entity.setUpdateTime(new Date().getTime());
        commodityMapper.updateById(entity);


        //通过commodityId查询commodityExt
        Wrapper<CommodityExt> wrapper = new EntityWrapper<CommodityExt>()
                .eq("commodityId", entity.getId());
        CommodityExt commodityExt = commodityExtMapper.selectList(wrapper).get(0);

        //把前台传递进来值设置给数据库查询出来值,并且把它修改进去
        CommodityExt tmp = entity.getCommodityExt();
        if ( tmp!= null) {
            commodityExt.setDescription(tmp.getDescription());
            commodityExt.setRichContent(tmp.getRichContent());
            commodityExtMapper.updateById(commodityExt);
        }
        return true;
    }

    @Override
    public boolean deleteById(Serializable id) {
        super.deleteById(id);
        //如果是上架状态,要同步删除es库
        Commodity commodity = commodityMapper.selectById(id);
        if (commodity.getState()==1){
            commodityDocClient.del(Long.valueOf(id.toString()));
        }
        return true;
    }

    /**
     * 转换多个
     * @param ids
     * @return
     */
    private List<CommodityDoc> commodity2commodityDocs(List<Long> ids) {
        List<CommodityDoc> commodityDocs = new ArrayList<>();
        for (Long id : ids) {
            Commodity commodity = commodityMapper.selectById(id);
            CommodityDoc commodityDoc = commodity2commodityDoc(commodity);
            commodityDocs.add(commodityDoc);
        }
        return commodityDocs;
    }

    /**
     * 转换一个
     * @param commodity
     * @return
     */
    private CommodityDoc commodity2commodityDoc(Commodity commodity) {

        //选中 alt+enter
        CommodityDoc commodityDoc = new CommodityDoc();
        commodityDoc.setId(commodity.getId());
        commodityDoc.setName(commodity.getName());
        commodityDoc.setCommodityTypeId(commodity.getCommodityTypeId());
        commodityDoc.setBrandId(commodity.getBrandId());
        //从某个商品sku中获取最大或最小
        List<Sku> skus = skuMapper.selectList(new EntityWrapper<Sku>()
                .eq("commodityId", commodity.getId()));

        Integer minPrice  = skus.get(0).getPrice();
        Integer maxPrice  = skus.get(0).getPrice();
        for (Sku sku : skus) {
            if (sku.getPrice()<minPrice) minPrice=sku.getPrice();
            if (sku.getPrice()>maxPrice) maxPrice = sku.getPrice();
        }
        commodityDoc.setMinPrice(minPrice);
        commodityDoc.setMaxPrice(maxPrice);
        commodityDoc.setSaleCount(commodity.getSaleCount());
        commodityDoc.setOnSaleTime(commodity.getOnSaleTime().intValue());
        commodityDoc.setCommentCount(commodity.getCommentCount());
        commodityDoc.setViewCount(commodity.getViewCount());
        String medias = commodity.getMedias();
        if (StringUtils.isNotBlank(medias)) {
            commodityDoc.setImages(Arrays
                    .asList(medias.split(",")));
        }
        Brand brand = brandMapper.selectById(commodity.getBrandId());
        CommodityType commodityType = commodityTypeMapper.selectById(commodity.getCommodityTypeId());
        //投机-有空格就会分词
        String all = commodity.getName()+" "
                +commodity.getSubName()+" "+brand.getName()+" "+commodityType.getName();

        commodityDoc.setAll(all);
        commodityDoc.setViewProperties(commodity.getViewProperties());
        commodityDoc.setSkuProperties(commodity.getSkuTemplate());
        //设置值
        return commodityDoc;
    }
}
