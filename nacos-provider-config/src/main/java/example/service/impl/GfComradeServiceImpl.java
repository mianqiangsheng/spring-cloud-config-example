package example.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import example.entity.GfComrade;
import example.mapper.GfComradeMapper;
import example.service.GfComradeService;
import org.springframework.stereotype.Service;

@Service
public class GfComradeServiceImpl extends ServiceImpl<GfComradeMapper, GfComrade> implements GfComradeService {

    @Override
    @DS("master")
    public IPage<GfComrade> gfComradePage() {
        return baseMapper.selectPage(new Page<>(1,10), Wrappers.emptyWrapper());
    }
}
