package ru.pepper8081.logging.benchmarks;

import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.pepper8081.logging.model.AnyRequest;
import ru.pepper8081.logging.service.TestService;

import java.io.IOException;
import java.time.Instant;
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
        TestService proxiedTestService;
        TestService noProxiedTestService;

        AnyRequest data = new AnyRequest(
                UUID.randomUUID().toString(),
                Instant.now());

        @Setup(Level.Trial)
        public synchronized void setup() {
            try {
                if (context == null) {
                    SpringApplication springApp = new SpringApplication(UseProxyBenchmarks.class);
                    springApp.setAdditionalProfiles("benchmark");
                    context = springApp.run();
                }
                proxiedTestService = context.getBean("proxiedTestService", TestService.class);
                noProxiedTestService = context.getBean("noProxiedTestService", TestService.class);
            } catch (Exception ignored) {
            }
        }
    }

    @Benchmark
    public void withProxy_Success(Context c) {
        c.proxiedTestService.execute(c.data);
    }

    @Benchmark
    public void withoutProxy_Success(Context c) {
        c.noProxiedTestService.execute(c.data);
    }

    @Benchmark
    public void withProxy_Error_byNPE(Context c) {
        try {
            c.proxiedTestService.execute(null);
        } catch (Throwable ignore) {
        }
    }

    @Benchmark
    public void withoutProxy_Error_byNPE(Context c) {
        try {
            c.noProxiedTestService.execute(null);
        } catch (Throwable ignore) {
        }
    }

}

