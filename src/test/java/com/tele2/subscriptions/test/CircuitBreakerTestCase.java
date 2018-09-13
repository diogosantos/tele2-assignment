package com.tele2.subscriptions.test;

import com.netflix.config.ConfigurationManager;
import com.netflix.hystrix.Hystrix;
import com.netflix.hystrix.HystrixCircuitBreaker;
import com.netflix.hystrix.HystrixCommandKey;
import com.tele2.subscriptions.command.logging.CircuitBreakerLoggingMessages;
import com.tele2.subscriptions.repository.SubscriptionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class CircuitBreakerTestCase {

    @MockBean
    protected CircuitBreakerLoggingMessages messages;

    @MockBean
    protected SubscriptionRepository subscriptionRepository;

    @Test
    public void testCircuitBreakerOpen() throws InterruptedException {
        openCircuitBreak();
        onOpenCircuit();
    }

    @Before
    public void before() {
        resetHystrix();
        openCircuitBreakerAfterOneFailingRequest();
    }

    protected abstract void onOpenCircuit();

    protected abstract void openCircuitBreakWith();

    protected abstract String getCommandKey();

    protected void openCircuitBreak() throws InterruptedException {
        try {
            openCircuitBreakWith();
        } catch (RuntimeException e) {
            waitUntilCircuitBreakerOpens();
            HystrixCircuitBreaker circuitBreaker = getCircuitBreaker(getCommandKey());
            assertThat(circuitBreaker.allowRequest(), is(false));
        }
    }


    private static HystrixCircuitBreaker getCircuitBreaker(final String commandKey) {
        return HystrixCircuitBreaker.Factory.getInstance(HystrixCommandKey.Factory.asKey(commandKey));
    }

    private void waitUntilCircuitBreakerOpens() throws InterruptedException {
        Thread.sleep(1000);
    }

    private void resetHystrix() {
        Hystrix.reset();
    }

    private void openCircuitBreakerAfterOneFailingRequest() {
        ConfigurationManager.getConfigInstance().
                setProperty("hystrix.command.findOneById.circuitBreaker.requestVolumeThreshold", 1);
    }

}
