import numpy as np
import tkinter as tk
from PIL import Image, ImageTk
import time
from lib.utils import *
from lib.simulation import Simulation
import aiohttp
import json

class Window:
    def __init__(self, title, width = 1024, height = 600, subTitle = "", gridEnable = True, clickReleaseFn = None, gridPixelsPerUnit = 24, gridOriginAtLL = False, canvasColor = '#F4EAD7', bckGndImage = None):
        # consts
        self.gridPixelsPerUnit = gridPixelsPerUnit
        
        # init params
        self.width = width
        self.height = height
        self.gridOriginPixels = [int(self.width / 2), int(self.height / 2)] if not gridOriginAtLL else [0, self.height]
        self.subTitle = subTitle
        self.gridEnable = gridEnable
        self.timeLastUpdate = time.time()
        self.gravity = v2(0, -9.8)
        
        # sim manager
        self.sim = Simulation()
        
        # graphics ids
        self.gfxSubTitle = None
        self.gfxGridLines = []
        
        # TkInter canvas
        self.root = tk.Tk()
        self.root.title(title)
        self.root.resizable(False, False)  # Disables resizing in both width and height
        self.canvasColor = canvasColor
        self.canvas = tk.Canvas(self.root, width = self.width, height = self.height, bg = self.canvasColor)
        self.canvas.pack()
        
        # background image support
        if bckGndImage is not None:
            img = Image.open(bckGndImage)
            img = img.resize( (self.width, self.height) )
            photo = ImageTk.PhotoImage(img)
            self.canvas.create_image(0, 0, image=photo, anchor="nw")
            self.canvas.pack(fill="both", expand=True)
        
        # mouse support
        self.isMouseLeftDown = False
        self.isMouseRightDown = False
        self.lastMousePos = None
        self.mouseLeftVelHist = []
        self.clickReleaseFn = clickReleaseFn
        self.canvas.bind("<Button-1>", self.onMouseLeftPressed)
        self.canvas.bind("<ButtonRelease-1>", self.onMouseLeftReleased)
        self.canvas.bind("<Double-Button-1>", self.onMouseLeftDoubleClick)
        self.canvas.bind("<Button-3>", self.onMouseRightPressed)
        self.canvas.bind("<ButtonRelease-3>", self.onMouseRightReleased)
        self.canvas.bind("<Motion>", self.onMouseMotion)
        
        # keyboard support
        self.keyStates = {}
        self.canvas.bind_all("<KeyPress>", self.onKeyPress)
        self.canvas.bind_all("<KeyRelease>", self.onKeyReleased)
        
        # grid
        if gridEnable:
            self.createGrid()
        
    def runGameLoop(self):
        self.mainAppLoop(True)
        self.root.mainloop()

    def mainAppLoop(self, firstUpdate = False):
        timeThisUpdate = time.time()
        deltaTime = min(timeThisUpdate - self.timeLastUpdate, 0.1) # min 10 fps
        self.timeLastUpdate = timeThisUpdate
        
        if (firstUpdate):
            self.initApp()
        self.update(deltaTime)
       
        self.root.after(15, self.mainAppLoop)  # Schedule the next loop iteration
        
    def update(self, deltaTime):
        self.sim.update(deltaTime)

    def initApp(self):
        # subtitle
        self.createSubTitle()
        
    def awardPoints(self, points, playerIdx = 0):
        pass
    async def postLeaderboard(self, name, score):
        # only post when players have an actual name
        if name == "Player 1" or name == "Player 2":
            return

        # log it
        log(f"leaderboard POST: {name} ({score})")

        # make the post
        leaderBoardPeriod = 1
        url = "https://u9m0v5iwsk.execute-api.us-west-2.amazonaws.com/default/classBuzzer"
        data = { #"{'TableName':'classLeaderboardTable','EventType':'postLeaderboardStudent','Item':{'periodKey':'" + str(leaderBoardPeriod) + "','StudentName':'" + name + "','score':'" + str(score) + "'}}"
            "TableName": "classLeaderboardTable",
            "EventType": "postLeaderboardStudent",
            "Item": {
                "periodKey": str(leaderBoardPeriod),
                "StudentName": name,
                "score": str(score)
            }
        }
        async with aiohttp.ClientSession() as session:
            async with session.post(url, json = json.dumps(data)) as response:
                response.raise_for_status()
                try:
                    await response.json()
                except aiohttp.ClientError as e:
                    log(f"--> {e}")

    # mouse support
    def onMouseLeftPressed(self, event):
        self.isMouseLeftDown = True
        self.mouseLeftVelHist = []
        self.lastMousePos = None
        mousePos = self.toCoordFrame(v2(event.x, event.y))
        self.lastMousePos = (mousePos, time.time())
        self.mouseLeftVelHist.append(v2(0,0))
    def onMouseLeftReleased(self, event):
        self.isMouseLeftDown = False
        if self.clickReleaseFn != None and len(self.mouseLeftVelHist) > 0:
            mousePos = self.toCoordFrame(v2(event.x, event.y))
            vel = np.mean(self.mouseLeftVelHist, axis = 0)
            self.clickReleaseFn(mousePos, vel)
        self.mouseLeftVelHist = []
        self.lastMousePos = None
    def onMouseLeftDoubleClick(self, event):
        pass
    def onMouseRightPressed(self, event):
        self.isMouseRightDown = True
    def onMouseRightReleased(self, event):
        self.isMouseRightDown = False
    def onMouseMotion(self, event):
        mousePos = self.toCoordFrame(v2(event.x, event.y))
        if self.lastMousePos != None:
            vel = ((mousePos - self.lastMousePos[0]) / max(time.time() - self.lastMousePos[1], 0.01))
            if len(self.mouseLeftVelHist) > 8:
                self.mouseLeftVelHist = self.mouseLeftVelHist[1:]
            self.mouseLeftVelHist.append(vel)
        self.lastMousePos = (mousePos, time.time())
        
    # keyboard support
    def onKeyPress(self, event):
        self.keyStates[event.keysym] = True
    def onKeyReleased(self, event):
        self.keyStates[event.keysym] = False
    def isKeyPressed(self, char):
        return self.keyStates.get(char, False)
        
    # coordFrame vs pixels helpers        
    def toCoordFrameX(self, x):
        return (x-self.gridOriginPixels[0]) / self.gridPixelsPerUnit
    def toCoordFrameY(self, y):
        return -(y-self.gridOriginPixels[1]) / self.gridPixelsPerUnit
    def toCoordFrame(self, pos):
        return v2((pos[0] - self.gridOriginPixels[0]) / self.gridPixelsPerUnit, -(pos[1] - self.gridOriginPixels[1]) / self.gridPixelsPerUnit)
    def toCoordFrameLength(self, len):
        return len / self.gridPixelsPerUnit
    def toPixelsX(self, x):
        return x * self.gridPixelsPerUnit + self.gridOriginPixels[0]
    def toPixelsY(self, y):
        return -y * self.gridPixelsPerUnit + self.gridOriginPixels[1]
    def toPixels(self, pos):
        return v2(pos[0] * self.gridPixelsPerUnit + self.gridOriginPixels[0], -pos[1] * self.gridPixelsPerUnit + self.gridOriginPixels[1])
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
    def coordUL(self):
        return v2(self.minCoordinateX(), self.maxCoordinateY())
    def coordLR(self):
        return v2(self.maxCoordinateX(), self.minCoordinateY())
    def coordUR(self):
        return v2(self.maxCoordinateX(), self.maxCoordinateY())
    def coordLL(self):
        return v2(self.minCoordinateX(), self.minCoordinateY())
    def widthInCoords(self):
        return self.toCoordFrameLength(self.width)
    def heightInCoords(self):
        return self.toCoordFrameLength(self.height)
    
    # draw helpers
    def createGrid(self):
        x = self.gridOriginPixels[0] + self.gridPixelsPerUnit
        lineColor = 'lightgray'
        while x < self.width:
            self.gfxGridLines.append( self.canvas.create_line(x, 0, x, self.height, fill = lineColor, width = 1) )
            x += self.gridPixelsPerUnit
        x = self.gridOriginPixels[0] - self.gridPixelsPerUnit
        while x > 0:
            self.gfxGridLines.append( self.canvas.create_line(x, 0, x, self.height, fill = lineColor, width = 1) )
            x -= self.gridPixelsPerUnit
        y = self.gridOriginPixels[1] + self.gridPixelsPerUnit
        while y < self.height:
            self.gfxGridLines.append( self.canvas.create_line(0, y, self.width, y, fill = lineColor, width = 1) )
            y += self.gridPixelsPerUnit
        y = self.gridOriginPixels[1] - self.gridPixelsPerUnit
        while y > 0:
            self.gfxGridLines.append( self.canvas.create_line(0, y, self.width, y, fill = lineColor, width = 1) )
            y -= self.gridPixelsPerUnit
        self.gfxGridLines.append( self.canvas.create_line(self.gridOriginPixels[0], 0, self.gridOriginPixels[0], self.height, fill = 'darkgray', width = 3) )
        self.gfxGridLines.append( self.canvas.create_line(0, self.gridOriginPixels[1], self.width, self.gridOriginPixels[1], fill = 'darkgray', width = 3) )
    def destroyGrid(self):
        for gridLine in self.gfxGridLines:
            self.canvas.delete(gridLine)
        self.gfxGridLines = []
    def createSubTitle(self):
        if len(self.subTitle) > 0:
            self.gfxSubTitle = self.canvas.create_text( self.toPixelsX(self.minCoordinateX() + 0.25), 
                                                        self.toPixelsY(self.maxCoordinateY() - 0.25), 
                                                        text=self.subTitle, font=("Arial", 14), fill="blue", anchor="nw" )
    def destroySubTitle(self):
        if self.gfxSubTitle != None:
            self.canvas.delete(self.gfxSubTitle)
            self.gfxSubTitle = None