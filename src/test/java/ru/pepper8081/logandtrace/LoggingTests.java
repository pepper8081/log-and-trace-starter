package ru.pepper8081.logandtrace;

import lombok.SneakyThrows;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ru.pepper8081.logandtrace.config.TestConfig;
import ru.pepper8081.logandtrace.model.AnyMessage;
import ru.pepper8081.logandtrace.service.TestService;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

@SuppressWarnings("CallToPrintStackTrace")
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = TestConfig.class)
public class LoggingTests {

    @Autowired
    @Qualifier("proxiedTestService")
    TestService testService;

    static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    static final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    static final PrintStream originalOut = System.out;
    static final PrintStream originalErr = System.err;

    static AnyMessage newAnyMessage() {
        return new AnyMessage(
                UUID.randomUUID().toString(),
                Instant.now(), String.valueOf(new Random().nextInt(1000)));
    }

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

    @Test
    @SneakyThrows
    void testLogAroundBlock_containsAllKeys_Success() {

        try {
            testService.execute();
            testService.execute(newAnyMessage());
            testService.execute(newAnyMessage(), newAnyMessage());
            testService.executeWithSeparateThread(newAnyMessage());
//            testService.execute(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertLog();
    }

    private void assertLog() {
        Awaitility.await()
                .timeout(1, TimeUnit.SECONDS)
                .pollInterval(100, TimeUnit.MILLISECONDS)
                .untilAsserted(() -> {
                    String fullLog = outContent.toString(StandardCharsets.UTF_8);
                    Assertions.assertTrue(fullLog != null && !fullLog.isEmpty() && !fullLog.isBlank());
                    List<String> logList = Arrays.stream(fullLog.split("(\n)|(\r\n)")).toList();

                    Predicate<String> servicesLogs = p -> p.matches(".*\"logger\":\"ru\\.pepper8081\\.logandtrace.*?TestService\".*");
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
