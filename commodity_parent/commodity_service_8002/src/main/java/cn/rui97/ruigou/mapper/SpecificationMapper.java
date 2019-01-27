package cn.rui97.ruigou.mapper;

import cn.rui97.ruigou.domain.Specification;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 商品属性 Mapper 接口
 * </p>
 *
 * @author lurui
 * @since 2019-01-20
 */
public interface SpecificationMapper extends BaseMapper<Specification> {

    List<Specification> getSpecifications2TypeId(Long commodityTypeId);
}
