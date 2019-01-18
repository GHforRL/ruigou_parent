package cn.rui97.ruigou.service;

import cn.rui97.ruigou.domain.Brand;
import cn.rui97.ruigou.query.BrandQuery;
import cn.rui97.ruigou.util.PageList;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 品牌信息 服务类
 * </p>
 *
 * @author lurui
 * @since 2019-01-13
 */
public interface IBrandService extends IService<Brand> {
    /**
     * 分页查询,关联对象
     * @param query
     * @return
     */
    PageList<Brand> selectPageList(BrandQuery query);
}
