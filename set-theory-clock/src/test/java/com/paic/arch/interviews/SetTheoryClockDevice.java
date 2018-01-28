package com.paic.arch.interviews;

import static com.paic.arch.interviews.support.BehaviouralTestEmbedder.aBehaviouralTestRunner;
import static org.assertj.core.api.Assertions.assertThat;

import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Test;

import com.paic.arch.interviews.impl.TimeConverterImpl;

/**
 * Acceptance test class that uses the JBehave (Gerkin) syntax for writing stories.  
 * You will notice the TimeConverter has no implementation ... (hint)
 */
public class SetTheoryClockDevice {

    private TimeConverter setTheoryClock = new TimeConverterImpl();
    private String theTime;

    @Test
    public void setTheoryClockAcceptanceTests() throws Exception {
        aBehaviouralTestRunner()
                .usingStepsFrom(this)
                .withStory("set-theory-clock.story")
                .run();
    }

    @When("the time is $time")
    public void whenTheTimeIs(String time) {
        theTime = time;
    }

    @Then("the clock should look like $")
    public void thenTheClockShouldLookLike(String theExpectedClockOutput) {
        assertThat(setTheoryClock.convertTime(theTime)).isEqualTo(theExpectedClockOutput);
    }
}
