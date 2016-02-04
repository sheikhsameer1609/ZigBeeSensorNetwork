package com.serial;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.beans.SensorDataBeanImpl;
import com.boundary.DataHistoryBoundary;
import com.entity.ThresholdValueSaver;
import com.services.DataService;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.Axis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Text;
import javafx.stage.Stage;

@SuppressWarnings("rawtypes")
public class ChartLauncher extends Application {
	static SerialPortDataReader serialportreader = new SerialPortDataReader();
	final static AudioClip alert = new AudioClip(ChartLauncher.class.getResource("alertvibrate.mp3").toString());
	Timer time = new Timer();
	final ObservableList<ZigBeeModule> data = FXCollections.observableArrayList();
	int counter;
	
	public static Map<String, XYChart.Series> lightseriesmap = new HashMap<String, XYChart.Series>();
	public static Map<String, XYChart.Series> tempseriesmap = new HashMap<String, XYChart.Series>();
	// public static List<XYChart.Series> li= new ArrayList<XYChart.Series>();


	@SuppressWarnings("unchecked")
	
	public static LineChart<Number,Number> lightlinechart = new LineChart(new NumberAxis(), new NumberAxis(1, 22, 0.5));
	@SuppressWarnings("unchecked")
	public static LineChart<Number,Number> templinechart = new LineChart(new NumberAxis(), new NumberAxis(1, 22, 0.5));
	SensorDataBeanImpl sdbi = SensorDataBeanImpl.getInstance();
	public void startTimer() {

	}

	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage primaryStage) {
		time.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				Platform.runLater(new Runnable() {

					public void run() {
						ThresholdValueSaver t = sdbi.getThresholdValue();
						for (Map.Entry<String, ZigBeeModule> entry : ZigBeeModuleCollector
								.getZigbeemap().entrySet()) {
							
							
							
							if (entry.getValue().isNewdata()) {
								System.out.println("ID: " + entry.getKey()
										+ ". Light: "
										+ entry.getValue().getLightdata()
										+ ". Temp: "
										+ entry.getValue().getTemperaturedata() +" current time: "+entry.getValue().getCurrenttime());
								counter = entry.getValue().getCounter();
							/*	//counter = entry.getValue().getCurrenttime();
								if(!data.isEmpty() && data.size() >= new Integer(entry.getKey()).intValue()&&  data.get((new Integer(entry.getKey())).intValue()-1) !=null){
									data.get((new Integer(entry.getKey())).intValue() -1).setHumidity(""+entry.getValue().getHumidity());
									data.get((new Integer(entry.getKey())).intValue()-1).setLightdata(""+entry.getValue().getLightdata());
									data.get((new Integer(entry.getKey())).intValue()-1).setTemperaturedata(""+entry.getValue().getTemperaturedata());
								}else{*/
									data.add( new ZigBeeModule(new Integer(entry.getKey()).intValue(),new String(""+entry.getValue().getLightdata()),new String(""+entry.getValue().getTemperaturedata()),new String(""+entry.getValue().getHumidity()),entry.getValue().getCurrenttime()));
								//}
									
								getSeriesLight(entry.getKey()).getData().add(
										new XYChart.Data(counter, new Integer(entry.getValue()
												.getLightdata())));
								getSeriesTemp(entry.getKey()).getData().add(
										new XYChart.Data(counter, new Integer(entry.getValue()
												.getTemperaturedata())));

								entry.getValue().setCounter(counter + 1);

								entry.getValue().setNewdata(false);
							}
						}
						
						boolean alert = false;
						for (Map.Entry<String, ZigBeeModule> entry : ZigBeeModuleCollector
								.getZigbeemap().entrySet()) {
							
							if( new Integer(entry.getValue().getLightdata()).intValue() > new Integer(t.getLightthresholdMax()) ){
								
								alert("The light value is increased above max light threshold at id: "+ entry.getKey()); 
								alert=true;
							}
							if( new Integer(entry.getValue().getLightdata()).intValue() <  new Integer(t.getLightthresholdMin()).intValue() ){
								
								alert("The light value is decreased below min threshold at id:  "+ entry.getKey()); 
								alert=true;
							}
							if( new Integer(entry.getValue().getTemperaturedata()).intValue() >= new Integer(t.getTempthresholdMax()).intValue() ){
								
								alert("The temperature value is increased above max temperature threshold at id:  "+ entry.getKey()); 
								alert=true;
							}
							if( new Integer(entry.getValue().getTemperaturedata()).intValue() < new Integer(t.getTempthresholdMin()) ){
								alert("The temperature value is decreased below max light threshold at id:  "+ entry.getKey()); 
								alert=true;
							}
							
						}
						if(alert){
							alert("");
						}
						else{
						stopAlert();
						alert = false;		
						}
						

					}

				});

			}
		}, 2000, 2000);

		// random axis here
		lightlinechart.getXAxis().setAutoRanging(true);
		lightlinechart.getYAxis().setAutoRanging(true);
		templinechart.getXAxis().setAutoRanging(true);
		templinechart.getYAxis().setAutoRanging(true);

		
		// btn.setOnAction(new EventHandler<ActionEvent>() {});
	/*	VBox root = new VBox();
		root.getChildren().add(lightlinechart);
		root.getChildren().add(templinechart);

		
		
		Scene scene = new Scene(root, 300, 250);

		primaryStage.setTitle("Hello World!");
		primaryStage.setScene(scene);
		primaryStage.show();*/
		BorderPane root = new BorderPane();
		
		
		TabPane root1 = new TabPane();
		
		VBox hometab = new VBox();
		VBox tab1 = new VBox();
		VBox tab2 = new VBox();
		
		Tab home = new Tab();
		home.setClosable(false);
			home.setText("Home");
			home.setContent(hometab);
			
			TableView<ZigBeeModule> table = new TableView<ZigBeeModule>();
			TableColumn<ZigBeeModule, Integer> id = new TableColumn<ZigBeeModule, Integer>();
			id.setText("Id");
			TableColumn<ZigBeeModule, Integer> first = new TableColumn<ZigBeeModule, Integer>();
			first.setText("Temperature");
			TableColumn<ZigBeeModule, Integer> second = new TableColumn<ZigBeeModule, Integer>();
			second.setText("Light");
			TableColumn<ZigBeeModule, Integer> third = new TableColumn<ZigBeeModule, Integer>();
			third.setText("Humidity");
			
			TableColumn<ZigBeeModule, Date> fourth = new TableColumn<ZigBeeModule, Date>();
			fourth.setText("Time");
			
			
			id.setCellValueFactory(new PropertyValueFactory<ZigBeeModule, Integer>("id"));
			first.setCellValueFactory(new PropertyValueFactory<ZigBeeModule, Integer>("temperaturedata"));
	        second.setCellValueFactory(new PropertyValueFactory<ZigBeeModule, Integer>("lightdata"));
	        third.setCellValueFactory(new PropertyValueFactory<ZigBeeModule, Integer>("humidity"));
	        fourth.setCellValueFactory(new PropertyValueFactory<ZigBeeModule, Date>("currenttime"));
	        
	        
	        table.setItems(data);
	        table.getColumns().addAll(id,first,second,third,fourth);
		Tab x = new Tab();
		x.setClosable(false);
			x.setText("Graph");
			Tab y = new Tab();
			y.setClosable(false);
			y.setText("Threshold");
			
			//root.getChildren().add(root1);
			
			tab1.getChildren().add(lightlinechart);
			tab1.getChildren().add(templinechart);
			x.setContent(tab1);			
					
			HBox tempboxmax = new HBox();
			HBox lightboxmax = new HBox();
			HBox tempboxmin = new HBox();
			HBox lightboxmin = new HBox();
			HBox buttons = new HBox();
			Text tempTitlemax = new Text("Temperature Threshold Max (in celcius): ");
			Text tempTitlemin = new Text("Temperature Threshold Min (in celcius): ");
			Text lightTitlemax = new Text("Light Threshold Max (in flux):       ");
			Text lightTitlemin = new Text("Light Threshold Min (in flux):       ");
			final TextField tetempmax = new TextField(DataHistoryBoundary.getThresholdValue().getTempthresholdMax());
			final TextField telightmax = new TextField(DataHistoryBoundary.getThresholdValue().getLightthresholdMax());
			final TextField tetempmin = new TextField(DataHistoryBoundary.getThresholdValue().getTempthresholdMin());
			final TextField telightmin = new TextField(DataHistoryBoundary.getThresholdValue().getLightthresholdMin());
			
			TextArea text= new TextArea();
			
			tetempmax.setEditable(false);
			telightmax.setEditable(false);
			tetempmin.setEditable(false);
			telightmin.setEditable(false);
			tempboxmax.getChildren().addAll(tempTitlemax,tetempmax);
			
			lightboxmax.getChildren().addAll(lightTitlemax,telightmax);

            tempboxmin.getChildren().addAll(tempTitlemin,tetempmin);
			
			lightboxmin.getChildren().addAll(lightTitlemin,telightmin);
			
			Button but = new Button("Save");
			Button edit = new Button("Edit");
			buttons.getChildren().addAll(edit,but);
			tab2.getChildren().addAll(tempboxmax,tempboxmin,lightboxmax,lightboxmin,buttons);
			
			
			
			home.setClosable(false);
			hometab.getChildren().add(table);
			

			edit.setOnAction(new EventHandler<ActionEvent>() {

				public void handle(ActionEvent arg0) {
					tetempmax.setEditable(true);
					telightmax.setEditable(true);
					tetempmin.setEditable(true);
					telightmin.setEditable(true);
					
				}
			});
			
			but.setOnAction(new EventHandler<ActionEvent>() {

				public void handle(ActionEvent arg0) {
					
					DataHistoryBoundary.saveThresholdToDatabase(telightmax.getText(),tetempmax.getText(),telightmin.getText(),tetempmin.getText());
					tetempmax.setEditable(false);
					telightmax.setEditable(false);
					tetempmin.setEditable(false);
					telightmin.setEditable(false);
				}
			});
			
			
			y.setContent(tab2);
			root1.getTabs().add(home);
			root1.getTabs().add(x);
			root1.getTabs().add(y);
			
		Scene scene = new Scene(root1, 300, 250);
		
		primaryStage.setTitle("Hello World!");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static Boolean isPresentLightSeries(String seriesid) {

		return lightseriesmap.containsKey(seriesid);

	}

	public static Boolean isPresentTempSeries(String seriesid) {

		return tempseriesmap.containsKey(seriesid);

	}

	public static XYChart.Series getSeriesLight(String lightseriesid) {
		XYChart.Series lightseries = null;
		if (isPresentLightSeries(lightseriesid)) {
			lightseries = lightseriesmap.get(lightseriesid);
		} else {
			lightseries = new XYChart.Series();
			lightseries.setName("Module " + lightseriesid);
			lightseriesmap.put(lightseriesid, lightseries);
			lightlinechart.getData().add(lightseries);

		}
		return lightseries;
	}

	public static XYChart.Series getSeriesTemp(String tempSeriesId) {
		XYChart.Series tempseries;
		if (isPresentTempSeries(tempSeriesId)) {
			tempseries = tempseriesmap.get(tempSeriesId);
		} else {
			tempseries = new XYChart.Series();
			tempseries.setName("Module " + tempSeriesId);
			tempseriesmap.put(tempSeriesId, tempseries);
			templinechart.getData().add(tempseries);

		}
		return tempseries;
	}

	public static void main(String[] args) {
		lightlinechart.setTitle("Light charting");
		templinechart.setTitle("Temp charting");
		
	    
	
		serialportreader.startSerial();
		launch();
	}
	
	public static void alert(String string){
		alert.play();
	}
	public static void stopAlert(){
		if(alert.isPlaying())
		alert.stop();
	}
}