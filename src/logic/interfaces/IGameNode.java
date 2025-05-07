package logic.interfaces;

import common.Observable;

public interface IGameNode extends Observable {
    void turn();
    boolean isLighted();

    boolean north();
    boolean east();
    boolean south();
    boolean west();

    boolean isLink();
    boolean isBulb();
    boolean isPower();
}
