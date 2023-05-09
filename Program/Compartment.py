from Sensor import *

# This is a blueprint for a compartment of a backpack.
# This compartment has, often, multiple sensors and the compartment can checked if it's filled with something
class Compartment:
    # parameters:
    # - sensors, a tuple that holds all the sensors that are present in the compartment
    def __init__(self, id):
        self.id = id
        self.sensors = []
        self.filled = False
    
    # checks if the compartment is filled with an physical object or not
    def isFilled(self):
        for sensor in self.sensors:
           if sensor.isPressed():
               self.filled = True
               return True
        self.filled = False
        return False