package com.google.mobilesafe.net.okhttputils.builder;

import com.google.mobilesafe.net.okhttputils.OkHttpUtils;
import com.google.mobilesafe.net.okhttputils.request.OtherRequest;
import com.google.mobilesafe.net.okhttputils.request.RequestCall;


public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers).build();
    }
}
