package xyz.chener.zp.zpusermodule.config.oplog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.chener.zp.zpusermodule.config.oplog.entity.OperateRecord;
import xyz.chener.zp.zpusermodule.config.oplog.entity.OperateRecordDto;

import java.util.List;

/**
 * (OperateRecord)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-12 20:18:32
 */
@Mapper
public interface OperateRecordDao extends BaseMapper<OperateRecord> {

    int insertRecord(@Param("op") OperateRecord operateRecord);

    List<OperateRecordDto> getList(@Param("dto") OperateRecordDto dto);

}

