public class Weight
{
    private Node Target;
    private Node Parent;
    private int type;
    // 0 - regular
    // 1 - bias
    private int layer; // 1 = between nodes 1 and 2, so it shares the layer of it's parent
    private int layerplace; 
    private String WeightCode;
    private double value;
    int x;
    int y;
    double CurrentGradient;
    public Weight(Node Target, Node Parent, int type, int layer, int layerplace, double value, int x, int y) {
        this.Target = Target;
        this.Parent = Parent;
        this.type = type;
        this.layer = layer;
        this.layerplace = layerplace;
        this.value = Equations.round(1000d, value);
        this.x = x;
        this.y = y;
        
        Target.InputWeights.add(this);
        Parent.OutputWeights.add(this);
        WeightCode = Integer.toString(layer) + "#" + Integer.toString(layerplace);
        //System.out.println(Target.getNodeCode());
        
    }
    public String getWeightCode() {
        return WeightCode;
    }
    public Node getTarget() {
        return Target;
    }
    public Node getParent() {
        return Parent;
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
    public int getType() {
        return type;
    }
    public void setValue(double value) {
        this.value = Equations.round(1000d, value);
    }
}
