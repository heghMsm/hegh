package com.leyou.item.mapper;

import com.leyou.item.pojo.SpecGroup;
import org.apache.ibatis.annotations.Insert;
import tk.mybatis.mapper.common.Mapper;

public interface SpecGroupMapper extends Mapper<SpecGroup> {

    @Insert("insert into tb_spec_group (cid,name) value (#{cid},#{name})")
    void insertGroup(SpecGroup record);
}
