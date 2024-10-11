package com.xuecheng.media.service.jobhandler;

import com.xuecheng.media.model.po.MediaProcess;
import com.xuecheng.media.service.MediaFileProcessService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.*;

@Slf4j
@Component
public class VideoTask {
    @Autowired
    private MediaFileProcessService mediaFileProcessService;

    @XxlJob("videoJobHandler")
    public void shardingJobHandler() throws Exception {

        // 分片参数
        int shardIndex = XxlJobHelper.getShardIndex();
        int shardTotal = XxlJobHelper.getShardTotal();
        //先获取任务，再放到线程池
        List<MediaProcess> mediaProcessList = mediaFileProcessService.getMediaProcessList(shardIndex, shardTotal, 5);
        //先获取CPU核数，方便创建线程池大小
        int i = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(i);
        CountDownLatch countDownLatch = new CountDownLatch(i);
        mediaProcessList.forEach(mediaProcess -> {
                    executorService.execute(() -> {
                        try {
                            Long id = mediaProcess.getId();
                            String fileId = mediaProcess.getFileId();
                            boolean b = mediaFileProcessService.startTask(id);
                            //开启任务失败
                            if (!b) {
                                log.error("开启任务失败,{}", id);
                                mediaFileProcessService.saveProcessFinishStatus(id, "3", fileId, null, "开启任务失败");
                                countDownLatch.countDown();
                                return;
                            }
                            //开启任务成功
                            //下载任务到本地
                            //转码成功再上传到minio
                            System.out.println("执行任务成功,转码成功");
                        } finally {
                            countDownLatch.countDown();
                        }
                    });

                }
        );
        countDownLatch.await(30, TimeUnit.MINUTES);
    }
}
