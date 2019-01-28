package cn.rui97.ruigou.service.impl;

import cn.rui97.ruigou.domain.User;
import cn.rui97.ruigou.mapper.UserMapper;
import cn.rui97.ruigou.query.UserQuery;
import cn.rui97.ruigou.service.IUserService;
import cn.rui97.ruigou.util.PageList;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: rui
 * @Date: 2019/1/27 16:46
 * @Description:
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public PageList<User> selectPageList(UserQuery query) {
        Page<User> page = new Page<>(query.getPage(),query.getRows());
        //当我们传入Page,表示要做分页查询,会把查询总数设置在Page的total
        List<User> rows = userMapper.selectPageList(page,query);
        long total = page.getTotal();

        return new PageList<>(total,rows);
    }
}
