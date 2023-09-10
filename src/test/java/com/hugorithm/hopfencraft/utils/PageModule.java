package com.hugorithm.hopfencraft.utils;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.data.domain.Page;


public class PageModule extends SimpleModule {
    public PageModule() {
        addDeserializer(Page.class, new PageDeserializer());
    }
}
