//package Guacamole;

import Jama.*;
import java.util.*;
import javax.media.j3d.*;

class Node extends Behavior{

// position and orientation represent an affine transformation
Matrix orientation;
Matrix position;
// absolutePosition and absoluteOrientation are inherited from ancestor Nodes
Matrix absoluteOrientation;
Matrix absolutePosition;
Vector<Node> children;

Node(){
	position = new Matrix(4, 1);
	orientation = Matrix.identity(4, 4);
	absolutePosition = new Matrix(4, 1);
	absoluteOrientation = Matrix.identity(4, 4);
	children = new Vector<Node>();
	}

void add(Node child){
	children.add(child);
	updateChild(child);
	}
	
void update(){
	for(int i=0; i<children.size(); i++){
		updateChild(children.get(i));
		}
	}
	
void update(Matrix newOri, Matrix newPos){
	absoluteOrientation = newOri;
	absolutePosition = newPos;
	update();
	}
	
void updateChild(Node child){
	child.update(absoluteOrientation.times(orientation), absolutePosition.plus(absoluteOrientation.times(position)));
	}
	
void setOrientation(Matrix orientation){
	this.orientation = orientation;
	update();
	}
	
void setPosition(Matrix position){
	this.position = position;
	update();
	}
	
void modifyAffine(Matrix chgOrientation, Matrix chgPosition){
	orientation = orientation.times(chgOrientation);
	position = position.plus(chgPosition);
	for(int i=0; i<children.size(); i++){
		updateChild(children.get(i));
		}
	}
	
// initialize() and processStimulus() need to be implemented because this class extends Behavior.

public void initialize(){}

public void processStimulus(Enumeration e){}
	
}