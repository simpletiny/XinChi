package apptest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.xinchi.bean.TempBean;
import com.xinchi.common.DateUtil;

/**
 * 
 * @author Administrator 通过aop拦截后执行具体操作
 */
@Aspect
@Component
public class SomeTest {

	public static void main(String[] aa) {
		List<TempBean> a = new ArrayList<TempBean>();
		TempBean d1 = new TempBean();
		TempBean d2 = new TempBean();
		TempBean d3 = new TempBean();
		TempBean d4 = new TempBean();
		d1.setConnect_date("2018-03-22");
		d2.setConnect_date("1988-03-22");
		d3.setConnect_date("2018-01-06");
		d4.setConnect_date("2018-07-08");
		a.add(d1);
		a.add(d2);
		a.add(d3);
		a.add(d4);

		Collections.sort(a);
		for (TempBean c : a) {
			System.out.println(c.getConnect_date());
		}

	}
}