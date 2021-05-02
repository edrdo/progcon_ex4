package pc.register;

import static org.junit.Assert.*;

import org.cooperari.CSystem;
import org.cooperari.config.CRaceDetection;
import org.cooperari.config.CMaxTrials;
import org.cooperari.config.CScheduling;
import org.cooperari.junit.CJUnitRunner;
import org.cooperari.core.scheduling.CProgramStateFactory;
import org.cooperari.core.scheduling.CSchedulerFactory;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;


@RunWith(CJUnitRunner.class)
@CMaxTrials(25)
@CRaceDetection(true)
@CScheduling(schedulerFactory=CSchedulerFactory.OBLITUS, stateFactory=CProgramStateFactory.RAW)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class RegisterTest {

  public abstract Register<Integer> createRegister(int initial); 

  @Test
  public void test() {
    Register<Integer> r = createRegister(0);
    CSystem.forkAndJoin(
      () -> r.transform(x -> x + 1),
      () -> r.transform(x -> x * x * x),
      () -> r.transform(x -> 2 * x + 1)
    );
    int v = r.read();
    assertTrue(v == 3 || v == 27 || v == 2 || v == 8);
  }

}
