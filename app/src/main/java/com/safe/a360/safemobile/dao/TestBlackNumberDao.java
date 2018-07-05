package com.safe.a360.safemobile.dao;

import android.content.Context;
import android.test.AndroidTestCase;
import com.safe.a360.safemobile.bean.BlackNumberInfo;
import java.util.List;
import java.util.Random;

/**
 * Created by BRUCE on 2016/5/29 0029.
 */
public class TestBlackNumberDao extends AndroidTestCase {
  

    public Context mContext;
	@Override
    protected void setUp() throws Exception {
        this.mContext = getContext();
        super.setUp();
    }

    public void testAdd(){
        BlackNumberDao dao = new BlackNumberDao(mContext);
        Random random = new Random();
        for(int i = 0; i < 200; i ++) {
            long number = 15100000000L + i;
            dao.add(number + "" , String.valueOf(random.nextInt(3) + 1));
        }
    }
    public void testDelete(){
        BlackNumberDao dao = new BlackNumberDao(mContext);
        boolean delete = dao.delete("151000000001");
        assertEquals(true,delete);
    }
    public void testFind(){
        BlackNumberDao dao = new BlackNumberDao(mContext);
        String number = dao.findNumber("15100000004");
        System.out.println(number);
    }

    public void testFindAll(){
        BlackNumberDao dao = new BlackNumberDao(mContext);
        List<BlackNumberInfo> blackNumberInfos = dao.findAll();
        for (BlackNumberInfo blackNumberInfo : blackNumberInfos) {
           System.out.println(blackNumberInfo.getMode() + "" + blackNumberInfo.getNumber());
        }
    }

}
