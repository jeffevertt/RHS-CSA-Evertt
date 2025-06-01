import numpy as np
import pygame
from lib.utils import *

# PROVIDED: A realtime 2d wave simulation using a simplified form of the discretized wave equation.
# GOAL: Add something unique and interesting to the simulation. Here are a couple sample ideas...
#        - Simulation small objects/particles/creatures that react to the water's surface.
#            these could be controlled by the user or fully autonomous. Consider making them buoyant and 
#            causing their own fluctuations in the water. 
#        - Modify the visualization to render light reflected off the surface towards the camera
#            to create a more realistic looking water surface. You will need to compute a gradient vector
#            for the water's surface.
# ADVICE: Start simple, try to understand what is happening and what each variable represents.
#           Choose and idea that can scale from simple to more advanced. 

# configuration
WIDTH_SIM, HEIGHT_SIM = 100, 100
WIDTH_WINDOW, HEIGHT_WINDOW = WIDTH_SIM * 8, HEIGHT_SIM * 8
DAMPING = 0.975
MOUSE_DRAW_RADIUS = 2.5
MOUSE_DRAW_PRESSURE = 0.75

# height fields
heightPrev = np.zeros((WIDTH_SIM, HEIGHT_SIM))
heightCur = np.zeros((WIDTH_SIM, HEIGHT_SIM))
heightNext = np.zeros((WIDTH_SIM, HEIGHT_SIM))

# pygame setup
pygame.init()
pygame.display.set_caption("2D Realtime Wave Simulation")
screen = pygame.display.set_mode((WIDTH_WINDOW, HEIGHT_WINDOW))
clock = pygame.time.Clock()

# image (copy sim values to it, then scale to screen)
img = np.zeros((WIDTH_SIM, HEIGHT_SIM, 3), dtype = np.uint8)
surface = pygame.Surface((WIDTH_SIM, HEIGHT_SIM))

# convert from [-1,1] -> 3 channel [0,255] tuple
def valueToColor(value):
    value = clamp(value, -1.0, 1.0)
    r = int((1.0 + value) * 127.0)
    g = int((1.0 + value) * 127.0)
    b = int((1.0 + value) * 127.0)
    return r, g, b
def toSimCoords(pos):
    return int(WIDTH_SIM - pos[0] / WIDTH_WINDOW * WIDTH_SIM), int(pos[1] / HEIGHT_WINDOW * HEIGHT_SIM)

# simulation loop
running = True
while running:
    # check for exit (escape key)
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False

    # cursor paints into the current height field (adds ripples)
    mouseX, mouseY = toSimCoords( pygame.mouse.get_pos() )
    mousePos = v2(mouseX, mouseY)
    if (pygame.mouse.get_pressed()[0] or pygame.mouse.get_pressed()[2]) and lastMousePos is not None:
        # turn on cells under line seg
        mousePressure = MOUSE_DRAW_PRESSURE if pygame.mouse.get_pressed()[0] else -MOUSE_DRAW_PRESSURE
        minX = max(min(math.floor(lastMousePos[0]-MOUSE_DRAW_RADIUS), math.floor(mousePos[0]-MOUSE_DRAW_RADIUS)), 0)
        maxX = min(max(math.ceil(lastMousePos[0]+MOUSE_DRAW_RADIUS), math.ceil(mousePos[0]+MOUSE_DRAW_RADIUS)), WIDTH_SIM)
        minY = max(min(math.floor(lastMousePos[1]-MOUSE_DRAW_RADIUS), math.floor(mousePos[1]-MOUSE_DRAW_RADIUS)), 0)
        maxY = min(max(math.ceil(lastMousePos[1]+MOUSE_DRAW_RADIUS), math.ceil(mousePos[1]+MOUSE_DRAW_RADIUS)), HEIGHT_SIM)
        for y in range(minY, maxY):
            for x in range(minX, maxX):
                dst = distancePointToSegment(v2(x, y), lastMousePos, mousePos - lastMousePos)
                if dst < MOUSE_DRAW_RADIUS:
                    heightCur[x, y] = clamp(heightCur[x, y] + (1.0 - dst / MOUSE_DRAW_RADIUS)**0.5 * mousePressure, -1.0, 1.0)
    lastMousePos = mousePos
                    
    # update simulation
    for mouseX in range(1, WIDTH_SIM-1):
        for mouseY in range(1, HEIGHT_SIM-1):
            heightNext[mouseX, mouseY] = (heightCur[mouseX-1, mouseY] + heightCur[mouseX+1, mouseY] + heightCur[mouseX, mouseY-1] + heightCur[mouseX, mouseY+1]) / 2.0 - heightPrev[mouseX, mouseY]
            heightNext[mouseX, mouseY] *= DAMPING
    
    # enforce boundary counditions
    heightNext[0, :] = 0
    heightNext[-1, :] = 0
    heightNext[:, 0] = 0
    heightNext[:, -1] = 0

    # swap buffers
    heightPrev, heightCur, heightNext = heightCur, heightNext, heightPrev

    # draw to an image, which will get copied onto the screen (and scaled up to the screen)
    for mouseX in range(WIDTH_SIM):
        for mouseY in range(HEIGHT_SIM):
            img[mouseX, mouseY] = valueToColor(heightCur[mouseX, mouseY])
    img = np.flip(img, axis = 0)    # flip horizontally (X-axis)

    # update the image/surface
    pygame.surfarray.blit_array(surface, img)

    # draw
    scaled = pygame.transform.scale(surface, (WIDTH_WINDOW, HEIGHT_WINDOW))
    screen.blit(scaled, (0, 0))
    pygame.display.flip()
    clock.tick(60)

pygame.quit()
