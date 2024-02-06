package com.levelup.job.crawler.scraper;

import com.levelup.job.domain.model.CreateJob;
import com.levelup.job.infrastructure.enumeration.Company;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class YanoljaScraper implements Scraper<CreateJob> {

    private final Company company = Company.YANOLJA;
    private final ObjectProvider<WebDriver> prototypeBeanProvider;

    @Override
    public Company getCompany() {
        return company;
    }

    @Override
    public List<CreateJob> scrape() {
        WebDriver driver = prototypeBeanProvider.getObject();
        driver.get(company.getUrl());

        List<String> validJobGroups = List.of("Software Engineer", "Backend Engineer", "Frontend Engineer", "DevOps Engineer", "DBA", "Infra");
        List<WebElement> elements = driver.findElements(By.cssSelector("ul.iKWWXF > a"));

        List<CreateJob> jobs = elements.stream()
                .map(element -> {
                    String title = element.findElement(By.cssSelector("li.gBlYBh > div.eBKBVi > div.hsclxU")).getText();
                    String url = element.getAttribute("href");
                    String noticeEndDate = "채용 마감시";
                    String jobGroup = element.findElement(By.cssSelector("li.gBlYBh > div.eBKBVi > div.hSIEYY > span.gDzMae")).getText();

                    return CreateJob.of(
                            title,
                            company,
                            url,
                            noticeEndDate,
                            jobGroup
                    );
                })
                .filter(job -> validJobGroups.contains(job.getJobGroup()))
                .filter(job -> !job.getTitle().isEmpty() && !job.getTitle().isBlank())
                .collect(Collectors.toList());

        driver.quit();

        return jobs;
    }
}
