package com.levelup.job.crawler.scraper;

import com.levelup.job.domain.VO.JobVO;
import com.levelup.job.domain.VO.TossJobVO;
import com.levelup.job.domain.enumeration.Company;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TossScraper {

    @Value("${webdriver.chrome.driver}")
    private String chromeDriver;

    public List<JobVO> findJobs() {
        System.setProperty("webdriver.chrome.driver", chromeDriver);
        WebDriver driver = new ChromeDriver();

        List<JobVO> jobs = new ArrayList<>();

        List<String> categories = List.of("core-system", "data", "design", "engineering-platform", "engineering-product");
        categories.forEach((category) -> {
            String params = "?isNewCareer=true&category=" + category;
            driver.get(Company.TOSS.getUrl() + params);

            List<WebElement> elements = driver.findElements(By.cssSelector("a.css-g65o95"));
            List<TossJobVO> tossJobList = elements.stream().map(element -> {
                String detailCompany = element.findElement(By.cssSelector("div.css-1xr69i7 > div.css-wp89al div.css-g3elji:last-child")).getText();
                String title = element.findElement(By.cssSelector("span.css-16tmfdr")).getText();
                title = "[" + detailCompany + "]" + title;

                String url = element.getAttribute("href");
                String noticedEndedDate = "채용 마감시";

                return TossJobVO.of(title, url, noticedEndedDate);
            }).toList();

            jobs.addAll(tossJobList);
        });

        driver.quit();

        return jobs;
    }
}