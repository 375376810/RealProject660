package com.project.mobilesafe660.test;

import com.project.mobilesafe660.db.dao.BlackNumberDao;

import android.test.AndroidTestCase;

/**
 * 黑名单单元测试
 * @author 袁星明
 */
public class TestBlackNumber extends AndroidTestCase {

	public void testAdd() {
		BlackNumberDao dao = BlackNumberDao.getInstence(getContext());
		dao.add("110", 1);
	}
	
	public void testDelete() {
		BlackNumberDao dao = BlackNumberDao.getInstence(getContext());
		dao.delete("110");
	}
	
	public void testUpdata() {
		BlackNumberDao dao = BlackNumberDao.getInstence(getContext());
		dao.update("110", 2);
	}
	
	public void testFind() {
		BlackNumberDao dao = BlackNumberDao.getInstence(getContext());
		boolean exist = dao.find("110");
		assertEquals(false, exist);
	}
	
}
