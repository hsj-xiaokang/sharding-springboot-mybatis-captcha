package com.example.shatding_springboot_mybatis_generator.entity;

import java.util.Date;

public class User {
	private Long id;
	private String name;
	private String phone;
	private String email;
	private String password;
	private Integer cityId;
    private Date createTime;
    private Integer sex;
    private Long addId;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public User(Long id, String name, String phone, String email, String password, Integer cityId, Date createTime,
			Integer sex) {
		super();
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.password = password;
		this.cityId = cityId;
		this.createTime = createTime;
		this.sex = sex;
	}
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Long getAddId() {
		return addId;
	}
	public void setAddId(Long addId) {
		this.addId = addId;
	}
    
}
