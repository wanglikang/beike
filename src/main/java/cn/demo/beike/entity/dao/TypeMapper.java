package cn.demo.beike.entity.dao;

import cn.demo.beike.entity.Type;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeMapper {
    int deleteByPrimaryKey(Integer id);

    /**
     * 返回自增的主键
     * @param record
     * @return
     */
    @Insert({"INSERT INTO `type`(typeName, flag) ",
            "VALUES (#{typename},#{flag})"})
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertUseGeneratedKeys(Type record);

    /**
     * 返回非自增主键
     * @param type
     * @return
     */
    @Insert({"INSERT INTO `type`(typeName, flag) ",
            "VALUES (#{typename},#{flag})"})
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyColumn = "id", keyProperty = "id", resultType = Integer.class, before = false)
    void insertUseSelectKey(Type type);

    int insertSelective(Type record);

    @ResultMap("typeResultMap")
    @Select("SELECT * FROM `type` where `id`=#{id}")
    Type selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Type record);

    int updateByPrimaryKey(Type record);

    @Results(id = "typeResultMap",value = {
            @Result(property = "id",column = "id",id = true),
            @Result(property = "typename",column = "typeName"),
            @Result(property = "flag",column = "flag")
    })
    @Select("select * from `type`")
    List<Type> selectAllType();
}