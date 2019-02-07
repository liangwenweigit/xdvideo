package com.fly.xdvideo;

import com.sun.org.apache.xpath.internal.SourceTree;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.bind.SchemaOutputResolver;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class XdvideoApplicationTests {

	@Test
	public void contextLoads() {
		Long nowTime = new Date().getTime();
		Long newTime = nowTime + (1000*60*60*24*30);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(nowTime);
        System.out.println(newTime);
        System.out.println(sd.format(new Date(nowTime)));
        System.out.println(sd.format(new Date(newTime)));//30天后时间


        SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
        String maxDateStr = "2019-02-05";
        String minDateStr = "";
        Calendar calc =Calendar.getInstance();
        try {
            calc.setTime(sdf.parse(maxDateStr));
            calc.add(calc.DATE, 30);
            Date minDate = calc.getTime();
            minDateStr = sdf.format(minDate);
            System.out.println("minDateStr:"+minDateStr);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
    }

}

