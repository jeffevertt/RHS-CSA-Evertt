import numpy as np
import ipycanvas as cv
import ipywidgets as widgets
from IPython.display import display
from ipywidgets import Image
from itertools import chain
import time
import math
from lib.utils import *

class Canvas:
    def __init__(self, title = "", width = 600, height = 400, gridPixelsPerUnit = 20, gridEnable = True, textBoxConfig = None):
        self.title = title
        self.subTitle = ""
        self.subTitleColor = "green"
        self.width = width
        self.height = height
        self.gridEnable = gridEnable
        self.textBoxConfig = textBoxConfig
        self.gridPixelsPerUnit = gridPixelsPerUnit
        self.gridOriginPixels = [int(self.width / 2), int(self.height / 2)]
        self.canvasObjects = []
        self.result = None
        self.resultImage = None
        self.preObjCB = None
        self.create()

    def create(self):
        # create the canvas & debug output
        self.canvas = cv.Canvas(width = self.width, height = self.height, layout = {'border': '3px solid black'} )
        self.output = widgets.Output(width = self.width, height = self.height)
        
        # input text box
        if self.textBoxConfig != None:
            self.inputTextBox = widgets.Text(value = '', placeholder = self.textBoxConfig['placeholder'], description = self.textBoxConfig['prompt'], disabled = False)
        
        # layout
        self.canvas.layout.min_width = str(self.width) + 'px'
        self.output.layout.height = str(self.height) + 'px'
        if self.textBoxConfig == None:
            self.layout = widgets.HBox([self.canvas, self.output])
        else:
            inputTextBoxHeight = 50
            self.inputTextBox.layout.height = str(inputTextBoxHeight) + 'px'
            self.output.layout.height = str(self.height - inputTextBoxHeight) + 'px'
            inputOutputWidgets = widgets.VBox([self.inputTextBox, self.output])
            self.layout = widgets.HBox([self.canvas, inputOutputWidgets])
        
        # update timer
        self.updateLastTime = time.time()
        
        # capture mouse & keyboard events
        self.canvas.on_mouse_down(self.handle_mouse_down)
        self.canvas.on_key_down(self.handle_key_down)
    
    def setTitle(self, text):
        self.title = text
    
    def setSubTitle(self, text, color = 'green'):
        self.subTitle = text
        self.subTitleColor = color
        
    def setPreObjCB(self, preObjCB):
        self.preObjCB = preObjCB
    
    # log into the right panel
    def log(self, text):
        with self.output:
            print(text)

    # mouse support            
    def handle_mouse_down(self, x, y):
        clickPos = self.toCoordFrame( [x, y] )
        self.log(f"click -> {np.round(clickPos, 3)}")
    def handle_key_down(self, key, shift_key, ctrl_key, meta_key):
        pass
 
    # coordFrame vs pixels helpers        
    def toCoordFrameX(self, x):
        return (x-self.gridOriginPixels[0]) / self.gridPixelsPerUnit
    def toCoordFrameY(self, y):
        return -(y-self.gridOriginPixels[1]) / self.gridPixelsPerUnit
    def toCoordFrame(self, pos):
        return [(pos[0] - self.gridOriginPixels[0]) / self.gridPixelsPerUnit, -(pos[1] - self.gridOriginPixels[1]) / self.gridPixelsPerUnit]
    def toCoordFrameLength(self, len):
        return len / self.gridPixelsPerUnit
    def toPixelsX(self, x):
        return x * self.gridPixelsPerUnit + self.gridOriginPixels[0]
    def toPixelsY(self, y):
        return -y * self.gridPixelsPerUnit + self.gridOriginPixels[1]
    def toPixels(self, pos):
        return [pos[0] * self.gridPixelsPerUnit + self.gridOriginPixels[0], -pos[1] * self.gridPixelsPerUnit + self.gridOriginPixels[1]]
    def toPixelsLength(self, len):
        return len * self.gridPixelsPerUnit
    def maxCoordinateX(self):
        return self.toCoordFrameX(self.width)
    def minCoordinateX(self):
        return self.toCoordFrameX(0)
    def maxCoordinateY(self):
        return self.toCoordFrameY(0)
    def minCoordinateY(self):
        return self.toCoordFrameY(self.height)
    
    # canvas object helpers
    def onCreated(self, canvasObject):
        if canvasObject not in self.canvasObjects:
            self.canvasObjects.append(canvasObject)
    def onDestroyed(self, canvasObject):
        self.canvasObjects.remove(canvasObject)
        
    # result support
    def setResult(self, result):
        self.result = result
        self.drawWorld()
        
    def update(self):
        # figure out how much time has passed
        timeCurrent = time.time()
        deltaTime = timeCurrent - self.updateLastTime
        deltaTime = min(deltaTime, 0.01) # slow down the simulation if we're not keeping up
        
        # canvasObjects
        for canvasObject in self.canvasObjects:
            canvasObject.update(deltaTime)
            
    def display(self):
        # setup horizontal layout, show, and do an initial paint
        display(self.layout)
        self.drawWorld()
        
    # canvas draw helpers
    def drawCircle(self, pos, radius, color):
        self.canvas.fill_style = color
        self.canvas.fill_circle(self.toPixelsX(pos[0]), self.toPixelsY(pos[1]), self.toPixelsLength(radius))
        self.canvas.stroke_style = 'black'
        self.canvas.stroke_width = 1
        self.canvas.stroke_circle(self.toPixelsX(pos[0]), self.toPixelsY(pos[1]), self.toPixelsLength(radius))
        
    def drawText(self, pos, text, color, fontSize = 18, centerHoriz = True, centerVert = True):
        self.canvas.font = str(fontSize) + "px serif"
        self.canvas.text_align = "center" if centerHoriz else "start"
        self.canvas.text_baseline = "middle" if centerVert else "alphabetic"
        self.canvas.fill_style = color
        self.canvas.fill_text(text, self.toPixelsX(pos[0]), self.toPixelsY(pos[1]))
    
    def drawLine(self, posA, posB, color, width = 2):
        self.canvas.stroke_style = color
        self.canvas.line_width = width
        self.canvas.stroke_line(self.toPixelsX(posA[0]), self.toPixelsY(posA[1]), self.toPixelsX(posB[0]), self.toPixelsY(posB[1]))
        
    def drawVector(self, start, end, color, width = 2, arrowLen = 0.25):
        self.canvas.stroke_style = color
        self.canvas.line_width = width
        self.canvas.stroke_line(self.toPixelsX(start[0]), self.toPixelsY(start[1]), self.toPixelsX(end[0]), self.toPixelsY(end[1]))
        self.canvas.line_width = width - 1
        arrowEnd0 = end + rotateVec2(unit(start - end) * arrowLen, 20)
        arrowEnd1 = end + rotateVec2(unit(start - end) * arrowLen, -20)
        self.canvas.stroke_line(self.toPixelsX(end[0]), self.toPixelsY(end[1]), self.toPixelsX(arrowEnd0[0]), self.toPixelsY(arrowEnd0[1]))
        self.canvas.stroke_line(self.toPixelsX(end[0]), self.toPixelsY(end[1]), self.toPixelsX(arrowEnd1[0]), self.toPixelsY(arrowEnd1[1]))
        
    def drawArcAngle(self, center, radius, angle0, angle1, color, width = 2, drawArrows = True):
        self.canvas.stroke_style = color
        self.canvas.line_width = width
        self.canvas.stroke_arc(self.toPixelsX(center[0]), self.toPixelsY(center[1]), self.toPixelsLength(radius), -math.radians(angle0), -math.radians(angle1), anticlockwise = True)
        
    def drawRectOriented(self, center, halfDims, angleDeg, color):
        self.canvas.translate(self.toPixelsX(center[0]), self.toPixelsY(center[1]))
        self.canvas.rotate(-math.radians(angleDeg))
        self.canvas.fill_style = color
        self.canvas.stroke_style = 'black'
        self.canvas.stroke_width = 2
        self.canvas.fill_rect(self.toPixelsLength(-halfDims[0]), self.toPixelsLength(-halfDims[1]), self.toPixelsLength(halfDims[0] * 2), self.toPixelsLength(halfDims[1] * 2))
        self.canvas.reset_transform()
        
    def drawGrid(self):
        self.canvas.stroke_style = '#80808044'
        self.canvas.line_width = 1
        for x in chain(range(-self.gridPixelsPerUnit, -self.gridOriginPixels[0], -self.gridPixelsPerUnit), range(self.gridPixelsPerUnit, self.gridOriginPixels[0], self.gridPixelsPerUnit)):
            self.canvas.stroke_line(self.gridOriginPixels[0] + x, 0, self.gridOriginPixels[0] + x, self.height)
        for y in chain(range(-self.gridPixelsPerUnit, -self.gridOriginPixels[1], -self.gridPixelsPerUnit), range(self.gridPixelsPerUnit, self.gridOriginPixels[1], self.gridPixelsPerUnit)):
            self.canvas.stroke_line(0, self.gridOriginPixels[1] + y, self.width, self.gridOriginPixels[1] + y)
        self.canvas.stroke_style = 'darkgray'
        self.canvas.line_width = 3
        self.canvas.stroke_line(self.gridOriginPixels[0], 0, self.gridOriginPixels[0], self.height)
        self.canvas.stroke_line(0, self.gridOriginPixels[1], self.width, self.gridOriginPixels[1])
        
    def drawResult(self):
        if self.result == None:
            return
        if self.resultImage == None:
            self.resultImage = Image.from_file("res/check-mark.png" if self.result else "res/x.png")
        if self.resultImage != None:
            self.canvas.draw_image(self.resultImage, self.toPixelsX(0) - 150, self.toPixelsY(0) - 150, width = 300, height = 300)
        
    def drawWorld(self):
        with cv.hold_canvas():
            # background
            self.canvas.fill_style = 'lightgray'
            self.canvas.fill_rect(0, 0, self.canvas.width, self.canvas.height)
            # grid
            if self.gridEnable:
                self.drawGrid()
            # preObjCB
            if self.preObjCB != None:
                self.preObjCB()
            # canvasObjects
            for canvasObject in self.canvasObjects:
                canvasObject.draw()
            # title
            if len(self.title):
                self.drawText([self.minCoordinateX() + 5 / self.gridPixelsPerUnit, self.maxCoordinateY() - 12 / self.gridPixelsPerUnit], self.title, "blue", fontSize = 18, centerHoriz = False)
            # subTitle
            if len(self.subTitle):
                self.drawText([self.minCoordinateX() + 5 / self.gridPixelsPerUnit, self.maxCoordinateY() - 32 / self.gridPixelsPerUnit], self.subTitle, self.subTitleColor, fontSize = 16, centerHoriz = False)
            # result
            self.drawResult()