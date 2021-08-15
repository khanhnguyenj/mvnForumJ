package com.huongdanjava.mvnforum.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import com.huongdanjava.mvnforum.usecases.adapter.GoogleReCaptchaAdapter;

public class GoogleReCaptchaAdapterImpl implements GoogleReCaptchaAdapter {

  public static final String SITE_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

  @Override
  public boolean verify(String gRecaptchaResponse, String secretKey) throws IOException {
    try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
      HttpPost httpPost = new HttpPost(SITE_VERIFY_URL);

      List<NameValuePair> nvps = new ArrayList<>();
      nvps.add(new BasicNameValuePair("secret", secretKey));
      nvps.add(new BasicNameValuePair("response", gRecaptchaResponse));
      httpPost.setEntity(new UrlEncodedFormEntity(nvps));

      try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
        System.out.println(response.getCode() + " " + response.getReasonPhrase());
        HttpEntity entity = response.getEntity();

        EntityUtils.consume(entity);
      }
    }

    return false;
  }
}
