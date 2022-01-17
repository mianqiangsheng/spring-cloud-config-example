package example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import example.entity.GfComrade;

public interface GfComradeService extends IService<GfComrade> {

    IPage<GfComrade> gfComradePage();
}
