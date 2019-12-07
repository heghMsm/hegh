package com.leyou.item.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "tb_spu")
public class Spu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long brandId;

    /**
     * 一级类目
     */
    private Long cid1;

    /**
     * 二级类目
     */
    private Long cid2;

    /**
     * 三级类目
     */
    private Long cid3;

    /**
     * 标题
     */
    private String title;

    /**
     * 子标题
     */
    private String subTitle;

    /**
     * 是否上架
     */
    private Boolean saleable;

    /**
     * 是否有效，用于逻辑删除
     */
    private Boolean valid;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后修改时间
     */
    private Date lastUpdateTime;

}





