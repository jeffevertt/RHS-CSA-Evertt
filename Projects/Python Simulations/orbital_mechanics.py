from lib.window import Window
from lib.utils import *
from lib.winobj_circle import Circle

# Update method  ****** IT IS YOUR JOB TO IMPLEMENT THIS FUNCTION IN THIS LAB ******
# The goal here is to update the meteoroid so that it behaves with physics as you expect.
#   You are simulating outer space & the effects of the sun on your meteoroid.
# If you do this right, you should be able to simulate/visualize all the types of
#   objects in space, those that follow elliptical orbits, parabolic or hyperbolic paths.
# Here's some useful equations...
#   F = G m1 m2 / d^2    <-- Newton's Law of Gravitation 
#                            (gravitational force between two bodies with masses m1 & m2 and at distance d)
#                            (G is the gravitational constant, I recommend using G = 1, though in reality G = 6.67430 × 10⁻¹¹ m³/kg·s²)
#   F = m a              <-- Newton's second law of motion (force = mass * acceleration)
#                            (I recommend using 10000 to 50000 for the sun's mass and 1 to 10 for the meteoroid)
# note: if you'd like to kill off meteroid that crash into the sun, you can call "meteroid.destroy()"
def updateMeteoroidFunction(meteoroid, deltaTime):
    # TODO...use meteoroid.pos and meteoroid.vel (both 2d vectors) and sun.pos
    pass


####################################################################################################
############################## DO NOT MODIFY  METHODS BELOW THIS LINE ##############################
####################################################################################################
def clickReleaseFn(pos, vel):
    vel = limitMag(vel, 75)
    Circle(window, pos, 0.5, vel = vel, color = "gray", updateFn = updateMeteoroidFunction)

window = Window("Lab 08b: Orbital Mechanics", subTitle = "Click, drag, and launch an meteoroid into space...", gridPixelsPerUnit = 8, clickReleaseFn = clickReleaseFn)
sun = Circle(window, v2(0,0), 5, text = "sun", textColor = "black", color = "yellow")
window.runGameLoop()