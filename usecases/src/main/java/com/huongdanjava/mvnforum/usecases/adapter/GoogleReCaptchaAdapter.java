package com.huongdanjava.mvnforum.usecases.adapter;

import java.io.IOException;

public interface GoogleReCaptchaAdapter {

  boolean verify(String gRecaptchaResponse, String secretKey) throws IOException;

}
