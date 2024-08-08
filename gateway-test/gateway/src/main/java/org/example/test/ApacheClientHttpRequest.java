package org.example.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

import com.netflix.client.config.IClientConfig;
import com.netflix.client.http.HttpResponse;
import org.springframework.cloud.netflix.ribbon.RibbonHttpRequest;
import org.springframework.cloud.netflix.ribbon.RibbonHttpResponse;
import org.springframework.cloud.netflix.ribbon.apache.RetryableRibbonLoadBalancingHttpClient;
import org.springframework.cloud.netflix.ribbon.apache.RibbonApacheHttpRequest;
import org.springframework.cloud.netflix.ribbon.apache.RibbonLoadBalancingHttpClient;
import org.springframework.cloud.netflix.ribbon.support.RibbonCommandContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.LinkedMultiValueMap;

/**
 * Apache ClientHttpRequest
 *
 * @author bojiangzhou
 */
public class ApacheClientHttpRequest extends RibbonHttpRequest {

    private final URI uri;

    private final HttpMethod httpMethod;

    private final String serviceId;

    private final RibbonLoadBalancingHttpClient client;

    private final IClientConfig config;
    /**
     * 是否重试
     */
    private final boolean retryable;

    public ApacheClientHttpRequest(URI uri,
                                   HttpMethod httpMethod,
                                   String serviceId,
                                   RibbonLoadBalancingHttpClient client,
                                   IClientConfig config,
                                   boolean retryable) {
        super(uri, null, null, config);
        this.uri = uri;
        this.httpMethod = httpMethod;
        this.serviceId = serviceId;
        this.client = client;
        this.config = config;
        this.retryable = retryable;
        if (retryable && !(client instanceof RetryableRibbonLoadBalancingHttpClient)) {
            throw new IllegalArgumentException("Retryable client must be RetryableRibbonLoadBalancingHttpClient");
        }
    }

    @Override
    protected ClientHttpResponse executeInternal(HttpHeaders headers) throws IOException {
        try {
            RibbonApacheHttpRequest request = new RibbonApacheHttpRequest(buildCommandContext(headers));

            HttpResponse response;
            if (retryable) {
                // RetryableRibbonLoadBalancingHttpClient 使用 RetryTemplate 做负载均衡和重试
                response = client.execute(request, config);
            } else {
                // RibbonLoadBalancingHttpClient 需调用 executeWithLoadBalancer 才具备负载均衡的能力
                response = client.executeWithLoadBalancer(request, config);
            }

            return new RibbonHttpResponse(response);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    protected RibbonCommandContext buildCommandContext(HttpHeaders headers) throws IOException {
        ByteArrayInputStream requestEntity = null;
        ByteArrayOutputStream bufferedOutput = (ByteArrayOutputStream) this.getBodyInternal(headers);
        if (bufferedOutput != null) {
            requestEntity = new ByteArrayInputStream(bufferedOutput.toByteArray());
            bufferedOutput.close();
        }

        return new RibbonCommandContext(serviceId, httpMethod.name(), uri.toString(), retryable,
                headers, new LinkedMultiValueMap<>(), requestEntity, new ArrayList<>());
    }
}
