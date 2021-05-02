package pc.stack;

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

import java.util.concurrent.atomic.AtomicInteger;

@RunWith(CJUnitRunner.class)
@CMaxTrials(25)
@CRaceDetection(true)
@CScheduling(schedulerFactory=CSchedulerFactory.OBLITUS, stateFactory=CProgramStateFactory.RAW)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class StackTest {

  Stack<Integer> s; 

  public abstract Stack<Integer> createStack(); 

  @Before
  public void init() {
     s = createStack();
  }

  @Test @CMaxTrials(1)
  public void testSeq() {
    s.push(1);
    s.push(2);
    s.push(3);
    int x = s.pop();
    int y = s.pop();
    int z = s.pop();
    assertTrue(x == 3);
    assertTrue(y == 2);
    assertTrue(z == 1);
  }

  @Test 
  public void test1() {
    CSystem.forkAndJoin(
      () -> s.push(1), 
      () -> s.push(2)
    );
    s.push(3);
    assertTrue(s.size() == 3);
    int x = s.pop();
    int y = s.pop();
    int z = s.pop();
    assertTrue(x == 3);
    assertTrue( (y == 1 && z == 2) || (y == 2 && z == 1));
    assertTrue(s.size() == 0);
  }

  @Test 
  public void test2() {
    CSystem.forkAndJoin(
      () -> s.push(1), 
      () -> s.push(2)
    );

    assertTrue(s.size() == 2);

    AtomicInteger rx = new AtomicInteger();
    AtomicInteger ry = new AtomicInteger();

    CSystem.forkAndJoin(
      () -> rx.set(s.pop()), 
      () -> ry.set(s.pop()) 
    );
    
    int x = rx.get();
    int y = ry.get();
    assertTrue( (x == 1 && y == 2) || (x == 2 && y == 1));
    assertTrue(s.size() == 0);
  }
}
