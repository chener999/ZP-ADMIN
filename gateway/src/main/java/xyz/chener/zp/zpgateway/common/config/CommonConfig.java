package xyz.chener.zp.zpgateway.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: chenzp
 * @Date: 2023/01/13/08:12
 * @Email: chen@chener.xyz
 */

@Component
@ConfigurationProperties(prefix = "zp")
@RefreshScope
public class CommonConfig {

    private final Jwt jwt = new Jwt();

    private final Security security = new Security();

    public Jwt getJwt() {
        return jwt;
    }

    public Security getSecurity() {
        return security;
    }

    public static class Jwt{
        private String salt = "123";
        private Integer expires = 1000*60*60;

        public String getSalt() {
            return salt;
        }

        public void setSalt(String salt) {
            this.salt = salt;
        }

        public Integer getExpires() {
            return expires;
        }

        public void setExpires(Integer expires) {
            this.expires = expires;
        }
    }

    public static class Security{
        private List<String> writeList = new ArrayList<>();

        private String feignCallSlat = "123";

        public List<String> getWriteList() {
            return writeList;
        }

        public void setWriteList(List<String> writeList) {
            this.writeList = writeList;
        }

        public String getFeignCallSlat() {
            return feignCallSlat;
        }

        public void setFeignCallSlat(String feignCallSlat) {
            this.feignCallSlat = feignCallSlat;
        }
    }

}
