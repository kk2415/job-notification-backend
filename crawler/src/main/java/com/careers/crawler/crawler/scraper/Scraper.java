package com.careers.crawler.crawler.scraper;

import com.careers.crawler.domain.enumeration.Company;

import java.util.List;

public interface Scraper<T> {
    List<T> scrape();
    Company getCompany();
}
