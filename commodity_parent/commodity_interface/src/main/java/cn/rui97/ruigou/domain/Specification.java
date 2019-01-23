package cn.rui97.ruigou.domain;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 商品属性
 * </p>
 *
 * @author lurui
 * @since 2019-01-20
 */
@TableName("t_specification")
public class Specification extends Model<Specification> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 规格名称
     */
    private String name;
    @TableField("commodity_type_id")
    private Long commodityTypeId;
    /**
     * 是否是sku属性
     */
    @TableField("isSku")
    private Integer isSku;
    /**
     * 显示属性的值-只有一个
     */
    @TableField(exist = false)
    private String selectValue;
    /**
     * sku选项-多个
     */
    @TableField(exist = false)
    List<String> skuValues = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCommodityTypeId() {
        return commodityTypeId;
    }

    public void setCommodityTypeId(Long commodityTypeId) {
        this.commodityTypeId = commodityTypeId;
    }

    public Integer getIsSku() {
        return isSku;
    }

    public void setIsSku(Integer isSku) {
        this.isSku = isSku;
    }

    public String getSelectValue() {
        return selectValue;
    }

    public void setSelectValue(String selectValue) {
        this.selectValue = selectValue;
    }

    public List<String> getSkuValues() {
        return skuValues;
    }

    public void setSkuValues(List<String> skuValues) {
        this.skuValues = skuValues;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Specification{" +
        ", id=" + id +
        ", name=" + name +
        ", commodityTypeId=" + commodityTypeId +
        ", isSku=" + isSku +
        "}";
    }
}
