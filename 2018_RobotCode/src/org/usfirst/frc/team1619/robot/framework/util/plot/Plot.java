//package org.usfirst.frc.team1619.robot.framework.util.plot;
//
//import java.awt.Dimension;
//import java.util.Map;
//
//import javax.swing.JFrame;
//
//import org.jfree.chart.ChartPanel;
//import org.jfree.chart.JFreeChart;
//import org.jfree.chart.renderer.AbstractRenderer;
//import org.jfree.data.general.Dataset;
//
//public abstract class Plot<T, U> extends JFrame {
//
//	private static final long serialVersionUID = -7065515177461816264L;
//
//	protected Map<T, U> mappedData;
//	protected String chartTitle;
//	protected Dimension chartSize;
//
//	protected AbstractRenderer renderer;
//	protected Dataset dataset;
//	protected JFreeChart chart;
//
//	private ChartPanel chartPanel;
//
//	public Plot(String applicationTitle, String chartTitle, Dimension chartSize, Map<T, U> mappedData) {
//		super(applicationTitle);
//
//		this.chartTitle = chartTitle;
//		this.chartSize = chartSize;
//		this.mappedData = mappedData;
//
//	}
//
//	protected abstract void createRenderer();
//
//	protected abstract void createDataset();
//
//	protected abstract void createPlot();
//
//	protected abstract void createChart();
//
//	protected void plot() {
//		this.chartPanel = new ChartPanel(this.chart);
//		this.chartPanel.setPreferredSize(this.chartSize);
//		setContentPane(this.chartPanel);
//	}
//
//	protected void construct() {
//		this.createRenderer();
//		this.createDataset();
//		this.createPlot();
//		this.createChart();
//
//		this.plot();
//	}
//
//}
