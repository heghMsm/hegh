package com.leyou.item.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "tb_stu_detail")
public class SpuDetail {

    /**
     * 对应的spu的Id
     */
    @Id
    private Long spuId;

    /**
     * 商品的描述信息
     */
    private String description;

    /**
     * 商品特殊规格的名称及可选用的模板
     */
    private String specialSpec;

    /**
     * 商品的全局规格属性
     */
    private String genericSpec;

    /**
     * 包装清单
     */
    private String packingList;

    /**
     * 售后服务
     */
    private String afterService;

}
