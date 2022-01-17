package example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import example.entity.GfTest;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GfTestMapper extends BaseMapper<GfTest> {
}
