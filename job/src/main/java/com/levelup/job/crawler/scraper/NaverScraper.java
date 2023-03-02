package com.levelup.job.crawler.scraper;

import com.levelup.job.crawler.jsoupConnetion.JsoupConnectionMaker;
import com.levelup.job.domain.VO.JobVO;
import com.levelup.job.domain.VO.NaverJobVO;
import com.levelup.job.domain.enumeration.Company;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NaverScraper {

    @Value("${webdriver.chrome.driver}")
    private String chromeDriver;

    private JsoupTemplate jsoupTemplate;

    public NaverScraper(@Qualifier("NaverConnectionMaker") JsoupConnectionMaker connectionMaker) {
        jsoupTemplate = JsoupTemplate.from(connectionMaker);
    }

    public List<JobVO> findJobs() {
        System.setProperty("webdriver.chrome.driver", chromeDriver);
        WebDriver driver = new ChromeDriver();

        String params = "?subJobCdArr=1010001%2C1010002%2C1010003%2C1010004%2C1010005%2C1010006%2C1010007%2C1010008%2C1010020%2C1020001%2C1030001%2C1030002%2C1040001%2C1060001&sysCompanyCdArr=KR%2CNB%2CWM%2CSN%2CNL%2CWTKR%2CNFN%2CNI&empTypeCdArr=&entTypeCdArr=&workAreaCdArr=&sw=&subJobCdData=1010001&subJobCdData=1010002&subJobCdData=1010003&subJobCdData=1010004&subJobCdData=1010005&subJobCdData=1010006&subJobCdData=1010007&subJobCdData=1010008&subJobCdData=1010020&subJobCdData=1020001&subJobCdData=1030001&subJobCdData=1030002&subJobCdData=1040001&subJobCdData=1060001&sysCompanyCdData=KR&sysCompanyCdData=NB&sysCompanyCdData=WM&sysCompanyCdData=SN&sysCompanyCdData=NL&sysCompanyCdData=WTKR&sysCompanyCdData=NFN&sysCompanyCdData=NI";
        driver.get(Company.NAVER.getUrl() + params);

        List<WebElement> elements = scrollToEnd(driver);

        List<JobVO> naverJobs = elements.stream().map(jobElement -> {
            String title = jobElement.findElement(By.cssSelector("a.card_link > h4.card_title")).getText();

            String htmlOnClickAttrValue = jobElement.findElement(By.cssSelector("a.card_link")).getAttribute("onclick");
            String jobNoticeKey;
            if (htmlOnClickAttrValue.contains("'")) {
                jobNoticeKey = htmlOnClickAttrValue.substring(htmlOnClickAttrValue.indexOf("'") + 1, htmlOnClickAttrValue.lastIndexOf("'"));
            } else {
                jobNoticeKey = htmlOnClickAttrValue.substring(htmlOnClickAttrValue.indexOf("(") + 1, htmlOnClickAttrValue.lastIndexOf(")"));
            }

            String url = "https://recruit.navercorp.com/rcrt/view.do?annoId=" + jobNoticeKey;

            String noticeEndDate = jobElement.findElement(By.cssSelector("dl.card_info dd.info_text:last-child")).getText();

            return NaverJobVO.of(title, url, noticeEndDate);
        }).collect(Collectors.toUnmodifiableList());

        driver.quit();

        return naverJobs;
    }

    private List<WebElement> scrollToEnd(WebDriver driver) {
        List<WebElement> elements = driver.findElements(By.cssSelector("ul.card_list > li"));
        int prevSize = elements.size();

        for (int i = 0; i < 100; i++) {
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");

            // Wait for more items to load
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            elements = driver.findElements(By.cssSelector("ul.card_list > li"));
            int curSize = elements.size();

            if (prevSize == curSize) {
                break;
            }
            prevSize = curSize;
        }

        return elements;
    }
}
