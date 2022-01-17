package example.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import example.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("master")
public class DataServiceImpl implements DataService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List selectFromLizhen() {
        return  jdbcTemplate.queryForList("select * from gf_comrade");
    }

    @Override
    @DS("mysql")
    public List selectFromTest() {
        return  jdbcTemplate.queryForList("select * from gf_test");
    }
}
