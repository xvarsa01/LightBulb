/**
 * Authors: xvarsa01, xhavli59
 * Date: 09.05.2025
 *
 * Description: Defines an interface for observable objects used in the observer pattern.
 */

package common;

public interface Observable {
    void addObserver(Observer var1);

    void removeObserver(Observer var1);

    void notifyObservers();

    public interface Observer {
        void update(Observable var1);
    }
}

