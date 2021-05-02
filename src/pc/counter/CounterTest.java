package pc.counter;

import static org.junit.Assert.*;

import org.cooperari.CSystem;
import org.cooperari.config.CRaceDetection;
import org.cooperari.config.CMaxTrials;
import org.cooperari.config.CScheduling;
import org.cooperari.junit.CJUnitRunner;
import org.cooperari.core.scheduling.CProgramStateFactory;
import org.cooperari.core.scheduling.CSchedulerFactory;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(CJUnitRunner.class)
@CMaxTrials(25)
@CRaceDetection(true)
@CScheduling(schedulerFactory=CSchedulerFactory.OBLITUS, stateFactory=CProgramStateFactory.RAW)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class CounterTest {

  public abstract Counter createCounter(int initial);

  @Test
  public void test() {
    Counter c = createCounter(0);
    int initial = c.value();
    CSystem.forkAndJoin(
      () -> c.increment(), 
      () -> c.increment()
    );
    assertEquals(2, c.value());
  }

}
