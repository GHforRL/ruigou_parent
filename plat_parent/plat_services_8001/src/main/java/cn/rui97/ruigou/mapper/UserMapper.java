package cn.rui97.ruigou.mapper;

import cn.rui97.ruigou.domain.User;
import cn.rui97.ruigou.query.UserQuery;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;

import java.util.List;

/**
 * @Auther: rui
 * @Date: 2019/1/27 16:19
 * @Description:
 */
public interface UserMapper extends BaseMapper<User> {
    List<User> selectPageList(Page<User> page, UserQuery query);
}
