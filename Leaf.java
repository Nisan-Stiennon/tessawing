//package Guacamole;

import Jama.*;
import javax.media.j3d.*;

class Leaf extends Node{

TransformGroup tg;

Leaf(TransformGroup tg){
	super();
	this.tg = tg;
	}

void add(Node n){
	throw new Error("Cannot add to Leaf.");
	}
	
void update(){
	super.update();
		Matrix ori = absoluteOrientation.times(orientation);
	Matrix pos = absolutePosition.plus(absoluteOrientation.times(position));
	// we need to forget the 4th component and pack the data into a Transform3D
	double[] flattened = {
		ori.get(0, 0),
		ori.get(0, 1),
		ori.get(0, 2),
		pos.get(0, 0),
		ori.get(1, 0),
		ori.get(1, 1),
		ori.get(1, 2),
		pos.get(1, 0),
		ori.get(2, 0),
		ori.get(2, 1),
		ori.get(2, 2),
		pos.get(2, 0),
		0,
		0,
		0,
		1};
	Transform3D transform = new Transform3D(flattened);
	tg.setTransform(transform);
	}
	
}