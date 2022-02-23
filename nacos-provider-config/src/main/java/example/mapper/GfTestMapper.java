package example.mapper;

import example.entity.GfTest;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GfTestMapper extends MyBaseMapper<GfTest> {

    @Insert("<script>" +
            "INSERT INTO gf_test(name,age,career,time) VALUES"
            + "<foreach collection='entities' index='index' item='item' open='' separator=',' close=''>"
            + " (#{item.name},#{item.age},#{item.career},#{item.time})"
            + "</foreach>"
            + " ON DUPLICATE KEY UPDATE"
            + " name = VALUES(name),age = VALUES(age),career = VALUES(career),time = VALUES(time)"
            + " </script>")
    int batchInsert(@Param("entities") List<GfTest> entities);
}
