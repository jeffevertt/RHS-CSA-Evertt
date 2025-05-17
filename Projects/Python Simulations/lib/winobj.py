from lib.window import Window
from lib.utils import *

class WinObj:
    def __init__(self, window, pos, vel = v2(0,0)):
        self.window = window
        self.pos = pos.copy()
        self.vel = vel.copy()

    def destroy(self):
        self.window.sim.onDestroyed(self)
    
    def isDying():
        return False
        
    def shouldBeCulled(self):
        return self.isOutOfBounds()
    def isOutOfBounds(self, minDst = 20):
        return self.pos[0] + minDst < self.window.minCoordinateX() or self.pos[0] - minDst > self.window.maxCoordinateX() or self.pos[1] + minDst < self.window.minCoordinateY() or self.pos[1] - minDst > self.window.maxCoordinateY()
    
    def updateGfx(self):
        pass
    
    def update(self, deltaTime):
        # check for death condition
        if self.shouldBeCulled():
            self.destroy()