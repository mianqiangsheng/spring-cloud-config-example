package example.service;

import example.mapper.MyBaseMapper;

import java.util.List;

/**
 * Java多线程批量拆分List导入数据库
 * @author ：li zhen
 * @description:
 * @date ：2022/2/23 9:41
 */
public interface ImportService<T> {

    Integer importData(List<T> list, MyBaseMapper<T> mapper);
}
