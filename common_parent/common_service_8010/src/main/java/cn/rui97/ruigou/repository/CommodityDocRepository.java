package cn.rui97.ruigou.repository;

import cn.rui97.ruigou.index.CommodityDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @Auther: rui
 * @Date: 2019/1/22 13:33
 * @Description:
 */
@Repository
public interface CommodityDocRepository extends ElasticsearchCrudRepository<CommodityDoc,Long> {

}
