package cn.rui97.ruigou.mapper;

import cn.rui97.ruigou.domain.Brand;
import cn.rui97.ruigou.query.BrandQuery;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;

import java.util.List;

/**
 * <p>
 * 品牌信息 Mapper 接口
 * </p>
 *
 * @author lurui
 * @since 2019-01-13
 */
public interface BrandMapper extends BaseMapper<Brand> {
    /**
     * 查询分页数据
     * @param page
     * @param query
     * @return
     */
    List<Brand> selectPageList(Page<Brand> page, BrandQuery query);
}
