
class Simulation:
    def __init__(self):
        self.objects = []

    def onCreated(self, obj):
        if obj not in self.objects:
            self.objects.append(obj)
    
    def onDestroyed(self, obj):
        if obj in self.objects:
            self.objects.remove(obj)

    def destroyAll(self):
        objects = self.objects[:]   # copy the list to avoid issues of removal during destruction
        for obj in objects:
            obj.destroy()
        self.objects = []
        
    def destroyAllOfType(self, type):
        objs = self.objectsOfType(type)
        for obj in objs:
            obj.destroy()
        
    def objectsOfType(self, type):
        return [obj for obj in self.objects if isinstance(obj, type)]
    
    def countObjectsOfType(self, type):
        return len([obj for obj in self.objects if isinstance(obj, type)])
    
    def firstObjectOfType(self, type):
        objs = self.objectsOfType(type)
        return objs[0] if len(objs) > 0 else None
    
    def updateGfxAllObjects(self):
        for obj in self.objects:
            obj.updateGfx()

    def update(self, deltaTime):
        for obj in self.objects:
            obj.update(deltaTime)
