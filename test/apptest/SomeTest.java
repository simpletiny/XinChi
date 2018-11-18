package apptest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.xinchi.common.DateUtil;

/**
 * 
 * @author Administrator 通过aop拦截后执行具体操作
 */
@Aspect
@Component
public class SomeTest {

	public static void main(String[] aa) {
	System.out.println(DateUtil.addDate("2018-11-18", 1));
	}
}