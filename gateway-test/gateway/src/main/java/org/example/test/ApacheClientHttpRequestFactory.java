package org.example.test;

import java.io.IOException;
import java.net.URI;

import com.netflix.client.config.IClientConfig;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.netflix.ribbon.apache.RibbonLoadBalancingHttpClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.NonNull;

/**
 * Apache HttpComponents ClientHttpRequest factory
 *
 * @author bojiangzhou
 */
public class ApacheClientHttpRequestFactory extends HttpComponentsClientHttpRequestFactory {

    private final SpringClientFactory clientFactory;
    private final boolean retryable;

    public ApacheClientHttpRequestFactory(SpringClientFactory clientFactory, boolean retryable) {
        this.clientFactory = clientFactory;
        this.retryable = retryable;
    }

    @Override
    @NonNull
    public ClientHttpRequest createRequest(URI originalUri, HttpMethod httpMethod) throws IOException {
        String serviceId = originalUri.getHost();
        if (serviceId == null) {
            throw new IOException(
                    "Invalid hostname in the URI [" + originalUri.toASCIIString() + "]");
        }
        IClientConfig clientConfig = this.clientFactory.getClientConfig(serviceId);
        RibbonLoadBalancingHttpClient httpClient = this.clientFactory.getClient(serviceId, RibbonLoadBalancingHttpClient.class);

        return new ApacheClientHttpRequest(originalUri, httpMethod, serviceId, httpClient, clientConfig, retryable);
    }
}
