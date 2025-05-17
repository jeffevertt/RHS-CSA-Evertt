from lib.canvas import Canvas

class CanvasObject:
    def __init__(self, canvas, pos):
        self.canvas = canvas
        self.pos = pos

    def destroy(self):
        self.canvas.onDestroyed(self)
        
    def update(self, deltaTime):
        raise NotImplementedError("Subclasses may implement the update method")

    def draw(self):
        raise NotImplementedError("subclass to implement")