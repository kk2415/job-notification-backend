package com.levelup.job.crawler.jsoupConnetion;

import com.levelup.job.domain.enumeration.Company;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

@Component("NaverConnectionMaker")
public class NaverConnectionMaker implements JsoupConnectionMaker {

    @Override
    public Connection makeConnection() {
        return Jsoup.connect(Company.NAVER.getUrl());
    }

    @Override
    public Connection makeConnection(String param) {
        return Jsoup.connect(Company.NAVER.getUrl() + param);
    }
}