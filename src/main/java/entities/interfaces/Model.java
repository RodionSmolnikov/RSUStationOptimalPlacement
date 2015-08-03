package entities.interfaces;


/**
 * Created by Rodion on 06.05.2015.
 */
public abstract class Model implements Runnable {
    ResultCollector collector;

    public abstract int getThreadNumber();

    public ResultCollector getCollector() {return this.collector;};

    public void setCollector(ResultCollector collector) {this.collector = collector;};

    public abstract void init ();

    public void log(String message) {
        getCollector().pushFlashMessage("thread-" + getThreadNumber() + ": " + message);
    }

}
