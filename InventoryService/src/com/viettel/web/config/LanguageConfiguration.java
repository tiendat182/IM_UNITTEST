package com.viettel.web.config;

import com.google.common.collect.Lists;
import com.viettel.fw.common.util.BundleUtil;
import com.viettel.ws.common.utils.Locate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author nhannt34
 * @since 17/12/2015
 */
@Configuration
public class LanguageConfiguration {
    @Bean
    public BundleUtil bundleUtil() {
        BundleUtil bundleUtil = new BundleUtil();
        bundleUtil.setRunEnvironment("service");
        bundleUtil.setLanguageLocation("com.viettel.language.lang");
        bundleUtil.setSupportedLanguages(Lists.newArrayList(
                new Locate("en", "US"),
                new Locate("vi", "VN")
        ));
        bundleUtil.setSortCode("vietnamese");
        bundleUtil.init();
        return bundleUtil;
    }
}
