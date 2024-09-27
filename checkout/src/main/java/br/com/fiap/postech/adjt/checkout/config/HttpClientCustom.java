package br.com.fiap.postech.adjt.checkout.config;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.BasicHttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayInputStream;
@Component

public class HttpClientCustom {

    public ResponseEntity<Object> handler(String url, HttpMethod method, Object obj) {
        HttpUriRequestBase httpGet = new HttpAnyWithEntity(url, method.name());
        ObjectMapper objMap = new ObjectMapper().registerModule(new JavaTimeModule());
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            ByteArrayInputStream stream = new ByteArrayInputStream(objMap.writeValueAsBytes(obj));
            HttpEntity entityReq = new BasicHttpEntity(stream, ContentType.APPLICATION_JSON);
            httpGet.setEntity(entityReq);
            try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
                HttpEntity entity = response.getEntity();
                String resultContent = EntityUtils.toString(entity);
                return ResponseEntity.status(response.getCode()).body(objMap.readValue(resultContent, Object.class));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


    public static class HttpAnyWithEntity extends HttpPost {
        private final String methodName;
        public HttpAnyWithEntity(String url, String methodName) {
            super(url);
            this.methodName = methodName;
        }
        @Override
        public String getMethod() {
            return this.methodName;
        }
    }

}
