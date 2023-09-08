package org.pepper8081.logandtrace.benchmarks;

import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;
import org.pepper8081.logandtrace.service.INoProxiedTestService;
import org.pepper8081.logandtrace.service.IProxiedTestService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.pepper8081.logandtrace.model.AnyObject;
import org.pepper8081.logandtrace.model.AnyRequest;
import org.pepper8081.logandtrace.model.MethodType;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.AverageTime, Mode.Throughput})
@Warmup(time = 5, iterations = 1, batchSize = 1)
@Measurement(time = 5, iterations = 1, batchSize = 1)
@OutputTimeUnit(TimeUnit.SECONDS)
@Fork(value = 1)
@Timeout(time = 10)
@Threads(Threads.MAX)
@SpringBootApplication
public class UseProxyBenchmarks {

    public static void main(String[] args) throws IOException {
        Main.main(args);
    }

    @State(Scope.Benchmark)
    public static class Context {

        ConfigurableApplicationContext context;
        IProxiedTestService proxiedTestService;
        INoProxiedTestService noProxiedTestService;

        AnyRequest data = new AnyRequest(
                UUID.randomUUID().toString(),
                Instant.now(),
                MethodType.CREATE_ANY,
                List.of(
                        new AnyObject(UUID.randomUUID(), new AnyObject.AnyData("hello", 1L)),
                        new AnyObject(UUID.randomUUID(), new AnyObject.AnyData("world", 2L)))
        );

        @Setup(Level.Trial)
        public synchronized void setup() {
            try {
                if (context == null) {
                    SpringApplication springApp = new SpringApplication(UseProxyBenchmarks.class);
                    springApp.setAdditionalProfiles("benchmark");
                    context = springApp.run();
                }
                proxiedTestService = context.getBean(IProxiedTestService.class);
                noProxiedTestService = context.getBean(INoProxiedTestService.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Benchmark
    public void withProxy(Context c) {
        c.proxiedTestService.execute(c.data);
    }

    @Benchmark
    public void withoutProxy_SUCCESS(Context c) {
        c.noProxiedTestService.execute(c.data);
    }

    @Benchmark
    public void withProxyError_NPE(Context c) {
        try {
            c.proxiedTestService.execute(null);
        } catch (Throwable ignore) {
        }
    }

    @Benchmark
    public void withoutProxyError_NPE(Context c) {
        try {
            c.noProxiedTestService.execute(null);
        } catch (Throwable ignore) {
        }
    }

}

