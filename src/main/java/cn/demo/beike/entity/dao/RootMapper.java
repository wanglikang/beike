package cn.demo.beike.entity.dao;

import cn.demo.beike.entity.Root;

public interface RootMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Root record);

    int insertSelective(Root record);

    Root selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Root record);

    int updateByPrimaryKey(Root record);
}