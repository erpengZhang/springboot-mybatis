package com.zhang.firsapplication.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.Consts;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Create by administrator on 2019/6/26 0026
 **/
public class PaChong {
    public static void main(String[] args) throws IOException, URISyntaxException {
        boolean flag = true;
        int offset = 0;
        while (flag) {
            String json = example(String.valueOf(offset), "20");
            JSONObject jsonObject = JSON.parseObject(json);
            JSONArray data = jsonObject.getJSONArray("data");
            for (Object datum : data) {
                System.out.println(datum);
                JSONObject ajsonObject = (JSONObject) datum;
                String avatar = ajsonObject.getString("avatar_url_template").replace("{size}", "r");
                saveToFile(avatar,ajsonObject.getString("name")+".jpg");
            }
            if ((flag = !Boolean.valueOf(jsonObject.getString("is_end")))) {
                offset += 20;
            }
        }
//        saveToFile("https://pic2.zhimg.com/v2-d6d6330a182bdf7bc18b510218ebff35_is.jpg","aa.jpg");
    }

    public static String example(String offset, String limit) throws URISyntaxException, IOException {

        //创建httpclient实例，采用默认的参数配置
        CloseableHttpClient httpClient = HttpClients.createMinimal();
        //通过URIBuilder类创建URI
        URI uri = new URIBuilder().setScheme("https")
                .setHost("www.zhihu.com")
                .setPath("/api/v4/members/yunaiv/followers")
//                .setPath("?include=data%5B*%5D.answer_count%2Carticles_count%2Cgender%2Cfollower_count%2Cis_followed%2Cis_following%2Cbadge%5B%3F(type%3Dbest_answerer)%5D.topics&offset=0&limit=2000000")
//                .setParameter("include", UrlUtil.decode("data%5B*%5D.answer_count%2Carticles_count%2Cgender%2Cfollower_count%2Cis_followed%2Cis_following%2Cbadge%5B%3F(type%3Dbest_answerer)%5D.topics"))
                .setParameter("offset", offset)
                .setParameter("limit", limit)
                .build();

        HttpGet get = new HttpGet(uri);   //使用Get方法提交

        //请求的参数配置，分别设置连接池获取连接的超时时间，连接上服务器的时间，服务器返回数据的时间
        RequestConfig config = RequestConfig.custom()
                .setConnectionRequestTimeout(3000)
                .setConnectTimeout(3000)
                .setSocketTimeout(3000)
                .build();
        //配置信息添加到Get请求中
        get.setConfig(config);
//        get.addHeader("Accept", "text/html");
//        get.addHeader("Accept-Charset", "utf-8");
//        get.addHeader("Accept-Encoding", "gzip");
        get.setHeader(":authority", "www.zhihu.com");
        get.setHeader(":method", "GET");
        get.setHeader(":path", "/api/v4/members/yunaiv/followers");
        get.setHeader(":scheme", "https");
        get.setHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        get.addHeader("accept-language", "gzip, deflate, br");
        get.addHeader("accept-encoding", "zh-CN,zh;q=0.9,en;q=0.8,ja;q=0.7");
        get.setHeader("cache-control", "max-age=0");
        get.setHeader("cookie", "_zap=45bb3658-edcf-4406-962f-6771970ebb49; _xsrf=Q9oBlVZAiBAwxGEIpE3le3LyUL7bvI1t; __utma=51854390.546477146.1531124533.1531124533.1555912203.2; __utmz=51854390.1555912203.2.1.utmcsr=zhihu.com|utmccn=(referral)|utmcmd=referral|utmcct=/people/xian-zai-mei-you-guo-qu-a/collections; __utmv=51854390.100--|2=registration_date=20180316=1^3=entry_date=20180316=1; d_c0=\"AEBsmB6Edg-PTsyw4yQW7uDqgP9yGaJ6nzU=|1558431518\"; z_c0=\"2|1:0|10:1558658544|4:z_c0|92:Mi4xYUJzM0NBQUFBQUFBUUd5WUhvUjJEeVlBQUFCZ0FsVk44SXZVWFFCR2hpcjNJc1ZDdUdBaXF3SXZMZ2wzUGdmQ1dB|912e9b219ed592c13c4ed667206e4b419f26f8f56fa706062ebaba99cd036c06\"; tshl=digital; tst=r; q_c1=46551b22805144768925d1cdb8428160|1561521308000|1535442595000; tgw_l7_route=a37704a413efa26cf3f23813004f1a3b");
        get.setHeader("upgrade-insecure-requests", "1");
        get.setHeader("user-agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");

        //通过httpclient的execute提交 请求 ，并用CloseableHttpResponse接受返回信息
        CloseableHttpResponse response = httpClient.execute(get);
        //服务器返回的状态
        int statusCode = response.getStatusLine().getStatusCode();
        //判断返回的状态码是否是200 ，200 代表服务器响应成功，并成功返回信息
        if (statusCode == HttpStatus.SC_OK) {
            //EntityUtils 获取返回的信息。官方不建议使用使用此类来处理信息
            String jsonStr = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
            System.out.println("Demo.example -------->" + jsonStr);
            return jsonStr;
        } else {
            System.out.println("Demo.example -------->" + "获取信息失败");
            return "";
        }

    }

    public static void saveToFile(String destUrl, String fileName) {
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        HttpURLConnection httpUrl = null;
        URL url = null;
        int BUFFER_SIZE = 1024;
        byte[] buf = new byte[BUFFER_SIZE];
        int size = 0;
        try {
            url = new URL(destUrl);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());
            fos = new FileOutputStream("F:\\zhiwuuser\\" + fileName);
            while ((size = bis.read(buf)) != -1) {
                fos.write(buf, 0, size);
            }
            fos.flush();
        } catch (IOException | ClassCastException ignored) {
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (bis != null) {
                    bis.close();
                }
                if (httpUrl != null) {
                    httpUrl.disconnect();
                }
            } catch (IOException | NullPointerException ignored) {
            }
        }
    }
}
