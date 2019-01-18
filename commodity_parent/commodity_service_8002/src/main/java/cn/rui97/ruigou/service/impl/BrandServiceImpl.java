package cn.rui97.ruigou.service.impl;

import cn.rui97.ruigou.domain.Brand;
import cn.rui97.ruigou.mapper.BrandMapper;
import cn.rui97.ruigou.query.BrandQuery;
import cn.rui97.ruigou.service.IBrandService;
import cn.rui97.ruigou.util.PageList;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 品牌信息 服务实现类
 * </p>
 *
 * @author lurui
 * @since 2019-01-13
 */
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements IBrandService {

    @Autowired
    private BrandMapper brandMapper;

    @Override
    public PageList<Brand> selectPageList(BrandQuery query) {
        Page<Brand> page = new Page<>(query.getPage(),query.getRows());
        //当我们传入Page,表示要做分页查询,会把查询总数设置在Page的total
        List<Brand> rows = brandMapper.selectPageList(page,query);
        //PageList total,rows
        long total = page.getTotal();

        return new PageList<>(total,rows);
    }
}
