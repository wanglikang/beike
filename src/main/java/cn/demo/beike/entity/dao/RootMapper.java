package cn.demo.beike.entity.dao;

import cn.demo.beike.entity.Root;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RootMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Root record);

    int insertSelective(Root record);

    Root selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Root record);

    int updateByPrimaryKey(Root record);

    @Results(id = "rootResultMap", value = {
            @Result(property = "id",column = "id",id = true),
            @Result(property = "loginname",column = "loginName"),
            @Result(property = "pass",column = "pass")
    })
    @Select({"select * from root where loginName=#{loginName} and pass=#{pass}"})
    List<Root> findRoot(String loginName, String pass);
}