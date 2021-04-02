package com.quan12yt.trackingcronjob.unittest.utiltest;


import com.quan12yt.trackingcronjob.util.ValidateUtil;
import org.junit.Assert;
import org.junit.Test;

public class ValidateUtilTest {

    @Test
    public void invalidEmailTest(){
        Assert.assertTrue(!ValidateUtil.isEmail("ASD"));
    }

    @Test
    public void validEmailTest(){
        Assert.assertTrue(ValidateUtil.isEmail("quan12yt@gmail.com"));
    }


    @Test
    public void dateEmptyTest(){
        Assert.assertTrue(ValidateUtil.isEmptyOrNull("","ASD"));
    }
    @Test
    public void urlEmptyTest(){
        Assert.assertTrue(ValidateUtil.isEmptyOrNull("dfdg",""));
    }
    @Test
    public void dateNullTest(){
        Assert.assertTrue(ValidateUtil.isEmptyOrNull(null,"ASD"));
    }
    @Test
    public void urlNullTest(){
        Assert.assertTrue(ValidateUtil.isEmptyOrNull("dfdg",null));
    }

//    @Test
//    public void validEmailTest(){
//        Assert.assertTrue(ValidateUtil.isEmail("quan12yt@gmail.com"));
//    }
}
