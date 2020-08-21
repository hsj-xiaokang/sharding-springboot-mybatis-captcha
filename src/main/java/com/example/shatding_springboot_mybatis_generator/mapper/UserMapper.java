package com.example.shatding_springboot_mybatis_generator.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.shatding_springboot_mybatis_generator.entity.User;
@Mapper
public interface UserMapper {
	/**
	 * 保存
	 */
	void save(User user);
	
	/**
	 * 查询
	 * @param id
	 * @return
	 */
	User get(@Param("page") Long id);
	/**
	 * 全部
	 *  @author:heshengjin qq:2356899074
	    @date 2020年7月7日 下午2:35:27
	 */
	List<User> getAll();
	/**
	 * 分页
	 *  @author:heshengjin qq:2356899074
	    @date 2020年7月7日 下午2:46:45
	 */
	List<User> getAllByPage(@Param("page") Integer page,@Param("size") Integer size);
	/**
	 * join测试
	 *  @author:heshengjin qq:2356899074
	    @date 2020年7月7日 下午3:00:23
	 */
	List<Map<String,Object>> getAllJoin();
	/**
	 * 总数查询
	 *  @author:heshengjin qq:2356899074
	    @date 2020年7月8日 下午3:15:20
	 */
	Long getAllCount();
}
