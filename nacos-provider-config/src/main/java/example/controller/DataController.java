package example.controller;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import example.entity.GfComrade;
import example.entity.GfTest;
import example.mapper.GfComradeMapper;
import example.mapper.GfTestMapper;
import example.service.DataService;
import example.service.GfComradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Wrapper;
import java.util.List;

@RestController
public class DataController {

    @Autowired
    private DataService dataService;

    @Autowired
    private GfComradeService gfComradeService;

    @Autowired
    private GfComradeMapper gfComradeMapper;

    @Autowired
    private GfTestMapper gfTestMapper;

    @GetMapping(value = "/select/lizhen")
    public List lizhen() {
        List list = dataService.selectFromLizhen();
        return list;
    }

    @GetMapping(value = "/select/test")
    public List test() {
        List list = dataService.selectFromTest();
        return list;
    }

    @GetMapping(value = "/select/gfComrade")
    public List gfComrade() {
        List list = gfComradeService.list();
        return list;
    }

    @GetMapping(value = "/page/gfComrade")
    public IPage<GfComrade> gfComradePage() {
        IPage<GfComrade> gfComradeIPage = gfComradeService.gfComradePage();
        return gfComradeIPage;
    }

    @GetMapping(value = "/origin/page/gfComrade")
    public IPage<GfComrade> oriGfComradePage() {
        IPage<GfComrade> gfComradeIPage = gfComradeService.page(new Page<>(1,10), Wrappers.emptyWrapper());
        return gfComradeIPage;
    }

    @GetMapping(value = "/mapper/page/gfComrade")
    public IPage<GfComrade> mapGfComradePage() {
        IPage<GfComrade> gfComradeIPage = gfComradeMapper.selectPage(new Page<>(1,10), Wrappers.emptyWrapper());
        return gfComradeIPage;
    }

    @GetMapping(value = "/mapper/method/gfComrade")
    public List<GfComrade> metGfComradePage() {
        List<GfComrade> all = gfComradeMapper.all();
        return all;
    }

    @GetMapping(value = "/mapper/method/gfTest")
    public List<GfTest> metGfTestPage1() {
        List<GfTest> all = gfComradeMapper.allGfTest();
        return all;
    }

    /**
     * TODO 当默认数据源和mybatis的mapper使用的数据源不一致，没有@DS注明数据源时，发现mapper不能找到对应datasource
     * 可以手动指定不同mapper下的SqlSessionFactory使用的dataSource,但是不能使用@DS功能了，需要完全兼容需要优先使用@DS如果没有找到对应数据源则自动切换使用mapper对应的数据源
     * @return
     */
    @GetMapping(value = "/origin/page/gfTest")
    public IPage<GfTest> metGfTestPage2() {
        Page<GfTest> gfTestPage = gfTestMapper.selectPage(new Page<>(1, 10), Wrappers.emptyWrapper());
        return gfTestPage;
    }
}
