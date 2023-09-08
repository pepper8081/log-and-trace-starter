package org.pepper8081.logandtrace;

import lombok.SneakyThrows;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.pepper8081.logandtrace.config.TestConfig;
import org.pepper8081.logandtrace.model.AnyObject;
import org.pepper8081.logandtrace.model.AnyRequest;
import org.pepper8081.logandtrace.model.MethodType;
import org.pepper8081.logandtrace.service.IProxiedTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = TestConfig.class)
public class LoggingTests {

    @Autowired
    IProxiedTestService testService;

    static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    static final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    static final PrintStream originalOut = System.out;
    static final PrintStream originalErr = System.err;

    final Set<String> logKeys = Set.of(
            "timestamp", "level",
            "thread", "logger",
            "message", "event",
            "spanId", "traceId",
            "outputValue", "inputParams",
            "logType"
    );

    static void switchOutput() {
        System.setOut(
                new PrintStream(new OutputStream() {
                    @Override
                    public void write(int b) {
                        outContent.write(b);
                        originalOut.write(b);
                    }
                })
        );
        System.setErr(
                new PrintStream(new OutputStream() {
                    @Override
                    public void write(int b) {
                        errContent.write(b);
                        originalErr.write(b);
                    }
                })
        );
    }

    static void restoreOutput() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @BeforeAll
    static void beforeAll() {
        switchOutput();
    }

    @AfterAll
    @SneakyThrows
    static void afterAll() {
        restoreOutput();
        outContent.close();
        errContent.close();
    }

    @SuppressWarnings("CallToPrintStackTrace")
    @Test
    @SneakyThrows
    void testLog_containsAllKeys_Success() {
        AnyRequest anyRequest1 = new AnyRequest(
                UUID.randomUUID().toString(),
                Instant.now(),
                MethodType.CREATE_ANY,
                List.of(
                        new AnyObject(UUID.randomUUID(), new AnyObject.AnyData("hello1", 1L)),
                        new AnyObject(UUID.randomUUID(), new AnyObject.AnyData("world1", 2L)))
        );
        AnyRequest anyRequest2 = new AnyRequest(
                UUID.randomUUID().toString(),
                Instant.now(),
                MethodType.CREATE_ANY,
                List.of(
                        new AnyObject(UUID.randomUUID(), new AnyObject.AnyData("hello2", 1L)),
                        new AnyObject(UUID.randomUUID(), new AnyObject.AnyData("world2", 2L)))
        );

        try {
            testService.execute(anyRequest1, anyRequest2);
            testService.execute(anyRequest1);
            testService.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Awaitility.await()
                .timeout(2, TimeUnit.SECONDS)
                .pollInterval(100, TimeUnit.MILLISECONDS)
                .untilAsserted(() -> {
                    String fullLog = outContent.toString(StandardCharsets.UTF_8);
                    Assertions.assertTrue(fullLog != null && !fullLog.isEmpty() && !fullLog.isBlank());
                    List<String> logList = Arrays.stream(fullLog.split("(\n)|(\r\n)")).toList();

                    Predicate<String> servicesLogs = p -> p.matches(".*\"logger\":\"org\\.pepper8081\\.logandtrace.*?TestService\".*");
                    Assertions.assertTrue(
                            logList.stream()
                                    .anyMatch(servicesLogs), "Test services do not log anything");
                    logKeys.stream().sorted().forEach(c -> {
                                List<String> s = logList.stream().filter(servicesLogs).toList();
                                Assertions.assertTrue(
                                        s.stream().allMatch(a -> a.contains("\"" + c + "\":")),
                                        "Key '" + c + "' is not presented in log");
                                Assertions.assertTrue(
                                        s.stream()
                                                .allMatch(a -> a.matches(".*\"" + c + "\":\".*?\".*")
                                                        || c.equals("inputParams") && a.matches(".*\"message\":\".*?void.*")
                                                        || c.equals("outputValue") && a.matches(".*\"message\":\".*?void.*")
                                                        || c.equals("outputValue") && a.matches(".*\"logType\":\"begin\".*")
                                                ),
                                        "Value of key '" + c + "' is not presented in log");
                            }
                    );
                });
    }

}
