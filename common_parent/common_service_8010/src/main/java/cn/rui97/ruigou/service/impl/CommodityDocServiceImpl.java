package cn.rui97.ruigou.service.impl;

import cn.rui97.ruigou.index.CommodityDoc;
import cn.rui97.ruigou.repository.CommodityDocRepository;
import cn.rui97.ruigou.service.ICommodityDocService;
import cn.rui97.ruigou.util.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Auther: rui
 * @Date: 2019/1/22 13:26
 * @Description:
 */
@Service
public class CommodityDocServiceImpl implements ICommodityDocService {

    @Autowired
    private CommodityDocRepository repository;

    @Override
    public void add(CommodityDoc commodityDoc) {
        repository.save(commodityDoc);
    }

    @Override
    public void del(Long id) {
        repository.deleteById(id);
    }

    @Override
    public CommodityDoc get(Long id) {
        return repository.findById(id).get();
    }

    @Override
    public void batchAdd(List<CommodityDoc> commodityDocs) {
        repository.saveAll(commodityDocs);
    }

    @Override
    public void batchDel(List<Long> ids) {
        for (Long id : ids) {
            repository.deleteById(id);
        }
    }

    @Override
    public PageList<CommodityDoc> search(Map<String, Object> params) {
        return null;
    }
}
