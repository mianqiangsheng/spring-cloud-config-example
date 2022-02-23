package example.service.impl;

import example.mapper.MyBaseMapper;
import example.service.ImportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Java多线程批量拆分List导入数据库
 * @author ：li zhen
 * @description:
 * @date ：2022/2/23 9:42
 */
@Service
public class ImportServiceImpl<T> implements ImportService<T> {

    private static final Integer nThreads = 5;

    /**
     * 每个线程最多插入记录数：size/nThreads 与 size/nThreads+nThreads-1之间的最大值，
     * 数据库一般一次sql操作对数据大小有限制，一般不能超过4M，根据实际情况调整nThreads保证sql数据大小不超过数据库上限
     * 又由于线程数一般不可能无限增加，所以控制一次输入的list的大小也需要考虑。即可以通过分批次调用importData来避免可能的内存溢出问题，即以时间换空间
     * @param list
     * @param mapper
     * @return
     */
    @Transactional(rollbackFor = {Exception.class,Error.class})
    public Integer importData(List<T> list, MyBaseMapper<T> mapper) {
        if (list == null || list.isEmpty()) {
            return 0;
        }

        int size = list.size();
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        List<Future<Integer>> futures = new ArrayList<>(nThreads);

        int i1 = size / nThreads == 0 ? 1 : size / nThreads;

        for (int i = 0; i < nThreads; i++) {

            int fromIndex = (size / nThreads == 0 ? 1 : size / nThreads) * i;
            int toIndex;
            if (i == nThreads - 1){
                toIndex = size;
            }else {
                toIndex = (size / nThreads == 0 ? 1 : size / nThreads) * (i + 1);
            }

            final List<T> subListData = list.subList(fromIndex, toIndex);
            Callable<Integer> task = () -> {
                mapper.batchInsert(subListData);
                return 1;
            };
            futures.add(executorService.submit(task));
        }

        executorService.shutdown();

        if (!futures.isEmpty() && futures != null) {
            return 1;
        }
        return -1;
    }
}
