package cn.rui97.ruigou.service;

import cn.rui97.ruigou.domain.Specification;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 商品属性 服务类
 * </p>
 *
 * @author lurui
 * @since 2019-01-20
 */
public interface ISpecificationService extends IService<Specification> {

    /**
     * 获取对应类型的属性
     * @param commodityTypeId
     */
    List<Specification> getSpecificationsByTypeId(Long commodityTypeId);
}
