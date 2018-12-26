package com.ambition.util.excel;

import java.util.Date;

/**
 * Created by IntelliJ IDEA. User: SUKE Date: 12-8-21 Time: 上午7:27 To change
 * this template use File | Settings | File Templates.
 */
public class EntityStudent {
	private long id;
	private String name;
	private int age;
	private boolean sex;
	private Date birthday;

	public EntityStudent() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EntityStudent(long id, String name, int age, boolean sex,
			Date birthday) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
		this.sex = sex;
		this.birthday = birthday;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public boolean getSex() {
		return sex;
	}

	public void setSex(boolean sex) {
		this.sex = sex;
	}

	public Date getBirthday() {
		return birthday;

	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
}
