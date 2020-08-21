package com.example.shatding_springboot_mybatis_generator.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.shatding_springboot_mybatis_generator.entity.User;
import com.example.shatding_springboot_mybatis_generator.mapper.AddressMapper;
import com.example.shatding_springboot_mybatis_generator.mapper.UserMapper;
import com.example.shatding_springboot_mybatis_generator.util.SnowFlake;

@Controller
public class UserController {
	
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private AddressMapper addressMapper;
	
	@SuppressWarnings("serial")
	@RequestMapping("/user/save")
	@ResponseBody
	public String save() throws Exception {
		List<Map<String,Object>> listAdd = addressMapper.getAll();
		if(ObjectUtils.isEmpty(listAdd)){
			throw new Exception("请先插入Address表数据"); 
		}
		//SnowFlake snowFlake = new SnowFlake(2, 3);
		//模拟随机雪花算法id生成器每个库都有的数据测试
		List<Long> ids = new ArrayList<Long>(){{
			add(478171045170524160L);
			add(478171045170524161L);
			add(478171045174718464L);
			add(478171045174718465L);
			add(478171045174718466L);
			add(478171045174718467L);
		}};
        for (int i = 0; i < ids.size() ; i++) {
            User user=new User();
            user.setId(ids.get(i));
            user.setName("test"+i);
            user.setCityId(1%2==0?1:2);
            user.setCreateTime(new Date());
            user.setSex(i%2==0?1:2);
            user.setPhone("11111111"+i);
            user.setEmail("xxxxx");
            user.setCreateTime(new Date());
            user.setPassword("eeeeeeeeeeee");
            user.setAddId((long)listAdd.get(i).get("id"));
            userMapper.save(user);
        }

		return "success";
	}
	
	@SuppressWarnings("serial")
	@RequestMapping("/user/saveV2")
	@ResponseBody
	public String saveV2() throws Exception {
		List<Map<String,Object>> listAdd = addressMapper.getAll();
		if(ObjectUtils.isEmpty(listAdd)){
			throw new Exception("请先插入Address表数据"); 
		}
		SnowFlake snowFlake = new SnowFlake(2, 3);
		//模拟随机雪花算法id生成器每个库都有的数据测试
//		List<Long> ids = new ArrayList<Long>(){{
//			add(478171045170524160L);
//			add(478171045170524161L);
//			add(478171045174718464L);
//			add(478171045174718465L);
//			add(478171045174718466L);
//			add(478171045174718467L);
//		}};
        for (int i = 0; i < listAdd.size() ; i++) {
            User user=new User();
            user.setId(snowFlake.nextId());
            user.setName("test"+i);
            user.setCityId(1%2==0?1:2);
            user.setCreateTime(new Date());
            user.setSex(i%2==0?1:2);
            user.setPhone("11111111"+i);
            user.setEmail("xxxxx");
            user.setCreateTime(new Date());
            user.setPassword("eeeeeeeeeeee");
            user.setAddId((long)listAdd.get(i).get("id"));
            userMapper.save(user);
        }

		return "success";
	}
	
	@RequestMapping("/user/get/{id}")
	@ResponseBody
	public User get(@PathVariable Long id) {
		User user =  userMapper.get(id);
		System.out.println(user.getId());
		return user;
	}
	
	@RequestMapping("/user/getAll")
	@ResponseBody
	public List<User> getAll() {
		List<User> userList =  userMapper.getAll();
		return userList;
	}
	
	@RequestMapping("/user/getAllByPage")
	@ResponseBody
	public List<User> getAllByPage(@RequestParam Integer page,@RequestParam Integer size) {
		List<User> userList =  userMapper.getAllByPage(page,size);
		return userList;
	}

	@RequestMapping("/user/getAllJoin")
	@ResponseBody
	public List<Map<String,Object>> getAllJoin() {
		List<Map<String,Object>> userListJoin =  userMapper.getAllJoin();
		return userListJoin;
	}
	
	@RequestMapping("/user/getAllCount")
	@ResponseBody
	public Long getAllCount() {
		return userMapper.getAllCount();
	}
}

