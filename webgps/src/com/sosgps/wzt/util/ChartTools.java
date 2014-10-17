package com.sosgps.wzt.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.ui.Layer;
import org.jfree.ui.LengthAdjustmentType;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.jfree.chart.labels.*;
import org.jfree.chart.axis.*;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import com.sosgps.wzt.system.common.Constants;

public class ChartTools extends ServletUtilities{
	public static JFreeChart createBarChart(long[] yAxis, String[] xAxis, double barWidth, String topTitle, String xTitle, String yTitle , String averageLabel, double average) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (int i = 0; i < yAxis.length; i++) {
			dataset.addValue(yAxis[i], "", xAxis[i]);
		}
		Font font = new Font("����", Font.BOLD, 17); // �������壬����������ʾ����
		Font xfont = new Font("����", Font.PLAIN, 14); // �������壬����������ʾ����
		
		JFreeChart chart = ChartFactory.createBarChart(topTitle, xTitle, yTitle, dataset, PlotOrientation.VERTICAL, false, true, false);
		chart.setBackgroundPaint(Color.WHITE); // ���ñ���ɫ
		// chart.setBackgroundPaint(new Color(0xE1E1E1)); //���ñ���ɫ
		chart.getTitle().setFont(font); // ���ñ�������
		CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(new Color(255, 255, 255)); // ���û�ͼ������ɫ
		plot.setRangeGridlinePaint(Color.gray); // ����ˮƽ���򱳾�����ɫ
		plot.setRangeGridlinesVisible(true); // �����Ƿ���ʾˮƽ���򱳾���,Ĭ��ֵΪTrue
		plot.setDomainGridlinePaint(Color.black); // ���ô�ֱ���򱳾�����ɫ
		 plot.setForegroundAlpha(0.80f);// ��������͸����
		// plot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);//����X����ʾλ��
		// plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);// ����Y����ʾλ��
		// ����Y����ʾ����
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		rangeAxis.setLabelFont(font);
		/*------������״����ͼƬ�߿�����¼��---*/
		ValueAxis rAxis = plot.getRangeAxis();
		rAxis.setUpperMargin(0.15);
		rAxis.setLowerMargin(0.15);
		/*------����X��������б�̶�----*/
		CategoryAxis domainAxis = plot.getDomainAxis();
		// ���þ���ͼƬ��˾���
		domainAxis.setLowerMargin(0.05);
		domainAxis.setLabelFont(font);// �����
		domainAxis.setTickLabelFont(xfont);// ����ֵ
		// domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI
		// / 6.0));
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45); // ���÷����ǩ��б45��
		// domainAxis.setTickLabelFont(font);
		/*------������״����ͼƬ�߿�����Ҽ��--*/
		domainAxis.setLowerMargin(0.01);
		domainAxis.setUpperMargin(0.01);
		CustomBarRenderer3D renderer = new CustomBarRenderer3D();
		if(averageLabel!=null&&averageLabel.length()>0&&average>0){
			
			renderer.setAverageValue(average);
			ValueMarker valuemarker = new ValueMarker(average); 
			valuemarker.setLabelOffsetType(LengthAdjustmentType.EXPAND); 
			valuemarker.setPaint(new Color(200, 200, 255)); 
			valuemarker.setStroke(new BasicStroke(1.0F));
			valuemarker.setLabel(averageLabel); 
			valuemarker.setLabelFont(new Font("����", Font.BOLD, 14)); 
			valuemarker.setLabelPaint(Color.BLACK); 
			valuemarker.setLabelAnchor(RectangleAnchor.TOP_LEFT); 
			valuemarker.setLabelTextAnchor(TextAnchor.BOTTOM_LEFT); 
			valuemarker.setLabelOffset(new RectangleInsets(5, 5, 5, 5)); 
			float[] f = { 2, 4, 2, 4 }; 
			valuemarker.setStroke(new BasicStroke(2.0f, 1, 1, 0, f, 1.0f)); // setStroke ����׼������Ϊ���ߣ�float[]����ʵ�֡� 
			plot.addRangeMarker(valuemarker, Layer.BACKGROUND); 

		}
		// ����������ɫ
		// renderer.setSeriesPaint(0, new Color(0xff00));
		/*---------����ÿһ����״��֮��ļ��---------*/
		renderer.setItemMargin(0.1);
		// �����
		renderer.setMaximumBarWidth(barWidth);
		renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());// ��ʾ�����ʾ
		// renderer.setWallPaint(Color.gray);// ���� Wall ����ɫ
		renderer.setItemLabelAnchorOffset(10D);// ��������ͼ�ϵ�����ƫ��ֵ
		renderer.setBaseItemLabelFont(new Font("arial", Font.PLAIN, 10), true);// ��������ͼ�ϵ�����
		// ��ʾÿ��������ֵ�����޸ĸ���ֵ����������
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(
				ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));
		renderer.setBaseItemLabelsVisible(true);
		plot.setRenderer(renderer);
		return chart;
	}
	public static double getBarWidth(int count) {
		double barWidth = 0.045;
		if(count <= 10){
			barWidth = 0.045;
		}else if(count <= 15){
			barWidth = 0.035;
		}else if(count <= 20){
			barWidth = 0.025;
		}else if(count <= 25){
			barWidth = 0.02;
		}else if(count <= 30){
			barWidth = 0.015;
		}else if(count <= 35){
			barWidth = 0.013;
		}else if(count <= 40){
			barWidth = 0.011;
		}else if(count <= 45){
			barWidth = 0.008;
		}else if(count <= 60){
			barWidth = 0.007;
		}
		return barWidth;
	}

	@SuppressWarnings("serial")
	static class CustomBarRenderer3D extends BarRenderer3D {
		double average = 0D;
		public void setAverageValue(double average) {
			this.average = average;
		}
		public double getAverageValue() {
			return this.average;
		}
		public Paint getItemPaint(int paramInt1, int paramInt2) {
			double average = getAverageValue();
			if (average == 0) {
				return new GradientPaint(0.0F, 0.0F, new Color(0x006f6fee), 0.0F, 0.0F, new Color(0x006f6fee));
			} else {
				CategoryDataset localCategoryDataset = getPlot().getDataset();
				double d = localCategoryDataset.getValue(paramInt1, paramInt2)
						.doubleValue();
				if (d >= average)
					return new GradientPaint(0.0F, 0.0F, new Color(0x006f6fee), 0.0F, 0.0F, new Color(0x006f6fee));
				return new GradientPaint(0.0F, 0.0F, new Color(0x00ee6f6f), 0.0F, 0.0F, new Color(0x00ee6f6f));
			}
		}
	}
	
	public static String saveChartAsPNG(JFreeChart chart, int width, int height, ChartRenderingInfo info, HttpSession session) throws IOException {
		if (chart == null) {
			throw new IllegalArgumentException("Null 'chart' argument.");   
		}
		ServletUtilities.createTempDir();
		String prefix = ServletUtilities.getTempFilePrefix();
		if (session == null) {
			prefix = ServletUtilities.getTempOneTimeFilePrefix();
		}
		File tempFile = File.createTempFile(prefix, ".png",  new File(Constants.CHART_PATH));
		ChartUtilities.saveChartAsPNG(tempFile, chart, width, height, info);
		if (session != null) {
			ServletUtilities.registerChartForDeletion(tempFile, session);
		}
		return tempFile.getName();
	}
	
}
