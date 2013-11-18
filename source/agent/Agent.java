package agent;

import java.util.concurrent.*;

import test.utilities.EventLog;
import test.utilities.LoggedEvent;
import utilities.StringUtil;

/**
 * Base class for simple agents
 */
public abstract class Agent
{
    private Semaphore sleepSemaphore = new Semaphore(1, true);//binary semaphore, fair
    private AgentThread agentThread; // initialized in the startThread method
    
    // For pausing agents:
    private Semaphore pauseSemaphore = new Semaphore(0, true);
    private boolean pauseBoolean = false;
    
    // TEST
    public EventLog log = new EventLog();

    protected Agent() { }

    /**
     * Wakes up the scheduler.
     * This should be called whenever state has changed that might cause the agent to do something.
     */
    public void stateChanged()
    {
        sleepSemaphore.release();
    }
    
    public void pause()
    {
    	pauseBoolean = true;
    	print("[paused]");
    }
    
    public void resume()
    {
    	pauseBoolean = false;
    	pauseSemaphore.release();
    	print("[resumed]");
    }

    /**
     * This is the scheduler.
     * Agents must implement this scheduler to perform any actions appropriate for the
     * current state.  Will be called whenever a state change has occurred,
     * and will be called repeated as long as it returns true.
     *
     * @return true if some action was executed that might have changed the state or might require more actions to be executedl
     */
    protected abstract boolean pickAndExecuteAnAction();

    /**
     * Return agent name for console messages.  Default is to return java instance name.
     */
    protected String getName()
    {
        return StringUtil.shortName(this);
    }

    /**
     * The simulated action code; used when there is not an implemented animation for the action.
     */
    protected void Do(String msg)
    {
        print(msg, null);
    }

    /**
     * Print message
     */
    protected void print(String msg) {
        print(msg, null);
    }
    
    // TEST
    protected void logThis(String msg) {
    	log.add(new LoggedEvent(msg));
    }

    /**
     * Print message with this agent's name and exception stack trace
     */
    protected void print(String msg, Throwable e)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(getName());
        sb.append(": ");
        sb.append(msg);
        sb.append("\n");
        if (e != null) sb.append(StringUtil.stackTraceString(e));
        System.out.print(sb.toString());
    }

    /**
     * Start agent scheduler thread.  Should be called once at init time.
     */
    public synchronized void startThread()
    {
        if (agentThread == null)
        {
            agentThread = new AgentThread(getName());
            agentThread.start(); // causes the run method to execute in the AgentThread below
        }
        else
        {
            agentThread.interrupt(); //don't worry about this for now
        }
    }

    /**
     * Stop agent scheduler thread.
     */
    //In this implementation, nothing calls stopThread().
    //When we have a user interface to agents, this can be called.
    public void stopThread()
    {
        if (agentThread != null)
        {
            agentThread.stopAgent();
            agentThread = null;
        }
    }

    /**
     * Agent scheduler thread, calls respondToStateChange() whenever a state
     * change has been signaled.
     */
    private class AgentThread extends Thread
    {
        private volatile boolean goOn = false;

        private AgentThread(String name)
        {
            super(name);
        }

        /**
         * The main method for the agent's scheduler-action thread.
         */
        public void run()
        {
            goOn = true;
            print("alive");

            while (goOn)
            {
                try
                {
                	// Freeze here if pausing
                	if(pauseBoolean) pauseSemaphore.acquire();
                    // The agent sleeps here until someone calls stateChanged(), which causes a call to stateChange.give(), which wakes up agent.
                    sleepSemaphore.acquire();
                    // print("[woke up]"); // for debug tracking
                    // The next while clause is the key to the control flow.  When the agent wakes up it will call the scheduler repeatedly until the scheduler returns false.
                    while (pickAndExecuteAnAction());
                }
                catch (InterruptedException e)
                {
                    // no action - expected when stopping or when deadline changed
                }
                catch (Exception e)
                {
                    print("Unexpected exception caught in Agent thread:", e);
                }
            }
        }

        private void stopAgent()
        {
            goOn = false;
            this.interrupt();
        }
    }
}