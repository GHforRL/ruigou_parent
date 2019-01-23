package cn.rui97.ruigou.mapper;

import cn.rui97.ruigou.domain.Commodity;
import cn.rui97.ruigou.query.CommodityQuery;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品 Mapper 接口
 * </p>
 *
 * @author lurui
 * @since 2019-01-18
 */
public interface CommodityMapper extends BaseMapper<Commodity> {
    /**
     * 加载分页数据
     * @param page
     * @param query
     * @return
     */
    List<Commodity> loadPageData(Page<Commodity> page, CommodityQuery query);

    /**
     * 上架
     * @param params
     */
    void onSale(Map<String, Object> params);

    /**
     * 下架
     * @param params
     */
    void offSale(Map<String, Object> params);
}
