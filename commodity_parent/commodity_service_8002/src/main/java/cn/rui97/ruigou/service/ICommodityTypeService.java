package cn.rui97.ruigou.service;

import cn.rui97.ruigou.domain.Brand;
import cn.rui97.ruigou.domain.CommodityType;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 商品目录 服务类
 * </p>
 *
 * @author lurui
 * @since 2019-01-13
 */
public interface ICommodityTypeService extends IService<CommodityType> {
    /**
     * 获取无限极数据
     * @return
     */
    List<CommodityType> treeData();

    /**
     * 获取面包屑
     * @param commodityTypeId
     * @return
     */
    List<Map<String,Object>> getCrumbs(Long commodityTypeId);

    List<Brand> getBrands(Long commodityTypeId);

    Set<String> getLetters(Long commodityTypeId);
}
