package cn.rui97.ruigou.service;

import cn.rui97.ruigou.domain.Commodity;
import cn.rui97.ruigou.domain.Sku;
import cn.rui97.ruigou.domain.Specification;
import cn.rui97.ruigou.query.CommodityQuery;
import cn.rui97.ruigou.util.PageList;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品 服务类
 * </p>
 *
 * @author lurui
 * @since 2019-01-18
 */
public interface ICommodityService extends IService<Commodity> {
    /**
     * 跨表高级分页
     * @param query
     * @return
     */
    PageList<Commodity> selectPageList(CommodityQuery query);

    /**
     * 添加显示属性
     * @param commodityId
     * @param specifications
     */
    void addViewProperties(Long commodityId, List<Specification> specifications);

    /**
     * 添加sku
     * @param commodityId
     * @param skuProperties
     * @param skuDatas
     */
    void addSkus(Long commodityId, List<Map<String,Object>> skuProperties, List<Map<String,Object>> skuDatas);

    /**
     * 通过商品id查询sku
     * @param commodityId
     * @return
     */
    List<Sku> querySkus(Long commodityId);

    /**
     * 上下架处理
     * @param ids
     * @param onSale
     */
    void onSale(String ids, Integer onSale);
}
