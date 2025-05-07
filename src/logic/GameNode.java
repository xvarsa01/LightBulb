package logic;

import common.AbstractObservable;
import enums.LinkShapes;
import enums.NodeType;
import enums.Side;
import logic.interfaces.IGameNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameNode extends AbstractObservable implements IGameNode {

    public Position position;
    public NodeType nodeType;
    private boolean IsConnectorN = false;
    private boolean IsConnectorS = false;
    private boolean IsConnectorE = false;
    private boolean IsConnectorW = false;
    private boolean IsLighted = false;
    private LinkShapes linkShape = LinkShapes.undefined;
    private int IconRotatedCounter = 0;
    private int UserRotatedCounter = 0;
    private int TotalRotatedCounter = 0;

    public GameNode(){
    }

    public void setSide(Side side){
        switch (side) {
            case NORTH -> {
                IsConnectorN = true;
                IconRotatedCounter = 1;
            }
            case EAST -> {
                IsConnectorE = true;
                IconRotatedCounter = 2;
            }
            case SOUTH -> {
                IsConnectorS = true;
                IconRotatedCounter = 3;
            }
            case WEST -> {
                IsConnectorW = true ;
                IconRotatedCounter = 0;
            }
        }
    }

    public void setMultipleSides(Side... sides){
        for (Side s : sides) {
            setSide(s);
        }

        if (Arrays.stream(sides).count() == 1 && nodeType == NodeType.Power){
            linkShape = LinkShapes.S;
            return;
        }
        if(Arrays.stream(sides).count() == 2){
            if (sides[0] == Side.EAST && sides[1] == Side.WEST || sides[0] == Side.WEST && sides[1] == Side.EAST){
                linkShape = LinkShapes.I;
            }
            else if (sides[0] == Side.SOUTH && sides[1] == Side.NORTH || sides[0] == Side.NORTH && sides[1] == Side.SOUTH){
                linkShape = LinkShapes.I;
                IconRotatedCounter = 1;
            }
            else{
                linkShape = LinkShapes.L;

                List<Side> sidesList = Arrays.asList(sides);
                if (sidesList.contains(Side.EAST) && sidesList.contains(Side.SOUTH)) {
                    IconRotatedCounter = 0; // default
                } else if (sidesList.contains(Side.SOUTH) && sidesList.contains(Side.WEST)) {
                    IconRotatedCounter = 1;
                } else if (sidesList.contains(Side.WEST) && sidesList.contains(Side.NORTH)) {
                    IconRotatedCounter = 2;
                } else if (sidesList.contains(Side.NORTH) && sidesList.contains(Side.EAST)) {
                    IconRotatedCounter = 3;
                }
            }
        }
        else if (Arrays.stream(sides).count() == 3) {
            linkShape = LinkShapes.T;

            // Figure out the missing side
            List<Side> sidesList = Arrays.asList(sides);
            if (!sidesList.contains(Side.NORTH)) {
                IconRotatedCounter = 0;
            } else if (!sidesList.contains(Side.WEST)) {
                IconRotatedCounter = 3;
            } else if (!sidesList.contains(Side.SOUTH)) {
                IconRotatedCounter = 2;
            } else if (!sidesList.contains(Side.EAST)) {
                IconRotatedCounter = 1;
            }
        }
        else if (Arrays.stream(sides).count() == 4) {
            linkShape = LinkShapes.X;
        }
        else{
            throw new IllegalArgumentException();
        }

    }
    public void setOnLighted(boolean turnOn){
        IsLighted = turnOn;
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

    public LinkShapes getLinkShape() {
        return linkShape;
    }
    public int getUserRotatedCounter() {return UserRotatedCounter;}

    public int getActualRotation() {return IconRotatedCounter % 4;}

    /**
     * @return minimal number of turns needed to get node into final state
     */
    public int turnsRemainingToCorrectRotation(){
        int cycleLength = getCycleLength();
        int result = cycleLength - (TotalRotatedCounter % cycleLength);
        return result==cycleLength ? 0 : result;
    }

    public void turn(boolean userRotated){
        boolean temp = IsConnectorN;
        IsConnectorN =  IsConnectorW;
        IsConnectorW = IsConnectorS;
        IsConnectorS = IsConnectorE;
        IsConnectorE = temp;

        if (userRotated){
            UserRotatedCounter++;
        }
        IconRotatedCounter++;
        TotalRotatedCounter++;
        notifyObservers();
    }

    public boolean isLighted(){
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

    /**
     * @return length of the rotation cycle. If node is "+" returns 1, if it is "I" shaped returns 2, other returns 4
     * */
    private int getCycleLength() {
        if (IsConnectorN && IsConnectorE && IsConnectorS && IsConnectorW) {
            return 1;
        }

        return (IsConnectorN && IsConnectorS && !IsConnectorE && !IsConnectorW ||
               !IsConnectorN && !IsConnectorS && IsConnectorE && IsConnectorW)
                ? 2 : 4;
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
