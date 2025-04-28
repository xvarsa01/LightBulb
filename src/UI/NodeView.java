package UI;

import common.Observable;
import logic.interfaces.IGame;
import logic.interfaces.IGameNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

public class NodeView extends JPanel implements Observable.Observer {
    private final IGameNode node;
    private int changedModel = 0;

    public NodeView(final IGameNode _node, final IGame game) {
        this.node = _node;
        this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent var1x) {
                _node.turn();
            }
        });
        game.addObserver(this);
    }

    protected void paintComponent(Graphics var1) {
        super.paintComponent(var1);
        Graphics2D var2 = (Graphics2D)var1;
        Rectangle var3 = this.getBounds();
        double var4 = var3.getWidth();
        double var6 = var3.getHeight();
        double var8 = var4 / (double)2.0F;
        double var10 = var6 / (double)2.0F;
        Color var12 = this.node.light() ? Color.RED : Color.BLACK;
        var2.setColor(var12);
        var2.setStroke(new BasicStroke(2.0F));
        if (this.node.north()) {
            Line2D.Double var13 = new Line2D.Double(var8, (double)0.0F, var8, var10);
            var2.draw(var13);
        }

        if (this.node.east()) {
            Line2D.Double var21 = new Line2D.Double(var4, var10, var8, var10);
            var2.draw(var21);
        }

        if (this.node.south()) {
            Line2D.Double var22 = new Line2D.Double(var8, var6, var8, var10);
            var2.draw(var22);
        }

        if (this.node.west()) {
            Line2D.Double var23 = new Line2D.Double((double)0.0F, var10, var8, var10);
            var2.draw(var23);
        }

        if (this.node.isPower()) {
            this.setBackground(Color.GREEN);
        } else if (this.node.isBulb()) {
            double var14 = Math.min(var6, var4) - (double)10.0F;
            double var16 = (var4 - var14) / (double)2.0F;
            double var18 = (var6 - var14) / (double)2.0F;
            Ellipse2D.Double var20 = new Ellipse2D.Double(var16, var18, var14, var14);
            var2.setColor(var12);
            var2.fill(var20);
        }

    }

    public int numberUpdates() {
        return this.changedModel;
    }

    public void clearChanged() {
    }

    // called from Game class at the end of relightBoard()
    public void update(Observable var1) {
        ++this.changedModel;
        this.repaint();
    }
}