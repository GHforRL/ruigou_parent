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
 * 商品目录
 * </p>
 *
 * @author lurui
 * @since 2019-01-13
 */
@TableName("t_commodity_type")
public class CommodityType extends Model<CommodityType> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long createTime;
    private Long updateTime;
    /**
     * 类型名
     */
    private String name;
    /**
     * 父ID
     */
    private Long pid;

    @TableField(exist = false)
    private CommodityType parent;
    @TableField(exist = false)
    private List<CommodityType> children = new ArrayList<>();

    /**
     * 图标
     */
    private String logo;
    /**
     * 描述
     */
    private String description;
    private Integer sortIndex;
    /**
     * 路径
     */
    private String path;
    /**
     * 商品数量
     */
    private Integer totalCount;
    /**
     * 分类标题（SEO）
     */
    private String seoTitle;
    /**
     * 分类关键字（SEO）
     */
    private String seoKeywords;
    @TableField("type_template_id")
    private Long typeTemplateId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public String getSeoTitle() {
        return seoTitle;
    }

    public void setSeoTitle(String seoTitle) {
        this.seoTitle = seoTitle;
    }

    public String getSeoKeywords() {
        return seoKeywords;
    }

    public void setSeoKeywords(String seoKeywords) {
        this.seoKeywords = seoKeywords;
    }

    public Long getTypeTemplateId() {
        return typeTemplateId;
    }

    public void setTypeTemplateId(Long typeTemplateId) {
        this.typeTemplateId = typeTemplateId;
    }

    public CommodityType getParent() {
        return parent;
    }

    public void setParent(CommodityType parent) {
        this.parent = parent;
    }

    public List<CommodityType> getChildren() {
        return children;
    }

    public void setChildren(List<CommodityType> children) {
        this.children = children;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "CommodityType{" +
        ", id=" + id +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", name=" + name +
        ", pid=" + pid +
        ", logo=" + logo +
        ", description=" + description +
        ", sortIndex=" + sortIndex +
        ", path=" + path +
        ", totalCount=" + totalCount +
        ", seoTitle=" + seoTitle +
        ", seoKeywords=" + seoKeywords +
        ", typeTemplateId=" + typeTemplateId +
        "}";
    }
}
