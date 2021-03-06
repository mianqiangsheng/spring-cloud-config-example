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
import example.service.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private ImportService<GfTest> importService;

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
     * TODO ?????????????????????mybatis???mapper????????????????????????????????????@DS???????????????????????????mapper??????????????????datasource
     * ????????????????????????mapper??????SqlSessionFactory?????????dataSource,??????????????????@DS????????????????????????????????????????????????@DS??????????????????????????????????????????????????????mapper??????????????????
     * @return
     */
    @GetMapping(value = "/origin/page/gfTest")
    public IPage<GfTest> metGfTestPage2() {
        Page<GfTest> gfTestPage = gfTestMapper.selectPage(new Page<>(1, 10), Wrappers.emptyWrapper());
        return gfTestPage;
    }

    @RequestMapping(value = "/import/data")
    public Integer importData(@RequestBody List<GfTest> list) {
        Integer integer = importService.importData(list, gfTestMapper);
        return integer;
    }
}
