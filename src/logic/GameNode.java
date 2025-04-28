package logic;

import common.AbstractObservable;
import enums.NodeType;
import enums.Side;
import logic.interfaces.IGameNode;

import java.util.ArrayList;
import java.util.List;

public class GameNode extends AbstractObservable implements IGameNode {

    public Position position;
    public NodeType nodeType;
    private boolean IsConnectorN = false;
    private boolean IsConnectorS = false;
    private boolean IsConnectorE = false;
    private boolean IsConnectorW = false;
    private boolean IsLighted = false;

    public GameNode(){
    }

    public void setSide(Side side){
        switch (side) {
            case EAST -> IsConnectorE = true;
            case WEST -> IsConnectorW = true;
            case NORTH -> IsConnectorN = true;
            case SOUTH -> IsConnectorS = true;
        };
    }

    public void setMultipleSides(Side... sides){
        for (Side s : sides) {
            setSide(s);
        }
    }
    public void setOnLighted(boolean turnOn){
        IsLighted = turnOn;
    }

    public boolean containsConnector(Side s) {
        return switch (s) {
            case EAST -> IsConnectorE;
            case WEST -> IsConnectorW;
            case NORTH -> IsConnectorN;
            case SOUTH -> IsConnectorS;
        };
    }

    public Position getPosition(){
        return position;
    }

    public boolean isBulb(){
        return nodeType==NodeType.Bulb;
    }

    public boolean isLink(){
        return nodeType==NodeType.Link;
    }

    public boolean isPower(){
        return nodeType==NodeType.Power;
    }

    public boolean north(){ return IsConnectorN;}

    public boolean east(){ return IsConnectorE; }

    public boolean south(){ return IsConnectorS; }

    public boolean west(){ return IsConnectorW; }

    public void turn(){
        boolean temp = IsConnectorN;
        IsConnectorN =  IsConnectorW;
        IsConnectorW = IsConnectorS;
        IsConnectorS = IsConnectorE;
        IsConnectorE = temp;

        notifyObservers();
    }

    public boolean light(){
        return IsLighted;
    }

    public String toString(){
        String str = "{";
        switch (nodeType) {
            case Empty -> str += "E";
            case Link -> str += "L";
            case Power -> str += "P";
            case Bulb -> str += "B";
        }
        str += "[" + position.getRow() + "@" + position.getCol() + "][";
        List<String> connectors = new ArrayList<>();
        if (north()) connectors.add("NORTH");
        if (east()) connectors.add("EAST");
        if (south()) connectors.add("SOUTH");
        if (west()) connectors.add("WEST");

        str += String.join(",", connectors);
        str += "]}";
        return str;
    }

    @Override
    public boolean equals(Object o) {
        // Check if the objects are the same
        if (this == o) return true;

        // Check if the object is an instance of GameNode
        if (o == null || getClass() != o.getClass()) return false;

        // Cast the object to a GameNode
        GameNode that = (GameNode) o;

        // Compare the relevant fields for equality
        return position.equals(that.position);
    }
}
