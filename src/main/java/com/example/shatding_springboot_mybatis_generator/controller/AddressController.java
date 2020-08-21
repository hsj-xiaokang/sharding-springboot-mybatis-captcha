package com.example.shatding_springboot_mybatis_generator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.shatding_springboot_mybatis_generator.entity.Address;
import com.example.shatding_springboot_mybatis_generator.mapper.AddressMapper;
import com.example.shatding_springboot_mybatis_generator.util.SnowFlake;

@Controller
public class AddressController {

	@Autowired
	private AddressMapper addressMapper;

	@RequestMapping("/address/save")
	@ResponseBody
	public String save() {
		SnowFlake snowFlake = new SnowFlake(2, 3);
		for (int i = 0; i <100000 ; i++) {
			Address address=new Address();
			address.setCode("code_"+i);
			address.setName("name_"+i);
			address.setPid(i+"");
			address.setType(0);
			address.setLit(i%2==0?1:2);
			address.setId(snowFlake.nextId());
			addressMapper.save(address);
/*			long random = new SnowFlake(2, 3).nextId()%2;
			if(random%2 == 1){System.out.println(random);}*/
		}

		return "success";
	}
	
	@RequestMapping("/address/get/{id}")
	@ResponseBody
	public Address get(@PathVariable Long id) {
		return addressMapper.get(id);
	}
	
	@RequestMapping("/address/getAllCount")
	@ResponseBody
	public Long getAllCount() {
		return addressMapper.getAllCount();
	}
}

