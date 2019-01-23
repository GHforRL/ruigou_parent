package cn.rui97.ruigou.domain;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 商品扩展
 * </p>
 *
 * @author lurui
 * @since 2019-01-18
 */
@TableName("t_commodity_ext")
public class CommodityExt extends Model<CommodityExt> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 简介
     */
    private String description;
    /**
     * 图文内容
     */
    private String richContent;
    /**
     * 商品ID
     */
    private Long commodityId;
    private String viewProperties;
    private String skuProperties;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRichContent() {
        return richContent;
    }

    public void setRichContent(String richContent) {
        this.richContent = richContent;
    }

    public Long getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Long commodityId) {
        this.commodityId = commodityId;
    }

    public String getViewProperties() {
        return viewProperties;
    }

    public void setViewProperties(String viewProperties) {
        this.viewProperties = viewProperties;
    }

    public String getSkuProperties() {
        return skuProperties;
    }

    public void setSkuProperties(String skuProperties) {
        this.skuProperties = skuProperties;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "CommodityExt{" +
        ", id=" + id +
        ", description=" + description +
        ", richContent=" + richContent +
        ", commodityId=" + commodityId +
        ", viewProperties=" + viewProperties +
        ", skuProperties=" + skuProperties +
        "}";
    }
}
