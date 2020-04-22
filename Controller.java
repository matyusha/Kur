package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class Controller extends Application {
    public TextField TField;
    public TextField cField;
    public TextField rField;
    public TextField sField;
    public TextField IField;
    public TextField NField;
    public TextField kField;

    public TextField layerField;
    public LineChart<Number, Number> lineChart;

    public CheckBox analyticalCheckBox;
    public RadioButton Mat;
    public RadioButton Myak;
    public RadioButton Vol;

    public Button calcButton;

    public TextArea area;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane pane = FXMLLoader.load(getClass().getResource("gui.fxml"));
        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.show();

    }

    private void showException(Exception e) {
        area.appendText("Ошибка! " + e.getMessage() + "\n");
    }

    private void println(String message) {
        area.appendText(message);
        area.appendText("\n");
    }

    public void initialize() {
        lineChart.setCreateSymbols(false);
        lineChart.getXAxis().setLabel("X");
        lineChart.getYAxis().setLabel("u");
        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().add(Mat);
        toggleGroup.getToggles().add(Myak);
        toggleGroup.getToggles().add(Vol);
        calcButton.setOnAction(event -> {
            try {
                calculate();
            } catch (Exception e) {
                showException(e);
            }
        });
    }

    private void calculate() {
        double R = Double.parseDouble(rField.getText());
        double s = Double.parseDouble(sField.getText());
        double T = Double.parseDouble(TField.getText());
        double k = Double.parseDouble(kField.getText());
        double c = Double.parseDouble(cField.getText());
        int I = Integer.parseInt(IField.getText());
        int N = Integer.parseInt(NField.getText());

        int layer = Integer.parseInt(layerField.getText());
        if (layer > N) {
            layer = layer%N;
            println("!!! номер просматриваемого слоя был изменен !!!");
        }

        lineChart.getData().clear();

        if (analyticalCheckBox.isSelected()) {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            double hi = 2*Math.PI * R/1000;
            double[] u = UtilFunctions.calculatePointsForAnalytic(1000, hi, (T/1000)* layer, k, c, R, !Vol.isSelected());
            for (int i = 0; i < u.length; i++) {
                series.getData().add(new XYChart.Data<>(hi*i, u[i]));
            }
            series.setName("Аналитическое решение");
            lineChart.getData().add(series);
        }

        if (Mat.isSelected()) {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            double hi = 2*Math.PI * R/I;
            double[] u = UtilFunctions.calculatePointsMatScheme(I,N, hi,T/N, c, k,R, layer);

            for (int i = 0; i < u.length; i++) {
                series.getData().add(new XYChart.Data<>(hi*i, u[i]));
            }
            series.setName("Матюшкина решение");
            lineChart.getData().add(series);
        }

        if (Myak.isSelected()) {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            double hi = (2*Math.PI * R)/I;
            double[] u = UtilFunctions.calculatePointsMyakScheme(I,N, hi,T/N, c, k,R, layer);

            for (int i = 0; i < u.length; i++) {
                series.getData().add(new XYChart.Data<>(hi*i, u[i]));
            }
            series.setName("Мякушко решение");
            lineChart.getData().add(series);
        }

        if (Vol.isSelected()){
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            double hi = 2*Math.PI * R/I;
            double[] u = UtilFunctions.calculatePointsVolScheme(I,N, hi,T/N, c, k,R, layer);

            for (int i = 0; i < u.length; i++) {
                series.getData().add(new XYChart.Data<>(hi*i, u[i]));
            }
            series.setName("Волошин решение");
            lineChart.getData().add(series);
        }



    }


}
