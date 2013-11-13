Tessawing
=========

Tessawing is "4D flightsim" — a game that has four spatial dimensions. See screenshot.png for a screenshot.

Installation
============

You will have to compile the source files yourself using javac. You must also install [Java 3D](https://java3d.java.net/) and [JAMA](http://math.nist.gov/javanumerics/jama/). To run the game, run Tessawing.class with the java interpreter; make sure the Java 3D and JAMA libraries are in the classpath.

Gameplay
========

Use the arrow keys to change attitude in three dimensions. Use / and SHIFT to roll. Use SPACE to move forward and Z to move backwards. Use the W, A, S, D, Q, and E keys to rotate in the fourth dimension.

How to navigate in 4D
=====================

Three-dimensional space is defined by three coordinate axes: x, y, and z. Each pair of axes gives rise to a primary mode of rotation. Since there are three distinct pairs of axes (xy, xz, and yz), there are three primary rotations (pitch, yaw, and roll). These are the rotations that are available in 3D flightsims.

Four-dimensional space has an extra coordinate axis, w, which is perpendicular to x, y, and z. This gives us six pairs of axes, which means that four-dimensional space has all the primary rotations of three-dimensional space plus three more. These extra rotations are controlled by the W, A, S, D, Q, and E keys.

What you see in the game is a projection of the four-dimensional space onto a three-dimensional hyperplane in the player's frame of reference.