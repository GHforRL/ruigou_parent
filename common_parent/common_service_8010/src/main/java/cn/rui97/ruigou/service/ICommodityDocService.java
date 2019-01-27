package cn.rui97.ruigou.service;

import cn.rui97.ruigou.index.CommodityDoc;
import cn.rui97.ruigou.util.PageList;

import java.util.List;
import java.util.Map;

/**
 * @Auther: rui
 * @Date: 2019/1/22 13:25
 * @Description:
 */
public interface ICommodityDocService {
    /**
     * 添加文档
     * @param commodityDoc
     */
    void add(CommodityDoc commodityDoc);


    /**
     * 删除文档
     * @param id
     */
    void del(Long id);


    /**
     * 获取文档
     * @param id
     * @return
     */
    CommodityDoc get(Long id);

    /**
     * 批量添加文档
     * @param commodityDocs
     */
    void batchAdd(List<CommodityDoc> commodityDocs);

    /**
     * 批量删除
     * @param ids
     */
    void batchDel(List<Long> ids);

    /**
     * 查询
     * @param params
     * @return
     */
    PageList<Map<String,Object>> search(Map<String, Object> params);
}
