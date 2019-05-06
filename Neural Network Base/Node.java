import java.util.ArrayList;
public class Node
{
    private int type;
    /*0 - input
     * 1 - hidden
     * 2 - output
     * 3 - bias
     */
    private int layer;
    /*
     * 0 - input
     * 1 - first hidden layer
     * etc
     */
    private int layerplace;
    /*
     * 0 - first node in layer, aka on the top
     */
    private double value;
    private String NodeCode;
    public ArrayList InputWeights = new ArrayList<Weight>();
    public ArrayList OutputWeights = new ArrayList<Weight>();
    
    int x;
    int y;
    
    double TargetOutput;
    public Node(int type, int layer, int layerplace, double value, int x, int y)
    {
        this.type = type;
        this.layer = layer;
        this.layerplace = layerplace;
        this.value = Equations.round(1000000d, value);
        this.x = x;
        this.y = y;
        if (type == 3) layerplace = -1;
        if ((type == 0) || (type == 3)) InputWeights = null;
        if (type == 2) OutputWeights = null;
        NodeCode = Integer.toString(layer) + "#" + Integer.toString(layerplace);
        
        
        //PrintStats();
        //System.out.println("Node: " + Integer.toString(layer) + "-" + Integer.toString(layerplace));
    }
    public String getNodeCode() {
        return NodeCode;
    }
    public int getType() {
        return type;
    }
    public int getlayer() {
        return layer;
    }
    public int getlayerplace() {
        return layerplace;
    }
    public double getValue() {
        return value;
    }
    public void setValue(double value) {
        this.value = Equations.round(100000d, value);
    }
    public ArrayList getInputWeights() {
        return InputWeights;
    }
    public void PrintStats() {
        System.out.println("Node: " + NodeCode);
        System.out.println("    " + "Value: " + Double.toString(value));
    }
}
