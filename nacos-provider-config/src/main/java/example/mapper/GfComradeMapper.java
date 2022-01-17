package example.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import example.entity.GfComrade;
import example.entity.GfTest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
//@DS("master")
public interface GfComradeMapper extends BaseMapper<GfComrade> {

    @Select("<script> select * from gf_comrade </script>")
    @DS("master")
    List<GfComrade> all();

    @Select("<script> select * from gf_test </script>")
    @DS("mysql")
    List<GfTest> allGfTest();
}
