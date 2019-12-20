package com.Triz.SIMS.util;
/**
 * 
 * @author Triz-X
 *
 *String类的公用方法
 *验证码的验证作用
 */
public class StringUtil {
	public static boolean isEmpty(String str){
		if(str == null || "".equals(str))return true;
		return false;
		
	}
}
