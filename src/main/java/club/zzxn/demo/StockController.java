package club.zzxn.demo;

import static java.lang.System.currentTimeMillis;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.mail.internet.MimeMessage;

import org.assertj.core.util.Arrays;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import net.hasor.utils.StringUtils;

/**
 * @author zhangzhenxin03 <zhangzhenxin03@kuaishou.com>
 * Created on 2021-10-08
 */
@RestController
@Slf4j
public class StockController {
    private final static String STOCK_URL = "http://hq.sinajs.cn/list=%s";
    private static List<String> stock_list = Lists.newArrayList("sz002106");
    private final static AtomicInteger atomicInteger = new AtomicInteger(0);
    private static String preResult = "";
    private static long TIME = currentTimeMillis();
    @Autowired
    JavaMailSender mailSender;//注入QQ发送邮件的bean

    @Scheduled(cron="0 0 6 * * ? ")
    public void clearTime() {
        atomicInteger.set(0);
    }

    @RequestMapping("/send")
    @Scheduled(cron="0/1 0 9-15 * * ? ")
    public void process() {
        log.info("start process ");
        RestTemplate restTemplate = new RestTemplate();
        String rep = restTemplate.getForObject(getStockUrl(), String.class);
        String[] stocks = StringUtils.split(rep, ";");
        if (Arrays.isNullOrEmpty(stocks)) {
            log.info("有问题----bug");
            return;
        }
        for (String stock : stocks) {
            if (StringUtils.isBlank(stock)) {
                continue;
            }
            if (StringUtils.isEmpty(preResult)) {
                preResult = stock;
            }
            if (StringUtils.equalsIgnoreCase(preResult, stock)) {
                log.info("忽略。。。。。。。。。。。");
                continue;
            }
            String[] stockInfo = stock.substring(stock.indexOf("=") + 1).split(",");
            String stockName = stockInfo[0];
//            String todayOpeningPrice = stockInfo[1];
            String yesterdayClosingPrice = stockInfo[2];
            String currentPrice = stockInfo[3];
            Double percentageGain = (Double.parseDouble(currentPrice) - Double.parseDouble(yesterdayClosingPrice))
                    / Double.parseDouble(yesterdayClosingPrice);
            if (percentageGain > 0.03) {
                if (atomicInteger.get() >= 2) {
                    return;
                }
                if ((currentTimeMillis() - TIME) < (1000 * 60 * 20)) {
                    return;
                }
                log.info("{}.涨幅超过3%", stockName);
                String body = stockName + "：涨幅超过3%";
                sendQQMessage(body);
            }
        }
    }
    private String getStockUrl() {
        String join = StringUtils.join(stock_list.toArray(), ",");
        return String.format(STOCK_URL, join);
    }
    private void sendQQMessage(String body) {
        try {
            String qq = "495960666@qq.com";
            MimeMessage mimeMessage = this.mailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setFrom("495960666@qq.com");//设置发件qq邮箱
            message.setTo(qq);				//设置收件人
            message.setSubject("小金库");	//设置标题
            message.setText(body);  	//第二个参数true表示使用HTML语言来编写邮件
//            FileSystemResource file = new FileSystemResource(
//            File file = new File("图片路径");
//            helper.addAttachment("图片.jpg", file);//添加带附件的邮件
//            helper.addInline("picture",file);//添加带静态资源的邮件
            this.mailSender.send(mimeMessage);
            atomicInteger.incrementAndGet();
            TIME = currentTimeMillis();
            log.info("send email success");
        } catch (Exception ex) {
            log.error("send email fail", ex);
        }
    }
}
