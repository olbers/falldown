## Libgdx Reference Links ##
  * architecture overview: http://code.google.com/p/libgdx/wiki/TheArchitecture
  * very useful javadoc API can be found under the /docs folder of the latest libgdx download
  * examples can be found here: http://code.google.com/p/libgdx/source/browse/#svn%2Ftrunk%2Fdemos

## Version ##
  * We are using the nightly build from 19 Feb 2011

## Basic Transformation Overview (OpenGL) ##
A big part of graphics are coordinate transformations.  In a 2D context, you have (0,0) at the top left, and (width-1,height-1) at the bottom right.  You can change this coordinate frame using transformations as an efficient way to move a lot of things around, or to create the effect of a moving camera.

Basic transform functions:
  * **glLoadIdentity** resets back to normal
  * **glTranslate**
  * **glScale**
  * **glRotate**

### Example ###
If you want to move the camera down, you would translate the coordinate frame up by calling glTranslatef(0,-10,0);  Now, your (0,0) is 10 pixels higher, and everything drawn afterwards will be higher on the screen.

## Managing Resources ##
Resources such as textures and fonts need to be in the /data folder in the desktop project.  In the android project, they need to be in the /assets/data folder.  This was why our phone tests were always crashing.