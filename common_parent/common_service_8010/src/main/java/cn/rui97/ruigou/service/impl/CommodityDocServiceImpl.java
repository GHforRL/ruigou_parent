package cn.rui97.ruigou.service.impl;

import cn.rui97.ruigou.index.CommodityDoc;
import cn.rui97.ruigou.repository.CommodityDocRepository;
import cn.rui97.ruigou.service.ICommodityDocService;
import cn.rui97.ruigou.util.PageList;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
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
    public PageList<Map<String,Object>> search(Map<String, Object> params) {

        String keyword = (String) params.get("keyword"); //查询
        String sortField = (String) params.get("sortField"); //排序
        String sortType = (String) params.get("sortType");//排序

        Long commodityType = params.get("commodityType") !=null?Long.valueOf(params.get("commodityType").toString()):null;//过滤
        Long brandId = params.get("brandId") !=null?Long.valueOf(params.get("brandId").toString()):null;//过滤
        Long priceMin = params.get("priceMin") !=null?Long.valueOf(params.get("priceMin").toString())*100:null;//过滤
        Long priceMax = params.get("priceMax") !=null?Long.valueOf(params.get("priceMax").toString())*100:null;//过滤
        Long page = params.get("page") !=null?Long.valueOf(params.get("page").toString()):null; //分页
        Long rows = params.get("rows") !=null?Long.valueOf(params.get("rows").toString()):null;//分页

        //构建器
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        //设置查询条件=查询+过滤
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(keyword)){
            boolQuery.must(QueryBuilders.matchQuery("all", keyword));
        }
        List<QueryBuilder> filter = boolQuery.filter();
        if (commodityType != null){ //类型
            filter.add(QueryBuilders.termQuery("commodityTypeId", commodityType));
        }
        if (brandId != null){ //品牌
            filter.add(QueryBuilders.termQuery("brandId", brandId));
        }
        //最大价格 最小价格
        //minPrice <= priceMax && maxPrice>=priceMin
        if(priceMax!=null){
            filter.add(QueryBuilders.rangeQuery("minPrice").lte(priceMax));
        }
        if(priceMin!=null){
            filter.add(QueryBuilders.rangeQuery("maxPrice").gte(priceMax));
        }

        builder.withQuery(boolQuery);
        //排序
        SortOrder defaultSortOrder = SortOrder.DESC;
        if (StringUtils.isNotBlank(sortField)){//销量 新品 价格 人气 评论
            //如果传入的不是降序改为升序
            if (StringUtils.isNotBlank(sortType) && sortType.equals(SortOrder.DESC)){
                defaultSortOrder = SortOrder.ASC;
            }
            //销量
            if (sortField.equals("xl")){
                builder.withSort(SortBuilders.fieldSort("saleCount").order(defaultSortOrder));
            }
            // 新品
            if (sortField.equals("xp")){
                builder.withSort(SortBuilders.fieldSort("onSaleTime").order(defaultSortOrder));
            }
            // 人气
            if (sortField.equals("rq")){
                builder.withSort(SortBuilders.fieldSort("viewCount").order(defaultSortOrder));
            }
            // 评论
            if (sortField.equals("pl")){
                builder.withSort(SortBuilders.fieldSort("commentCount").order(defaultSortOrder));
            }
            // 价格  索引库有两个字段 最大,最小
            //如果用户按照升序就像买便宜的,就用最小价格,如果用户按照降序想买贵的,用最大价格
            if (sortField.equals("jg")){
                if (SortOrder.ASC.equals(defaultSortOrder)){
                    builder.withSort(SortBuilders.fieldSort("minPrice").order(defaultSortOrder));
                }else{
                    builder.withSort(SortBuilders.fieldSort("maxPrice").order(defaultSortOrder));
                }
            }
        }
        //分页
        Long pageTmp = page-1; //从0开始
        builder.withPageable(PageRequest.of(pageTmp.intValue(), rows.intValue()));
        //截取字段 @TODO
        //封装数据
        Page<CommodityDoc> commodityDocs = repository.search(builder.build());
        List<Map<String,Object>> datas = commodityDocs2ListMap(commodityDocs.getContent());
        return new PageList<>(commodityDocs.getTotalElements(),datas);
    }

    /**
     * 数据转换
     * @param content
     * @return
     */
    private List<Map<String,Object>> commodityDocs2ListMap(List<CommodityDoc> content) {
        List<Map<String,Object>> result = new ArrayList<>();
        for (CommodityDoc commodityDoc : content)
        {
            result.add(commodityDoc2Map(commodityDoc));
        }
        return result;
    }


    private Map<String,Object> commodityDoc2Map(CommodityDoc commodityDoc) {
        Map<String,Object> result = new HashMap<>();
        result.put("id", commodityDoc.getId());
        result.put("name", commodityDoc.getName());
        result.put("commodityTypeId", commodityDoc.getCommodityTypeId());
        result.put("brandId", commodityDoc.getBrandId());
        result.put("minPrice", commodityDoc.getMinPrice());
        result.put("maxPrice", commodityDoc.getMaxPrice());

        result.put("saleCount", commodityDoc.getSaleCount());
        result.put("onSaleTime", commodityDoc.getOnSaleTime());
        result.put("commentCount", commodityDoc.getCommentCount());
        result.put("viewCount", commodityDoc.getViewCount());
        result.put("images", commodityDoc.getImages());
        return result;
    }
}
