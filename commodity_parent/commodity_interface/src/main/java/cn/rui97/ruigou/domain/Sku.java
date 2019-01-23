package cn.rui97.ruigou.domain;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * SKU
 * </p>
 *
 * @author lurui
 * @since 2019-01-21
 */
@TableName("t_sku")
public class Sku extends Model<Sku> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 商品ID
     */
    private Long commodityId;
    /**
     * 优惠价
     */
    private Integer price;
    /**
     * 锁定库存
     */
    private Integer stock;
    /**
     * SKU属性摘要
     */
    private String skuValues;
    /**
     * 状态
     */
    private Boolean state;
    /**
     * sku索引
     */
    private String indexs;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Long commodityId) {
        this.commodityId = commodityId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getSkuValues() {
        return skuValues;
    }

    public void setSkuValues(String skuValues) {
        this.skuValues = skuValues;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getIndexs() {
        return indexs;
    }

    public void setIndexs(String indexs) {
        this.indexs = indexs;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Sku{" +
        ", id=" + id +
        ", commodityId=" + commodityId +
        ", price=" + price +
        ", stock=" + stock +
        ", skuValues=" + skuValues +
        ", state=" + state +
        ", indexs=" + indexs +
        "}";
    }
}
