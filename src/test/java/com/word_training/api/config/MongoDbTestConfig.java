package com.word_training.api.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.word_training.api.config.converters.DateToOffsetDateTimeConverter;
import com.word_training.api.config.converters.OffsetDateTimeToDateConverter;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.transitions.Mongod;
import de.flapdoodle.embed.mongo.transitions.RunningMongodProcess;
import de.flapdoodle.embed.process.io.directories.PersistentDir;
import de.flapdoodle.embed.process.store.DownloadCache;
import de.flapdoodle.embed.process.store.LocalDownloadCache;
import de.flapdoodle.reverse.TransitionWalker;
import de.flapdoodle.reverse.transitions.Start;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import static de.flapdoodle.net.Net.freeServerPort;
import static java.util.Arrays.asList;

@TestConfiguration
public class MongoDbTestConfig implements InitializingBean, DisposableBean {

    private static final String CONNECTION_STRING = "mongodb://127.0.0.1:%d";
    public static String uri;
    public static int port;

    private TransitionWalker.ReachedState<RunningMongodProcess> runningMongodProcess;

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(
                MongoClientSettings.builder()
                        .applyToSocketSettings(builder -> builder.connectTimeout(1000, TimeUnit.MILLISECONDS))
                        .applyConnectionString(new ConnectionString(uri))
                        .build()
        );
    }

    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate() {
        var reactiveMongoTemplate = new ReactiveMongoTemplate(MongoClients.create(uri), "test");
        var converter = (MappingMongoConverter) reactiveMongoTemplate.getConverter();
        converter.setCustomConversions(new MongoCustomConversions(asList(new OffsetDateTimeToDateConverter(), new DateToOffsetDateTimeConverter())));
        converter.afterPropertiesSet();
        return reactiveMongoTemplate;
    }

    @Override
    public void destroy() {
        runningMongodProcess.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        port = freeServerPort();
        uri = String.format(CONNECTION_STRING, port);

        var localHostIsIPv6 = de.flapdoodle.net.Net.localhostIsIPv6();
        var localPath = Path.of(System.getProperty("user.dir"), "/build/resources/test/mongodb/embedded");
        var dir = PersistentDir.of(localPath);
        var downloadCache = new LocalDownloadCache(localPath);

        var mongod = Mongod.builder()
                .downloadCache(Start.to(DownloadCache.class).initializedWith(downloadCache))
                .net(Start.to(Net.class).initializedWith(Net.of("127.0.0.1", port, localHostIsIPv6)))
                .persistentBaseDir(Start.to(PersistentDir.class).providedBy(() -> dir))
                .build();

        runningMongodProcess = mongod.start(Version.Main.V6_0);
    }
}
