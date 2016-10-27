package com.xplatform.base.workflow.core.bpm.graph.activiti;

import java.awt.geom.Point2D;
import java.util.List;

public class BPMNEdge {
	private List<Point2D.Double> points;
	private String name;
	private Point2D.Double midpoint;
	private DirectionType direction;
	private String sourceRef;
	private String targetRef;

	public List<Point2D.Double> getPoints() {
		return this.points;
	}

	public void setPoints(List<Point2D.Double> points) {
		this.points = points;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Point2D.Double getMidpoint() {
		return this.midpoint;
	}

	public void setMidpoint(Point2D.Double midpoint) {
		this.midpoint = midpoint;
	}

	public DirectionType getDirection() {
		return this.direction;
	}

	public void setDirection(DirectionType direction) {
		this.direction = direction;
	}

	public String getSourceRef() {
		return this.sourceRef;
	}

	public void setSourceRef(String sourceRef) {
		this.sourceRef = sourceRef;
	}

	public String getTargetRef() {
		return this.targetRef;
	}

	public void setTargetRef(String targetRef) {
		this.targetRef = targetRef;
	}

	public String toString() {
		String str = "";
		for (Point2D.Double point : this.points) {
			str = str + point.getX() + ":" + point.getY() + "  ";
		}

		return "BPMNEdge [points=" + str + ", name=" + this.name
				+ " <midpoint=" + this.midpoint.getX() + ":"
				+ this.midpoint.getY() + ">]";
	}
}