package example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author ：li zhen
 * @description:
 * @date ：2022/2/23 9:56
 */
public interface MyBaseMapper<T> extends BaseMapper<T> {

    /**
     * 批量插入记录
     *
     * @param entities 实体对象列表
     */
    int batchInsert(List<T> entities);
}
