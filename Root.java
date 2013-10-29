import Jama.*;
import javax.media.j3d.*;
import java.util.*;
import javax.vecmath.*;

// The root node of the scene tree is supposed to be a Root object. When the player wants to move around in the scene, we instead manipulate the Root object, causing everything in the scene to move around the point of view, simulating motion.

class Root extends Node{

// As written, linear advancement works just like rotation, which means you have to hold down the space bar to move. In future versions, the player will be moving forward by default.

long lastTime = 0;
// forward velocity
double velocity = 0;
// we will accelerate to targetVelocity
double targetVelocity = 0;
// targetVelocity is one of {-maximumVelocity, 0, maximumVelocity}
double maximumVelocity = 2;
// this variable doesn't actually get used ever
double maximumAcceleration = 3;
// angular velocity works like forward velocity, except there are six components
Matrix angularVelocity = new Matrix(6, 1);
Matrix targetAngularVelocity = new Matrix(6, 1);
double maximumAngularVelocityComponent = 1.5;
double maximumAngularAcceleration = 2.3;
// this is a basis for the lie algebra so(4) as a  subspace of the 4x4 matrices: pitch, yaw, roll, and three other rotations.
double[][][] rotationsd_ = {
	{{0, 0, 0, 0},
	 {0, 0, -1, 0},
	 {0, 1, 0, 0},
	 {0, 0, 0, 0}},
	{{0, 0, -1, 0},
	 {0, 0, 0, 0},
	 {1, 0, 0, 0},
	 {0, 0, 0, 0}},
	{{0, 1, 0, 0},
	 {-1, 0, 0, 0},
	 {0, 0, 0, 0},
	 {0, 0, 0, 0}},
	{{0, 0, 0, 0},
	 {0, 0, 0, 1},
	 {0, 0, 0, 0},
	 {0,-1, 0, 0}},
	{{0, 0, 0, 1},
	 {0, 0, 0, 0},
	 {0, 0, 0, 0},
	 {-1, 0, 0, 0}},
	{{0, 0, 0, 0},
	 {0, 0, 0, 0},
	 {0, 0, 0, 1},
	 {0, 0,-1, 0}}};
Matrix[] rotations = new Matrix[6];
	 
Root(){
	for(int i=0; i<6; i++){
		rotations[i] = new Matrix(rotationsd_[i]);
		}
	}

public void initialize(){
	// as long as the ViewPlatform is within 10 units of the origin, the player will be able to keep moving. I think we need to do this.
	setSchedulingBounds(new BoundingBox(new Point3d(-10, -10, -10), new Point3d(10, 10, 10)));
	wakeupOn(new WakeupOnElapsedFrames(0));
	}
	
public void processStimulus(Enumeration e){
	//System.out.println("angularVelocity = " + Arrays.toString(angularVelocity.getRowPackedCopy()));
	// in the old version I used a synchronized(this) around everything in this method except the call to wakeupOn(). I don't know why I did that and it seems like a bad idea in the current version.
	double time = getElapsedTime();
	// first, update velocities.
	double velocityDifference = targetVelocity - velocity;
	if(velocityDifference > 0) velocityDifference = Math.min(velocityDifference, maximumVelocity);
	else velocityDifference = Math.max(velocityDifference, -maximumVelocity);
	velocity += velocityDifference;
	// next, update angular velocity
	Matrix angularVelocityDifference = targetAngularVelocity.minus(angularVelocity);
	if(angularVelocityDifference.normF() != 0) angularVelocityDifference.timesEquals(Math.min(angularVelocityDifference.normF(), maximumAngularAcceleration*time) / angularVelocityDifference.normF());
	angularVelocity = angularVelocity.plus(angularVelocityDifference);
	// next, update orientation
	Matrix differentialRotation = Matrix.identity(4, 4);
	for(int i=0; i<6; i++){
		differentialRotation.minusEquals(rotations[i].times(angularVelocity.get(i, 0)*time));
		}
	orientation = differentialRotation.times(orientation);
	// next, update position
	Matrix temp = new Matrix(4, 1);
	temp.set(2, 0, velocity * time);
	position = differentialRotation.times(position.plus(temp));
	// since we added a tangent vector to an element of SO(4), orientation is no longer quite orthonormal. if it's too far off, we'll have to reorthonormalize.
	double aberration = orthonormalAberration(orientation);
	/*System.out.print("orthonormal aberration: " + aberration);
	if(aberration > .00001){
		System.out.println(" reorthonormalizing... ");
		orientation = reorthonormalize(orientation);
		}
	else System.out.println("ok");
	*/
	update();
	wakeupOn(new WakeupOnElapsedFrames(0));
	}
	
double orthonormalAberration(Matrix matrix){
	int m = matrix.getRowDimension();
	int n = matrix.getColumnDimension();
	double norms = 0;
	double dotProducts = 0;
	for(int i=0; i<n; i++){
		Matrix columnI = matrix.getMatrix(0, m - 1, i, i);
		norms += columnI.normF();
		for(int j=i+1; j<n; j++){
			Matrix columnJ = matrix.getMatrix(0, m - 1, j, j);
			dotProducts += Math.abs(columnI.transpose().times(columnJ).get(0, 0));
			}
		}
	return Math.sqrt(norms / n) + dotProducts - 1;
	}
	
Matrix reorthonormalize(Matrix matrix){
	// Use Jama's SVD because vecmath's SVD gives NAN sometimes and Jama's EVD hangs a lot.
	SingularValueDecomposition decomp = new SingularValueDecomposition(matrix);
	return decomp.getU().times(decomp.getV().transpose());
	}
	
double getElapsedTime(){
	if(lastTime == 0){
		lastTime = System.currentTimeMillis();
		return 0;
		}
	long now = System.currentTimeMillis();
	double retVal = ((double)(now - lastTime)) / 1000;
	lastTime = now;
	return retVal;
	}
	
}