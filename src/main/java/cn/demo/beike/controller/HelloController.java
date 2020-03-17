package cn.demo.beike.controller;

import cn.demo.beike.entity.Type;
import cn.demo.beike.entity.dao.TypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @Autowired
    TypeMapper typeMapper;

    @GetMapping("/get")
    @ResponseBody
    public String testMybatis(){
        List<Type> types = typeMapper.selectAllType();
        for(Type type:types) {
            System.out.println(type);
        }
        return types.toString();
    }


    @GetMapping("/insert")
    @ResponseBody
    public String testInsertAutoIncrease(){
        Type newtype = new Type();
        newtype.setFlag("3");
        newtype.setTypeName("预约33");
        typeMapper.insertUseSelectKey(newtype);
        System.out.println("new ID:"+newtype.getId());
        return newtype.getId()+"";
    }
}
