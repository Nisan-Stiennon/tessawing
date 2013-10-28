//package Guacamole;

import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.universe.*;
import Jama.*;
import java.util.*;
import javax.vecmath.*;

class World_Test{

static Root initializeWorld(SimpleUniverse universe){
	BranchGroup sceneGraph = new BranchGroup();
	Root parent = new Root();
	// I think we need to add parent to the universe so that parent.processStimulus() will be called every frame.
	sceneGraph.addChild(parent);
	World_Test.addColorCube(sceneGraph, parent, .4, 2, 0, 0, 0);
	World_Test.addColorCube(sceneGraph, parent, .4, -2, 0, 0, 0);
	World_Test.addColorCube(sceneGraph, parent, .4, 0, 2, 0, 0);
	World_Test.addColorCube(sceneGraph, parent, .4, 0, -2, 0, 0);
	World_Test.addColorCube(sceneGraph, parent, .4, 0, 0, 2, 0);
	World_Test.addColorCube(sceneGraph, parent, .4, 0, 0, -2, 0);
	World_Test.addColorCube(sceneGraph, parent, .4, 2, 2, 2, 2);
	universe.addBranchGraph(sceneGraph);
	return parent;
	}

// there should be a superclass World and an add3DShape method
	
static void addColorCube(BranchGroup bg, Node parent, double size, double x, double y, double z, double w){
	TransformGroup tg = new TransformGroup();
	tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	bg.addChild(tg);
	ColorCube cube = new ColorCube(size);
	// make a clone with opposite normals
	ColorCube clone = new ColorCube(size);
	GeometryArray cloneGeo = (QuadArray)clone.getGeometry();
	for(int i=0; i<cloneGeo.getVertexCount(); i+=4){
		Point3d p = new Point3d();
		Point3d q = new Point3d();
		cloneGeo.getCoordinate(i, p);
		cloneGeo.getCoordinate(i + 3, q);
		cloneGeo.setCoordinate(i, q);
		cloneGeo.setCoordinate(i + 3, p);
		cloneGeo.getCoordinate(i + 1, p);
		cloneGeo.getCoordinate(i + 2, q);
		cloneGeo.setCoordinate(i + 1, q);
		cloneGeo.setCoordinate(i + 2, p);
		// if you're generalizing this code, you should also do colors and textures and normals, etc.
		}
	tg.addChild(cube);
	tg.addChild(clone);
	Node child = new Leaf(tg);
	parent.add(child);
	double[] positionArray = {x, y, z, w};
	child.setPosition(new Matrix(positionArray, 4));
	}
	
}