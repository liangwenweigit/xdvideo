package com.fly.xdvideo.service.impl;
import com.fly.xdvideo.config.WeChatConfig;
import com.fly.xdvideo.domain.User;
import com.fly.xdvideo.domain.Video;
import com.fly.xdvideo.domain.VideoOrder;
import com.fly.xdvideo.dto.VideoOrderDto;
import com.fly.xdvideo.mapper.UserMapper;
import com.fly.xdvideo.mapper.VideoMapper;
import com.fly.xdvideo.mapper.VideoOrderMapper;
import com.fly.xdvideo.service.VideoOrderService;
import com.fly.xdvideo.utils.CommonUtils;
import com.fly.xdvideo.utils.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 订单业务层实现类
 * @author liang
 * @date 2019/2/3 - 15:47
 */
@Service
public class VideoOrderServiceImpl implements VideoOrderService {

    @Autowired
    private VideoOrderMapper videoOrderMapper;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WeChatConfig weChatConfig;

    @Override
    public VideoOrder saveOrder(VideoOrderDto videoOrderDto) throws Exception {
      //查找商品信息
      Video video = videoMapper.findVideoById(videoOrderDto.getVideoId());
      //查询用户信息
      User user = userMapper.findById(videoOrderDto.getUserId());
      //构造订单(根据从上面查出来的数据保存)保存数据库，这里订单表的冗余数据起作用了
      //到配置文件设置微信商户支付配置
      VideoOrder videoOrder = new VideoOrder();
      videoOrder.setTotalFee(video.getPrice());
      videoOrder.setVideoImg(video.getCoverImg());
      videoOrder.setVideoId(video.getId());
      videoOrder.setVideoTitle(video.getTitle());
      videoOrder.setCreateTime(new Date());

      videoOrder.setState(0);//0表示未支付
      videoOrder.setDel(0);//0表示未删除

      videoOrder.setUserId(user.getId());
      videoOrder.setOpenid(user.getOpenid());
      videoOrder.setHeadImg(user.getHeadImg());
      videoOrder.setNickname(user.getName());//切记这个名字是base64位的

      videoOrder.setIp(videoOrderDto.getIp());//下单用户ip
      videoOrder.setOutTradeNo(CommonUtils.getUUID());//流水号
      //把上面生成的订单 保存数据库
      videoOrderMapper.insert(videoOrder);
      //调用下面抽取的统一下单方法
      unifiedOrder(videoOrder);
      //获取codeurl

      //生成二维码


        return null;
    }

    /**
     * 统一下单方法
     * @return
     */
    private String unifiedOrder(VideoOrder videoOrder) throws Exception {
        //生成签名，生成签名的参数看这里，是的是必须的，否的可以不传
        //https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_1
        SortedMap<String,String> params = new TreeMap<>();
        params.put("appid",weChatConfig.getAppId());//公众号appid
        params.put("mch_id",weChatConfig.getMer_id());//商户号
        params.put("nonce_str",CommonUtils.getUUID());//随机32位字符串uuid即可
        params.put("body",videoOrder.getVideoTitle());//例如：腾讯充值中心-QQ会员充值
        params.put("out_trade_no",videoOrder.getOutTradeNo());//订单流水号/订单号
        params.put("total_fee",videoOrder.getTotalFee().toString());//订单价格，单位为：分
        params.put("spbill_create_ip",videoOrder.getIp());//下单ip
        params.put("notify_url",weChatConfig.getCallback());//微信回调地址
        params.put("trade_type","NATIVE");//交易类型
        //使用工具类
        String sign = WXPayUtil.createSign(params,weChatConfig.getKey());
        params.put("sign",sign);//签名
        //map转xml
        String payXml = WXPayUtil.mapToXml(params);
        //写到这里可以测试一下，启动程序测试访问：http://localhost:8088/user/api/order/save_order?video_id=1
        System.out.println(payXml);//最后输出下面的xml成功
        //把下面生成的xml复制到这里测试校验：https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=20_1
        //注意 需要绑定商户号的登录了才能使用上面的测试校验连接
        /**
         * <xml>
         <appid>wxyx1p9v991wx6i8ub</appid>
         <body>SpringBoot+Maven整合Websocket课程</body>
         <mch_id>0482820207</mch_id>
         <nonce_str>D26890815ABF49B0B64CD97ED8F6CC1E</nonce_str>
         <notify_url>http://xdclasstest.ngrok.xiaomiqiu.cn/api/wechat/user/callback</notify_url>
         <out_trade_no>AB6D58092E7B44E3BC8BCEF14509320B</out_trade_no>
         <sign>EF8A4A17A9C3D48DAC6D0258E584418B</sign>
         <spbill_create_ip>0:0:0:0:0:0:0:1</spbill_create_ip>
         <total_fee>1000</total_fee>
         <trade_type>NATIVE</trade_type>
         </xml>
         */
        //统一下单

      return null;
    }
}
