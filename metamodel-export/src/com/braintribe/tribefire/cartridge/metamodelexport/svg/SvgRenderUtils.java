package com.braintribe.tribefire.cartridge.metamodelexport.svg;

import java.util.List;

import com.braintribe.model.modellergraph.graphics.AggregationKind;
import com.braintribe.model.modellergraph.graphics.Color;
import com.braintribe.model.modellergraph.graphics.Edge;
import com.braintribe.model.processing.modellergraph.common.ArrowTools;
import com.braintribe.model.processing.modellergraph.common.ColorPalette;
import com.braintribe.model.processing.modellergraph.common.Complex;

public class SvgRenderUtils {
	
	
	public static Complex turningPoint(Edge edge) {
		Complex startPoint = Complex.getComplex(edge.getStart());
		Complex endPoint = Complex.getComplex(edge.getEnd());
		Complex direction = endPoint.minus(startPoint);		
		Complex directionQuarter = direction.div(4);
		Complex turningPointComplex = Complex.getComplex(edge.getTurning());
		return turningPointComplex.minus(directionQuarter);
	}
	
	public static Complex negativeTurningPoint(Edge edge) {
		Complex startPoint = Complex.getComplex(edge.getStart());
		Complex endPoint = Complex.getComplex(edge.getEnd());
		Complex direction = endPoint.minus(startPoint);		
		Complex directionQuarter = direction.div(4);
		Complex turningPointComplex = Complex.getComplex(edge.getTurning());
		return turningPointComplex.plus(directionQuarter);
	}
	
	public static String toHex(Color color) {
	    String alpha = pad(Integer.toHexString((int)color.getAlpha()));
	    String red = pad(Integer.toHexString((int)color.getRed()));
	    String green = pad(Integer.toHexString((int)color.getGreen()));
	    String blue = pad(Integer.toHexString((int)color.getBlue()));
	    String hex = "0x" + alpha + red + green + blue;
	    return "#" + red + green + blue;
	}
	
	private static String pad(String s) {
	    return (s.length() == 1) ? "0" + s : s;
	}
	
	public static String aggregation(Edge edge, boolean start){	
		AggregationKind aggregationKind = start ? edge.getStartAggregationKind() : edge.getEndAggregationKind();
		Color color = ColorPalette.getColor(aggregationKind, false);
		String hex = ColorPalette.toHex(color);
		
		StringBuilder sb = new StringBuilder();
		String svg = "";
		switch (aggregationKind) {
		case simple_aggregation:
			return circle(edge, start, 6, hex);
		case unordered_aggregation: case ordered_aggregation:
			
//			element = new OMSVGGElement();
//			element.appendChild(renderCircle(edge, start, edgeGroup, 10));
//			element.appendChild(renderCircle(edge, start, edgeGroup, 5));
//			element.setAttribute("style", "stroke:" + color +";stroke-width:" + modelGraphConfigurations.edgeStrokeWidth + ";fill:white; opacity:" + alpha);
			break;
		case multiple_aggregation:
//			element = new OMSVGGElement();
//			OMSVGCircleElement circle1 = renderCircle(edge, start, edgeGroup, 10);
//			element.appendChild(circle1);
//			circle1.setAttribute("style", "stroke:" + color + ";stroke-width:1;fill:white");
//			OMSVGCircleElement circle2 = renderCircle(edge, start, edgeGroup, 8);
//			circle2.setAttribute("style", "stroke:" + color + ";stroke-width:0;fill:" + color);
//			element.appendChild(circle2);
//			element.setAttribute("style", "opacity:" + alpha);
			break;
		case key_association:
//			Complex complex = Complex.getComplex(start ? edge.getEnd() : edge.getStart());
//			element = new OMSVGGElement();				
//			OMSVGCircleElement circle = new OMSVGCircleElement((float)complex.x,(float)complex.y,10);				
//			element.appendChild(circle);
//			OMSVGPolygonElement arrow = new OMSVGPolygonElement();				
//			element.setAttribute("style", "stroke:" + color +";stroke-width:3;fill:white; opacity: " + alpha/* + getColor(edge.getColor())*/);
//
//			Complex tip = complex;
//			Complex direction = Complex.getComplex(edge.getTurning()).minus(tip);
//			List<Complex> arrowPaths = ArrowTools.createArrowPath(tip.plus(direction.normalize().times(5)), direction, 8, 8);
//			
//			String points = "";
//			for(int i = 0; i< arrowPaths.size();i++){
//				points += arrowPaths.get(i).x + "," + arrowPaths.get(i).y + " ";
//			}
//			arrow.setAttribute("points", points);
//			element.appendChild(arrow);
			break;
		case value_association:
//			complex = Complex.getComplex(start ? edge.getEnd() : edge.getStart());
//			element = new OMSVGGElement();
//			circle = new OMSVGCircleElement((float)complex.x,(float)complex.y,10);				
//			element.appendChild(circle);
//			arrow = new OMSVGPolygonElement();				
//			element.setAttribute("style", "stroke:" + color +";stroke-width:3;fill:white; opacity: " + alpha/* + getColor(edge.getColor())*/);
//							
//			tip = complex;
//			direction = tip.minus(Complex.getComplex(edge.getTurning()));
//			arrowPaths = ArrowTools.createArrowPath(tip.plus(direction.normalize().times(5)), direction, 8, 8);
//			
//			points = "";
//			for(int i = 0; i< arrowPaths.size();i++){
//				points += arrowPaths.get(i).x + "," + arrowPaths.get(i).y + " ";
//			}
//			arrow.setAttribute("points", points);
//			element.appendChild(arrow);
			break;
		default:
			break;
	}
		
		return "";
	}
	
	private static String circle(Edge edge, boolean start, double radius, String hex){
		float x = 0, y = 0;
		x = (float) (start ? edge.getStart().getX().floatValue() : edge.getEnd().getX().floatValue());
		y = (float) (start ? edge.getStart().getY().floatValue() : edge.getEnd().getY().floatValue());
		
		return "<circle r='" + radius + "' cx='" + x + "' cy='" + y + "' style='stroke:" + hex + ";stroke-width:2;fill:white'></circle>";
	}

}
