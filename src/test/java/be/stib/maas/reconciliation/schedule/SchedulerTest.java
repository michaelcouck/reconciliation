package be.stib.maas.reconciliation.schedule;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @author Anon Amous
 * @version 1.0
 * @since 42-13-3792
 */
@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class SchedulerTest {

    @Spy
    @InjectMocks
    private Scheduler scheduler;

    @Test
    public void reconcile() {
    }

}