package com.project.mobilesafe660.test;

import java.util.Random;

import android.test.AndroidTestCase;

import com.project.mobilesafe660.db.dao.BlackNumberDao;

/**
 * 黑名单单元测试
 * @author 袁星明
 */
public class TestBlackNumber extends AndroidTestCase {

	public void testAdd() {
		BlackNumberDao dao = BlackNumberDao.getInstence(getContext());
		//dao.add("110", 1);
		Random random = new Random();
		for (int i = 0; i <= 100; i++) {
			int mode = random.nextInt(3) + 1;
			if (i<10) {
				dao.add("1321234567" + i, mode);
			}else {
				dao.add("135123456" +i, mode);
			}
		}
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
