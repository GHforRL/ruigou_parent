package cn.rui97.ruigou.service.impl;

import cn.rui97.ruigou.domain.Specification;
import cn.rui97.ruigou.mapper.SpecificationMapper;
import cn.rui97.ruigou.service.ISpecificationService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品属性 服务实现类
 * </p>
 *
 * @author lurui
 * @since 2019-01-20
 */
@Service
public class SpecificationServiceImpl extends ServiceImpl<SpecificationMapper, Specification> implements ISpecificationService {

    @Autowired
    private SpecificationMapper specificationMapper;

    @Override
    public List<Specification> getSpecificationsByTypeId(Long commodityTypeId) {
        return specificationMapper.getSpecifications2TypeId(commodityTypeId);
    }
}
