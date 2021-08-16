package com.huongdanjava.mvnforum.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huongdanjava.mvnforum.usecases.adapter.GoogleRecaptchaAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GoogleRecaptchaAdapterImpl implements GoogleRecaptchaAdapter {

  private static final String SITE_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

  @Override
  public boolean verify(String gRecaptchaResponse, String secretKey) throws IOException {
    try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
      HttpPost httpPost = new HttpPost(SITE_VERIFY_URL);

      List<NameValuePair> nvps = new ArrayList<>();
      nvps.add(new BasicNameValuePair("response", gRecaptchaResponse));
      nvps.add(new BasicNameValuePair("secret", secretKey));
      try (UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nvps)) {
        httpPost.setEntity(formEntity);

        try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
          if (response.getCode() != 200) {
            return false;
          }

          try (HttpEntity entity = response.getEntity()) {
            String result = EntityUtils.toString(entity);

            ObjectMapper om = new ObjectMapper();
            Map<String, Object> map = om.readValue(result, Map.class);

            return ((Boolean) map.get("success")).booleanValue();
          }
        } catch (ParseException e) {
          log.error(e.getMessage(), e);
        }
      }
    }

    return false;
  }
}
