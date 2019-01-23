package cn.rui97.ruigou.client;

import cn.rui97.ruigou.index.CommodityDoc;
import cn.rui97.ruigou.util.AjaxResult;
import cn.rui97.ruigou.util.PageList;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Auther: rui
 * @Date: 2019/1/22 13:05
 * @Description:
 */
@Component
public class CommodityDocClientFallbackFactory implements FallbackFactory<CommodityDocClient> {
    @Override
    public CommodityDocClient create(Throwable throwable) {
        return new CommodityDocClient() {
            @Override
            public AjaxResult save(CommodityDoc commodityDoc) {
                return null;
            }

            @Override
            public AjaxResult del(Long id) {
                return null;
            }

            @Override
            public CommodityDoc get(Long id) {
                return null;
            }

            @Override
            public AjaxResult batchSave(List<CommodityDoc> commodityDocs) {
                return null;
            }

            @Override
            public AjaxResult batchDel(List<Long> ids) {
                return null;
            }

            @Override
            public PageList<CommodityDoc> search(Map<String, Object> params) {
                return null;
            }
        };
    }
}
