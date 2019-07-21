import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
public class Driver
{
    int[] LayerStats; //Definition = [Number of nodes in layer 0, Number of nodes in layer 1... , Bias' enable];
    Node[] TotalNodeCount;
    Weight[] TotalWeightCount;
    Node[][] Node2DCount;
    Node[] Node2DCountBias; //no actual 2d array
    double LearningRate;
    boolean GradientChecking;
    double[][] TestingArray;
    int Runs = 0;
    boolean StepBased;
    int NumberofTests;
    int FrameSpeed;
    
    int leastXDistance;
    int leastYDistance;
    public Driver() {
        
    }
    public void setNeuralNetworkSettings() {
        /* rules
         * Input of 0 wants output of 0 0 
         * Input of 1 wants output of 0 1
         * Input of 2 wants output of 1 0 
         * Input of 3 wants output of 1 1
         */
        //LayerStats = new int[] {# of Nodes in input layer, # of Nodes in hidden layer 1, # of nodes in hidden layer 2... # of nodes in output layer, (1, or 0 based off of whether bias nodes are enabled)}
        LayerStats = new int[]{1, 3, 3, 2, 1};
        //LearningRate = Used in determining how much weights change each step
        LearningRate = 0.5;
        
        GradientChecking = false;
        //Step based determines whether 
        StepBased = false;
        FrameSpeed = 333;
        NumberofTests = 50000;
        //[red, orange, yellow, green, blue, purple, pink, grey, black, white]
        TestingArray = new double[][] { 
        new double[]{0.0}, new double[]{0, 0},
        new double[]{0.1}, new double[]{0, 1},
        new double[]{0.2}, new double[]{1, 0},
        new double[]{0.3}, new double[]{1, 1} 
        };
    }
    public void StartNeuralNetwork() {
        setNeuralNetworkSettings();
        //Set up 2DNode arrays
        Node2DCount = new Node[LayerStats.length - 1][];
        for (int i = 0; i <= LayerStats.length - 2; i++) {
            Node2DCount[i] = new Node[LayerStats[i]];
        }
        Node2DCountBias = new Node[LayerStats.length - 2];
        int CountofNodes = 0;
        //finds amount of Nodes
        for (int i = 0; i <= LayerStats.length - 2; i++) {
            CountofNodes += LayerStats[i];
        }
        if (LayerStats[LayerStats.length - 1] == 1) CountofNodes += LayerStats.length - 2;
        TotalNodeCount = new Node[CountofNodes];
        //Creates each individual node
        CountofNodes = 0;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int xdistance = (int)((screenSize.getWidth() - 300) / (LayerStats.length - 1)); //150 borders on both sides
        leastXDistance = xdistance;
        int least = -1;
        for (int i = 0; i <= LayerStats.length - 2; i++) {
            if (least == -1) least = LayerStats[i];
            else if (LayerStats[i] < least) least = LayerStats[i];
        }
        leastYDistance = (int)((screenSize.getHeight() - 300) / least);
        int ydistance; //200 from top 100 from bottem
        for (int i = 0; i <= LayerStats.length - 2; i++) {
            ydistance = (int)((screenSize.getHeight() - 300) / LayerStats[i]);
            for (int ii = 1; ii <= LayerStats[i]; ii++) {
                
                //input node
                if (i == 0) TotalNodeCount[CountofNodes] = new Node(0, i, ii - 1, Math.random(), i * xdistance + 150, (ii - 1) * (/*150 + 50 * (i % 2 == 0 ? 1 : 0)*/ydistance) + 200);
                //Outer node
                if (i == LayerStats.length - 2) TotalNodeCount[CountofNodes] = new Node(2, i, ii - 1, Math.random(), i * xdistance + 150, (ii - 1) * (/*150 + 50 * (i % 2 == 0 ? 1 : 0)*/ ydistance) + 200);
                //hidden node
                if ((i != 0) && (i != LayerStats.length - 2)) TotalNodeCount[CountofNodes] = new Node(1, i, ii - 1, Math.random(), i * xdistance + 150, (ii - 1) * (/*150 + 50 * (i % 2 == 0 ? 1 : 0)*/ ydistance) + 200);
                
                Node2DCount[i][ii - 1] = TotalNodeCount[CountofNodes];
                
                CountofNodes++;
            }
        }
        //Creates bias nodes    
        if (LayerStats[LayerStats.length - 1] == 1) {
            for (int i = 1; i <= LayerStats.length - 2; i++) {
                TotalNodeCount[CountofNodes] = new Node(3, i - 1, -1, 1, (int)(((i - 1) * (xdistance)) + 150 + xdistance * 0.25)/*(int)((i - 1) * (xdistance + 50 * (i % 2 == 0 ? 1 : 0)) + 150)*/, 100);
                Node2DCountBias[i - 1] = TotalNodeCount[CountofNodes];
                CountofNodes++;
            }
        }
        //Finding aomunt of weights
        int CountofWeights = 0;
        int CurrentWeightPlace = 0;
        for (int i = 0; i <= LayerStats.length - 3; i++) {
            CountofWeights += LayerStats[i] * LayerStats[i + 1];
        }
        if (LayerStats[LayerStats.length - 1] == 1) {
            for (int i = 1; i <= LayerStats.length - 2; i++) {
                CountofWeights += LayerStats[i];
            }
        }
        
        TotalWeightCount = new Weight[CountofWeights];
        CountofWeights = 0;
        int NegativeWeightPlace = -1;
        //Creating individual weights
        
        int count = 0;
        for (int i = 0; i <= TotalNodeCount.length - 1; i++) {
            Node CurrentNode = TotalNodeCount[i];
            if (CurrentNode.getlayer() != LayerStats.length - 2) {
                for (int ii = 1; ii <= LayerStats[CurrentNode.getlayer() + 1]; ii++) {
                    if (CurrentNode.getType() != 3) {
                        Node Target = FindNode(Integer.toString(CurrentNode.getlayer() + 1) + "#" + Integer.toString(ii - 1));
                        Node Parent = CurrentNode;
                        //System.out.println(Target.getNodeCode());
                        //System.out.println(Parent.getNodeCode());
                        TotalWeightCount[CountofWeights] = new Weight(Target, Parent, 0, CurrentNode.getlayer(), CurrentWeightPlace, Math.random(), (Parent.x + Target.x) / 2, (Parent.y + Target.y) / 2);
                        CountofWeights++;
                        CurrentWeightPlace++;
                    } else {
                        Node Target = FindNode(Integer.toString(CurrentNode.getlayer() + 1) + "#" + Integer.toString(ii - 1));
                        Node Parent = CurrentNode;
                        TotalWeightCount[CountofWeights] = new Weight(Target, Parent, 1, CurrentNode.getlayer(), NegativeWeightPlace, Math.random(), (Parent.x + Target.x) / 2, (Parent.y + Target.y) / 2);
                        //System.out.println(TotalWeightCount[CountofWeights].getTarget().getNodeCode());
                        CountofWeights++;
                        NegativeWeightPlace--;
                    }
                      
                }
                
                if ((i != TotalNodeCount.length - 1) && (TotalNodeCount[i + 1].getlayer() != CurrentNode.getlayer())) {
                    CurrentWeightPlace = 0;
                    NegativeWeightPlace = -1;
                }
            }
            
        }

        //setExampleValues();
        for (int i = 1; i <= NumberofTests; i++) {
            double Max = (TestingArray.length / 2) - 1;
            double place = 0 + (int)(Math.random() * ((Max - 0) + 1));
            OneRun(TestingArray[(int)(place * 2)], TestingArray[(int)(place * 2 + 1)]);
        }
        
    }
    public void StepUpdate() {
        if (StepBased == true) {
            double Max = (TestingArray.length / 2) - 1;
            double place = 0 + (int)(Math.random() * ((Max - 0) + 1));
            OneRun(TestingArray[(int)(place * 2)], TestingArray[(int)(place * 2 + 1)]);
        }
    }
    public void GraphicsUpdate(Graphics g, Graphics2D g2) {    
        int Measurement = Math.min(leastXDistance, leastYDistance);
        int NodeWidth = (int)(Measurement * 0.3); //max of each
        int WeightWidth = (int)(NodeWidth / 3); //max of each
        //draw nodes
        for (int i = 0; i <= TotalNodeCount.length - 1 /*- (((LayerStats[LayerStats.length - 1] == 1) ? 1 : 0) * (LayerStats.length - 2))*/; i++) {
            Node CurrentNode = TotalNodeCount[i];
            g2.drawString(CurrentNode.getNodeCode(), CurrentNode.x, CurrentNode.y - NodeWidth / 2);
            g2.drawString(Double.toString(CurrentNode.getValue()), CurrentNode.x - NodeWidth / 4, CurrentNode.y);
            g.drawOval(CurrentNode.x - NodeWidth / 2, CurrentNode.y - NodeWidth / 2, (int)(NodeWidth * 0.25 * (CurrentNode.getValue()) + (NodeWidth * 0.75)), (int)(NodeWidth * 0.25 * (CurrentNode.getValue()) + (NodeWidth * 0.75)));
        }
        //Drawing weights
        for (int i = 0; i <= TotalWeightCount.length - 1; i++) {
            Weight CurrentWeight = TotalWeightCount[i];
            int Parentx = CurrentWeight.getParent().x;
            int Parenty = CurrentWeight.getParent().y;
            int Targetx = CurrentWeight.getTarget().x;
            int Targety = CurrentWeight.getTarget().y;
            double Distance = Equations.CalculateDistance(Parentx, Parenty, Targetx, Targety);
            double Angle = Equations.CalculateAngle(Parentx, Parenty, Targetx, Targety);
            g.drawOval(CurrentWeight.x - (WeightWidth / 2), CurrentWeight.y - (WeightWidth / 2), WeightWidth, WeightWidth);
            g2.drawString(Double.toString(CurrentWeight.getValue()), CurrentWeight.x - NodeWidth / 4, CurrentWeight.y);
            g2.drawString(CurrentWeight.getWeightCode(), CurrentWeight.x, CurrentWeight.y - WeightWidth / 2);
            g.drawLine(Parentx, Parenty, Targetx, Targety);
        }
        //Drawing inputs and desired outputs
        for (int i = 0; i <= LayerStats[0] - 1; i++) {
            g2.drawString(Double.toString(Node2DCount[0][i].getValue()), 0, i * 10 + 10); 
        }
        
        for (int i = 0; i <= LayerStats[LayerStats.length - 2] - 1; i++) {
            g2.drawString(Double.toString(Node2DCount[LayerStats.length - 2][i].getValue()), 100, i * 10 + 10);
            g2.drawString(Double.toString(Node2DCount[LayerStats.length - 2][i].TargetOutput), 200, i * 10 + 10);
        }
        g2.drawString("Runs " + Integer.toString(Runs), 1800, 10);
    }
    public Node FindNode(String NodeCode) {
        for (int i = 0; i <= TotalNodeCount.length - 1; i++) {
            if (TotalNodeCount[i].getNodeCode().equals(NodeCode)) return TotalNodeCount[i];
        }
        Node bazinga = new Node(-1, -1, -1, -1, -1, -1);
        return bazinga;
    }
    public Weight FindWeight(String WeightCode) {
        for (int i = 0; i <= TotalWeightCount.length - 1; i++) {
            if (TotalWeightCount[i].getWeightCode().equals(WeightCode)) return TotalWeightCount[i];
        }   
        Node aa = new Node(-1, -1, -1, -1, -1, -1);
        Weight bazinga = new Weight(aa, aa, -1, -1, -1, -1, -1, -1);
        return bazinga;
    }
    public void setNodeValue(String NodeCode, double value) {
        Node CurrentNode = FindNode(NodeCode);
        CurrentNode.setValue(value);
    }
    public void setWeightValue(String WeightCode, double value) {
        Weight CurrentWeight = FindWeight(WeightCode);
        CurrentWeight.setValue(value);
    }
    public void setExampleValues() {
        setWeightValue("0#-1", 0.8);
        setWeightValue("0#-2", 0.25);
        setWeightValue("0#0", 0.1);
        setWeightValue("0#1", 0.2);
        setWeightValue("0#2", 0.12);
        setWeightValue("0#3", 0.17);
        setWeightValue("1#-1", 0.15);
        setWeightValue("1#-2", 0.70);
        setWeightValue("1#0", 0.05);
        setWeightValue("1#1", 0.40);
        setWeightValue("1#2", 0.33);
        setWeightValue("1#3", 0.07);
    }
    public void setInputsandTargets(double[] Inputs, double[] TargetOutputs) {
        for (int i = 0; i <= Inputs.length - 1; i++) {
            Node2DCount[0][i].setValue(Inputs[i]);
        }
        for (int i = 0; i <= TargetOutputs.length - 1; i++) {
            Node2DCount[LayerStats.length - 2][i].TargetOutput = TargetOutputs[i];
        }
    }
    public void ForwardPropagationFunctions(Node NodeSelected) {
       setNodeValue(NodeSelected.getNodeCode(), Equations.ActivationFunction(Equations.SummationOperator(NodeSelected)));
    }
    public void ForwardPropagation() {
        for (int i = 1; i <= LayerStats.length - 2; i++) {
            for (int ii = 0; ii <= LayerStats[i] - 1; ii++) {
                ForwardPropagationFunctions(Node2DCount[i][ii]);
            }
        }
    }
    public double getTotalError() {
        double total = 0;
        for (int ii = 0; ii <= LayerStats[LayerStats.length - 2] - 1; ii++) {
            total += Equations.CalculateLocalError(Node2DCount[LayerStats.length - 2][ii]);
        }
        return total / 2;
    }
    
    public void setOutputLayerWeightGradients() {
        for (int i = 0; i <= LayerStats[LayerStats.length - 3] - 1; i++) {
            Node SelectedNode = Node2DCount[LayerStats.length - 3][i];
            for (int ii = 0; ii <= SelectedNode.OutputWeights.size() - 1; ii++) {
                ((Weight)SelectedNode.OutputWeights.get(ii)).CurrentGradient = Equations.CalculateOutputLayerWeightGradient((Weight)SelectedNode.OutputWeights.get(ii));
            }
        }
    }
    public void setOutputLayerWeightBiasGradients() {
        for (int i = 0; i <= Node2DCountBias.length - 1; i++) {
            Node CurrentNode = Node2DCountBias[i];
            if (CurrentNode.getlayer() == LayerStats.length - 3) {
                for (int ii = 0; ii <= CurrentNode.OutputWeights.size() - 1; ii++) {
                    ((Weight)CurrentNode.OutputWeights.get(ii)).CurrentGradient = Equations.DeltaZ(((Weight)CurrentNode.OutputWeights.get(ii)).getTarget());
                }
                //System.out.println(CurrentNode.getNodeCode());
            }
        }
    }
    public void setAllHiddenLayerWeightGradients() {
        //find all nodes starting at the nodes that are on a layer before the ouput nodes, and havw them look at theirinput nodes, 
        for (int i = LayerStats.length - 3; i >= 1; i--) {
            //determines which layer we are on
            for (int ii = 0; ii <= LayerStats[i] - 1; ii++) {
                //determines which node we are on
                Node CurrentNode = Node2DCount[i][ii];
                //change things to bias
                for (int iii = 0; iii <= CurrentNode.InputWeights.size() - 1; iii++) {
                    //gets each weight
                    Weight CurrentWeight = (Weight)CurrentNode.InputWeights.get(iii);
                    //unbiased hidden node
                    if (CurrentWeight.getType() == 0) CurrentWeight.CurrentGradient = Equations.CalculateHiddenLayerWeightGradient(CurrentWeight);
                    //bias hidden node
                    if (CurrentWeight.getType() == 1) CurrentWeight.CurrentGradient = Equations.CalculateHiddenLayerBiasWeightGradient(CurrentWeight);
                    //System.out.println(CurrentWeight.getWeightCode());
                    //System.out.println(CurrentWeight.CurrentGradient);
                }
            }
        }
    }
    public void DisplayCalculatedRelativeError() {
        for (int i = 0; i <= TotalWeightCount.length - 1; i++) {
            Weight CurrentWeight = TotalWeightCount[i];
            //System.out.println(" " + CurrentWeight.getWeightCode());
            //System.out.println(" " + Double.toString(Equations.GradientChecking(CurrentWeight, TotalNodeCount)));
        }
    }
    public void UpdateWeights() {
        for (int i = 0; i <= TotalWeightCount.length - 1; i++) {
            Weight CurrentWeight = TotalWeightCount[i];
            Equations.UpdateGeneralWeightFormula(CurrentWeight, LearningRate);
            
        }
    }
    public void OneRun(double[] SetInputs, double[] TargetOutputs) {
        setInputsandTargets(SetInputs, TargetOutputs);
        ForwardPropagation();
        //System.out.println("Total Error: " + Double.toString(getTotalError()));
        setOutputLayerWeightGradients();
        if (LayerStats[LayerStats.length - 1] == 1)setOutputLayerWeightBiasGradients();
        //System.out.println(Equations.CalculateHiddenLayerWeightGradient((FindWeight("0#0"))));
        setAllHiddenLayerWeightGradients();
        if (GradientChecking == true) DisplayCalculatedRelativeError();
        UpdateWeights();
        PrintOutputs();
        Runs++;
    }
    public void PrintOutputs() {
        System.out.println();
        for (int i = 0; i <= TotalNodeCount.length - 1; i++) {
            Node CurrentNode = TotalNodeCount[i];
            if (CurrentNode.getType() == 2) {
                //System.out.println(CurrentNode.getNodeCode());
                
                System.out.println(Double.toString(CurrentNode.getValue()));
            }
            //System.out.println(CurrentNode.getValue());
        }
    }
}
