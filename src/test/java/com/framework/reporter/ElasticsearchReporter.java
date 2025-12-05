package com.framework.reporter;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.framework.config.ConfigurationManager;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ElasticsearchReporter {
    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchReporter.class);
    private static ElasticsearchReporter instance;
    private final ElasticsearchClient client;
    private final ConfigurationManager config;
    private final String indexName;

    private ElasticsearchReporter() {
        config = ConfigurationManager.getInstance();
        indexName = config.getElasticsearchIndex();

        if (config.isElasticsearchEnabled()) {
            client = createElasticsearchClient();
            logger.info("Elasticsearch reporter initialized with index: {}", indexName);
        } else {
            client = null;
            logger.info("Elasticsearch reporting is disabled");
        }
    }

    public static synchronized ElasticsearchReporter getInstance() {
        if (instance == null) {
            instance = new ElasticsearchReporter();
        }
        return instance;
    }

    private ElasticsearchClient createElasticsearchClient() {
        try {
            String esUrl = config.getElasticsearchUrl();
            HttpHost host = HttpHost.create(esUrl);

            RestClientBuilder builder = RestClient.builder(host);

            String username = config.getElasticsearchUsername();
            String password = config.getElasticsearchPassword();

            if (username != null && !username.isEmpty()) {
                BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider.setCredentials(
                        AuthScope.ANY,
                        new UsernamePasswordCredentials(username, password)
                );
                builder.setHttpClientConfigCallback(httpClientBuilder ->
                        httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
            }

            RestClient restClient = builder.build();

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            ElasticsearchTransport transport = new RestClientTransport(
                    restClient,
                    new JacksonJsonpMapper(objectMapper)
            );

            return new ElasticsearchClient(transport);
        } catch (Exception e) {
            logger.error("Failed to create Elasticsearch client", e);
            throw new RuntimeException("Elasticsearch client initialization failed", e);
        }
    }

    public void indexTestResult(TestResult testResult) {
        if (!config.isElasticsearchEnabled() || client == null) {
            logger.debug("Elasticsearch reporting is disabled, skipping result indexing");
            return;
        }

        try {
            IndexRequest<TestResult> request = IndexRequest.of(i -> i
                    .index(indexName)
                    .document(testResult)
            );

            client.index(request);
            logger.info("Test result indexed successfully: {}", testResult.getTestName());
        } catch (IOException e) {
            logger.error("Failed to index test result: {}", testResult.getTestName(), e);
        }
    }

    public void close() {
        if (client != null) {
            try {
                client._transport().close();
                logger.info("Elasticsearch client closed");
            } catch (IOException e) {
                logger.error("Error closing Elasticsearch client", e);
            }
        }
    }
}
