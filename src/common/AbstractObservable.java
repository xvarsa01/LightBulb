/**
 * Authors: xvarsa01, xhavli59
 * Date: 09.05.2025
 *
 * Description: Base implementation of the Observable interface supporting observer registration.
 */

package common;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractObservable implements Observable {
    private final Set<Observable.Observer> observers = new HashSet<>();

    public AbstractObservable() {
    }

    public void addObserver(Observable.Observer var1) {
        this.observers.add(var1);
    }

    public void removeObserver(Observable.Observer var1) {
        this.observers.remove(var1);
    }

    public void notifyObservers() {
        this.observers.forEach((var1) -> var1.update(this));
    }
}