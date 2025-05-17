from lib.winobj import WinObj
from lib.utils import *

class Circle(WinObj):
    def __init__(self, window, pos, radius, vel = v2(0,0), text = "", textColor = "white", color = "red", updateFn = None):
        super().__init__(window, pos, vel)
        self.radius = radius
        self.text = text
        self.textColor = textColor
        self.color = color
        self.window.sim.onCreated(self)
        self.updateFn = updateFn
        self.createGfx()
            
    def destroy(self):
        self.destroyGfx()
        super().destroy()
        
    def createGfx(self):
        self.gfxCircle = self.window.canvas.create_oval(self.window.toPixelsX(self.pos[0] - self.radius), self.window.toPixelsY(self.pos[1] - self.radius), 
                                                        self.window.toPixelsX(self.pos[0] + self.radius), self.window.toPixelsY(self.pos[1] + self.radius), 
                                                        fill = self.color, outline = "black", width = 2)
        if len(self.text) > 0:
            self.gfxText = self.window.canvas.create_text(self.window.toPixelsX(self.pos[0]), self.window.toPixelsY(self.pos[1]), 
                                                        text = self.text, font=("Arial", 12), fill = self.textColor)
        else:
            self.gfxText = None
    def destroyGfx(self):
        if self.gfxCircle != None:
            self.window.canvas.delete(self.gfxCircle)
            self.gfxCircle = None
        if self.gfxText != None:
            self.window.canvas.delete(self.gfxText)
            self.gfxText = None
    def updateGfx(self):
        if self.gfxCircle != None:
            self.window.canvas.coords(self.gfxCircle, self.window.toPixelsX(self.pos[0] - self.radius), self.window.toPixelsY(self.pos[1] - self.radius), 
                                                      self.window.toPixelsX(self.pos[0] + self.radius), self.window.toPixelsY(self.pos[1] + self.radius))
        if self.gfxText != None:
            self.window.canvas.coords(self.gfxText, self.window.toPixelsX(self.pos[0]), self.window.toPixelsY(self.pos[1]))
    
    def update(self, deltaTime):
        if self.updateFn != None:
            self.updateFn(self, deltaTime)
        self.updateGfx()
        super().update(deltaTime)
