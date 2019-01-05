//package org.usfirst.frc.team1619.robot.framework.util.plot;
//
//import java.awt.Dimension;
//import java.util.Map;
//
//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.plot.XYPlot;
//import org.jfree.chart.renderer.xy.XYDotRenderer;
//import org.jfree.data.xy.XYDataItem;
//import org.jfree.data.xy.XYDataset;
//import org.jfree.data.xy.XYSeries;
//import org.jfree.data.xy.XYSeriesCollection;
//
//public class DotPlot extends Plot<Double, Double> {
//
//	private static final long serialVersionUID = 5773484160587724101L;
//
//	private String xAxisLabel;
//	private String yAxisLabel;
//	private XYSeries data;
//
//	public DotPlot(String applicationTitle, String chartTitle, Dimension preferredSize, String xAxisLabel,
//			String yAxisLabel, Map<Double, Double> mappedData) {
//		super(applicationTitle, chartTitle, preferredSize, mappedData);
//
//		this.xAxisLabel = xAxisLabel;
//		this.yAxisLabel = yAxisLabel;
//
//		this.construct();
//	}
//
//	@Override
//	protected void createRenderer() {
//		XYDotRenderer renderer = new XYDotRenderer();
//
//		renderer.setSeriesVisible(0, true);
//		renderer.setDotHeight(5);
//		renderer.setDotWidth(5);
//	}
//
//	@Override
//	protected void createDataset() {
//		XYSeriesCollection dataset = new XYSeriesCollection();
//		this.data = new XYSeries(0);
//
//		for (Map.Entry<Double, Double> entry : this.mappedData.entrySet()) {
//			this.data.add(new XYDataItem(entry.getKey(), entry.getValue()));
//		}
//
//		dataset.addSeries(this.data);
//		this.dataset = dataset;
//	}
//
//	@Override
//	protected void createChart() {
//		this.chart = ChartFactory.createXYLineChart(this.chartTitle, this.xAxisLabel, this.yAxisLabel,
//				(XYDataset) this.dataset);
//	}
//
//	@Override
//	protected void createPlot() {
//		XYPlot xyPlot = new XYPlot();
//		xyPlot.setRenderer((XYDotRenderer) this.renderer);
//	}
//
//}
