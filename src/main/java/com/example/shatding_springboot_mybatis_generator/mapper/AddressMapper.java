package com.example.shatding_springboot_mybatis_generator.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.example.shatding_springboot_mybatis_generator.entity.Address;
@Mapper
public interface AddressMapper {
	/**
	 * 保存
	 */
	void save(Address address);
	
	/**
	 * 查询
	 * @param id
	 * @return
	 */
	Address get(Long id);
	/**
	 * 获取所有
	 *  @author:heshengjin qq:2356899074
	    @date 2020年7月7日 下午3:48:00
	 */
	List<Map<String,Object>> getAll();
	/**
	 * 总数查询
	 *  @author:heshengjin qq:2356899074
	    @date 2020年7月8日 下午3:15:20
	 */
	Long getAllCount();
}