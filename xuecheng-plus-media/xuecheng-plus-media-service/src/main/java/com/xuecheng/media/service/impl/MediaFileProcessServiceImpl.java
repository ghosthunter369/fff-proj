package com.xuecheng.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.media.mapper.MediaFilesMapper;
import com.xuecheng.media.mapper.MediaProcessHistoryMapper;
import com.xuecheng.media.mapper.MediaProcessMapper;
import com.xuecheng.media.model.po.MediaFiles;
import com.xuecheng.media.model.po.MediaProcess;
import com.xuecheng.media.model.po.MediaProcessHistory;
import com.xuecheng.media.service.MediaFileProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Mr.M
 * @version 1.0
 * @description TODO
 * @date 2022/9/14 14:41
 */
@Slf4j
@Service
public class MediaFileProcessServiceImpl implements MediaFileProcessService {

    @Autowired
    MediaFilesMapper mediaFilesMapper;
    @Autowired
    MediaProcessHistoryMapper  mediaProcessHistoryMapper;

    @Autowired
    MediaProcessMapper mediaProcessMapper;


    @Override
    public List<MediaProcess> getMediaProcessList(int shardIndex, int shardTotal, int count) {
        List<MediaProcess> mediaProcesses = mediaProcessMapper.selectListByShardIndex(shardTotal, shardIndex, count);
        return mediaProcesses;
    }
    //实现如下
    public boolean startTask(long id) {
        int result = mediaProcessMapper.startTask(id);
        return result<=0?false:true;
    }

    @Override
    public void saveProcessFinishStatus(Long taskId, String status, String fileId, String url, String errorMsg) {
        //首先查询有无这条记录
        MediaProcess mediaProcess = mediaProcessMapper.selectById(taskId);
        if(mediaProcess == null){
            return;
        }
        //失败的话更新MediaProcess状态，失败次数+1，状态status
        if(Integer.parseInt(status) != 2){
            //任务失败
            mediaProcess.setFailCount(mediaProcess.getFailCount()+1);
            mediaProcess.setStatus("3");
            mediaProcess.setErrormsg(errorMsg);
            mediaProcessMapper.updateById(mediaProcess);
            return;
        }
        //成功的话更新MediaProcess状态，状态status
        if(Integer.parseInt(status) == 2){
            //任务成功
            MediaFiles mediaFiles = mediaFilesMapper.selectById(fileId);
            //更新media_file表的url
            mediaFiles.setUrl(url);

            mediaFilesMapper.updateById(mediaFiles);

            //更新media_process表的状态
            mediaProcess.setStatus("2");
            mediaProcess.setFinishDate(LocalDateTime.now());
            mediaProcessMapper.updateById(mediaProcess);
            //更新mediaProcessHistory表
            MediaProcessHistory mediaProcessHistory = new MediaProcessHistory();
            BeanUtils.copyProperties(mediaProcess,mediaProcessHistory);
            mediaProcessHistoryMapper.insert(mediaProcessHistory);
            //删除当前已执行任务
            mediaProcessMapper.deleteById(taskId);
        }
    }

}