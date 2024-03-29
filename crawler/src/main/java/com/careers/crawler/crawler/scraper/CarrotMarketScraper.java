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
public class CarrotMarketScraper implements Scraper<Job> {

    private final Company company = Company.CARROT_MARKET;
    private final ObjectProvider<WebDriver> prototypeBeanProvider;

    @Override
    public Company getCompany() {
        return company;
    }

    @Override
    public List<Job> scrape() {
        WebDriver driver = prototypeBeanProvider.getObject();
        driver.get(company.getUrl());

        List<WebElement> elements = driver.findElements(By.cssSelector("ul.c-jpGEAj > div"));
        List<Job> jobs = elements.stream()
                .map(element -> {
                    String title = element.findElement(By.cssSelector("li.c-deAcZv > a.c-hCDnza > div.c-MPFyP > h3.c-boyXyq")).getAccessibleName();
                    String url = element.findElement(By.cssSelector("li.c-deAcZv > a.c-hCDnza")).getAttribute("href");
                    String noticeEndDate = "채용 마감시";

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
