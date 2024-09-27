//package br.com.fiap.postech.adjt.checkout.config;
//
//import java.security.SecureRandom;
//import java.security.cert.X509Certificate;
//import javax.net.ssl.HostnameVerifier;
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.SSLSocketFactory;
//import javax.net.ssl.TrustManager;
//import javax.net.ssl.X509TrustManager;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import feign.Client;
//import feign.Logger;
//
//
//@Configuration
//public class FeignConfig {
//
//    // Classe interna para configurar o SSL
//    static class SSLSocketClient {
//        public static SSLSocketFactory getSSLSocketFactory() {
//            try {
//                SSLContext sslContext = SSLContext.getInstance("SSL");
//                sslContext.init(null, getTrustManagers(), new SecureRandom());
//                return sslContext.getSocketFactory();
//            } catch (Exception e) {
//                throw new RuntimeException("Failed to create SSL Socket Factory", e);
//            }
//        }
//
//        private static TrustManager[] getTrustManagers() {
//            return new TrustManager[]{new X509TrustManager() {
//                @Override
//                public X509Certificate[] getAcceptedIssuers() {
//                    return new X509Certificate[0];
//                }
//
//                @Override
//                public void checkClientTrusted(X509Certificate[] certs, String authType) {
//                    // No-op: Trust all clients
//                }
//
//                @Override
//                public void checkServerTrusted(X509Certificate[] certs, String authType) {
//                    // No-op: Trust all servers
//                }
//            }};
//        }
//
//        public static HostnameVerifier getHostnameVerifier() {
//            return (hostname, session) -> true; // Trust all hostnames
//        }
//    }
//
//    // Configura o cliente Feign para usar SSL
//    @Bean
//    public Client feignClient() {
//        return new OkHttpClient(new okhttp3.OkHttpClient.Builder()
//                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), (X509TrustManager) SSLSocketClient.getTrustManagers()[0])
//                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
//                .build());
//    }
//
//    // Configura o nível de log do Feign
//    @Bean
//    Logger.Level feignLoggerLevel() {
//        return Logger.Level.FULL; // Ajuste o nível de log conforme necessário
//    }
//}