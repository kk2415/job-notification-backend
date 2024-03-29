package com.careers.crawler.crawler.scraper;

import com.careers.crawler.domain.enumeration.Company;
import com.careers.crawler.domain.model.Job;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class LineScraper implements Scraper<Job> {

    private final Company company = Company.LINE;
    private final ObjectProvider<WebDriver> prototypeBeanProvider;

    @Override
    public Company getCompany() {
        return company;
    }

    @Override
    public List<Job> scrape() {
        WebDriver driver = prototypeBeanProvider.getObject();

        String params = "ca=Engineering&ci=Bundang,Seoul";
        driver.get(company.getUrl(params));

        List<WebElement> elements = driver.findElements(By.cssSelector("ul.job_list > li"));
        List<Job> jobs = elements.stream()
                .map(element -> {
                    String title = element.findElement(By.cssSelector("a h3.title")).getText();
                    String jobNoticeUri = element.findElement(By.cssSelector("a")).getAttribute("href");
                    String jobNoticePath = jobNoticeUri.substring(jobNoticeUri.lastIndexOf("/"));
                    String noticeEndDate = element.findElement(By.cssSelector("a span.date")).getText();
                    String url = Company.LINE.getUrl() + jobNoticePath;

                    return Job.of(
                            title,
                            company,
                            url,
                            noticeEndDate,
                            ""
                    );
                })
                .filter(job -> !job.title().isEmpty() && !job.title().isBlank())
                .distinct()
                .toList();

        driver.quit();

        return jobs;
    }
}
